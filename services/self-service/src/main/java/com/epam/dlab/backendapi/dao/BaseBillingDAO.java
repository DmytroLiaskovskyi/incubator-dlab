/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.epam.dlab.backendapi.dao;

import com.epam.dlab.auth.UserInfo;
import com.epam.dlab.backendapi.resources.dto.BillingFilter;
import com.epam.dlab.dto.UserInstanceStatus;
import com.epam.dlab.dto.base.DataEngineType;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.client.FindIterable;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;

import static com.epam.dlab.backendapi.dao.ComputationalDAO.COMPUTATIONAL_ID;
import static com.epam.dlab.backendapi.dao.ExploratoryDAO.COMPUTATIONAL_RESOURCES;
import static com.epam.dlab.backendapi.dao.ExploratoryDAO.EXPLORATORY_ID;
import static com.epam.dlab.backendapi.dao.MongoCollections.BILLING;
import static com.epam.dlab.backendapi.dao.MongoCollections.USER_INSTANCES;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static java.util.Collections.singletonList;

@Slf4j
public abstract class BaseBillingDAO<T extends BillingFilter> extends BaseDAO implements BillingDAO<T> {

	public static final String SHAPE = "shape";
	public static final String SERVICE_BASE_NAME = "service_base_name";
	public static final String ITEMS = "lines";
	public static final String COST_TOTAL = "cost_total";
	public static final String FULL_REPORT = "full_report";

	private static final String MASTER_NODE_SHAPE = "master_node_shape";
	private static final String SLAVE_NODE_SHAPE = "slave_node_shape";
	private static final String TOTAL_INSTANCE_NUMBER = "total_instance_number";

	private static final String DATAENGINE_SHAPE = "dataengine_instance_shape";
	private static final String DATAENGINE_INSTANCE_COUNT = "dataengine_instance_count";

	private static final String DATAENGINE_DOCKER_IMAGE = "image";
	private static final int ONE_HUNDRED = 100;
	private static final String TOTAL_FIELD_NAME = "total";
	private static final String COST_FIELD = "$cost";
	public static final String SHARED_RESOURCE_NAME = "Shared resource";

	@Inject
	protected SettingsDAO settings;
	@Inject
	private UserSettingsDAO userSettingsDAO;

	protected Map<String, ShapeInfo> getShapes(List<String> shapeNames) {
		FindIterable<Document> userInstances = getUserInstances();
		final Map<String, ShapeInfo> shapes = new HashMap<>();

		for (Document d : userInstances) {
			getExploratoryShape(shapeNames, d)
					.ifPresent(shapeInfo -> shapes.put(d.getString(EXPLORATORY_ID), shapeInfo));
			@SuppressWarnings("unchecked")
			List<Document> comp = (List<Document>) d.get(COMPUTATIONAL_RESOURCES);
			comp.forEach(computational ->
					getComputationalShape(shapeNames, computational)
							.ifPresent(shapeInfo -> shapes.put(computational.getString(COMPUTATIONAL_ID), shapeInfo)));
		}

		appendSsnAndEdgeNodeType(shapeNames, shapes);

		log.trace("Loaded shapes is {}", shapes);
		return shapes;
	}

	@Override
	public Double getTotalCost() {
		return aggregateBillingData(singletonList(group(null, sum(TOTAL_FIELD_NAME, COST_FIELD))));
	}

	@Override
	public Double getUserCost(String user) {
		final List<Bson> pipeline = Arrays.asList(match(eq(USER, user)),
				group(null, sum(TOTAL_FIELD_NAME, COST_FIELD)));
		return aggregateBillingData(pipeline);
	}

	@Override
	public int getBillingQuoteUsed() {
		return toPercentage(() -> settings.getMaxBudget(), getTotalCost());
	}

	@Override
	public int getBillingUserQuoteUsed(String user) {
		return toPercentage(() -> userSettingsDAO.getAllowedBudget(user), getUserCost(user));
	}

	@Override
	public boolean isBillingQuoteReached() {
		return getBillingQuoteUsed() >= ONE_HUNDRED;
	}

	@Override
	public boolean isUserQuoteReached(String user) {
		final Double userCost = getUserCost(user);
		return userSettingsDAO.getAllowedBudget(user)
				.filter(allowedBudget -> userCost.intValue() != 0 && allowedBudget <= userCost)
				.isPresent();
	}

	protected String getUserOrDefault(String user) {
		return StringUtils.isNotBlank(user) ? user : SHARED_RESOURCE_NAME;
	}

	private Integer toPercentage(Supplier<Optional<Integer>> allowedBudget, Double totalCost) {
		return allowedBudget.get()
				.map(userBudget -> (totalCost * ONE_HUNDRED) / userBudget)
				.map(Double::intValue)
				.orElse(BigDecimal.ZERO.intValue());
	}


	private Optional<ShapeInfo> getComputationalShape(List<String> shapeNames, Document c) {
		return isDataEngine(c.getString(DATAENGINE_DOCKER_IMAGE)) ? getDataEngineShape(shapeNames, c) :
				getDataEngineServiceShape(shapeNames, c);
	}

	private Double aggregateBillingData(List<Bson> pipeline) {
		return Optional.ofNullable(aggregate(BILLING, pipeline).first())
				.map(d -> d.getDouble(TOTAL_FIELD_NAME))
				.orElse(BigDecimal.ZERO.doubleValue());
	}

	private FindIterable<Document> getUserInstances() {
		return getCollection(USER_INSTANCES)
				.find()
				.projection(
						fields(excludeId(),
								include(SHAPE, EXPLORATORY_ID, STATUS,
										COMPUTATIONAL_RESOURCES + "." + COMPUTATIONAL_ID,
										COMPUTATIONAL_RESOURCES + "." + MASTER_NODE_SHAPE,
										COMPUTATIONAL_RESOURCES + "." + SLAVE_NODE_SHAPE,
										COMPUTATIONAL_RESOURCES + "." + TOTAL_INSTANCE_NUMBER,
										COMPUTATIONAL_RESOURCES + "." + DATAENGINE_SHAPE,
										COMPUTATIONAL_RESOURCES + "." + DATAENGINE_INSTANCE_COUNT,
										COMPUTATIONAL_RESOURCES + "." + DATAENGINE_DOCKER_IMAGE,
										COMPUTATIONAL_RESOURCES + "." + STATUS
								)));
	}

	private Optional<ShapeInfo> getExploratoryShape(List<String> shapeNames, Document d) {
		final String shape = d.getString(SHAPE);
		if (isShapeAcceptable(shapeNames, shape)) {
			return Optional.of(new ShapeInfo(shape, UserInstanceStatus.of(d.getString(STATUS))));
		}
		return Optional.empty();
	}

	private boolean isDataEngine(String dockerImage) {
		return DataEngineType.fromDockerImageName(dockerImage) == DataEngineType.SPARK_STANDALONE;
	}

	private Optional<ShapeInfo> getDataEngineServiceShape(List<String> shapeNames,
														  Document c) {
		final String desMasterShape = c.getString(MASTER_NODE_SHAPE);
		final String desSlaveShape = c.getString(SLAVE_NODE_SHAPE);
		if (isShapeAcceptable(shapeNames, desMasterShape, desSlaveShape)) {
			return Optional.of(new ShapeInfo(desMasterShape, desSlaveShape, c.getString(TOTAL_INSTANCE_NUMBER),
					UserInstanceStatus.of(c.getString(STATUS))));
		}
		return Optional.empty();
	}

	private Optional<ShapeInfo> getDataEngineShape(List<String> shapeNames, Document c) {
		final String dataEngineShape = c.getString(DATAENGINE_SHAPE);
		if ((isShapeAcceptable(shapeNames, dataEngineShape))
				&& StringUtils.isNotEmpty(c.getString(COMPUTATIONAL_ID))) {

			return Optional.of(new ShapeInfo(dataEngineShape, c.getString(DATAENGINE_INSTANCE_COUNT),
					UserInstanceStatus.of(c.getString(STATUS))));
		}
		return Optional.empty();
	}

	private boolean isShapeAcceptable(List<String> shapeNames, String... shapes) {
		return shapeNames == null || shapeNames.isEmpty() || Arrays.stream(shapes).anyMatch(shapeNames::contains);
	}

	protected abstract void appendSsnAndEdgeNodeType(List<String> shapeNames, Map<String, ShapeInfo> shapes);

	protected String generateShapeName(ShapeInfo shape) {
		return Optional.ofNullable(shape).map(ShapeInfo::getName).orElse(StringUtils.EMPTY);
	}

	protected void usersToLowerCase(List<String> users) {
		if (users != null) {
			users.replaceAll(u -> u != null ? u.toLowerCase() : null);
		}
	}

	protected void setUserFilter(UserInfo userInfo, BillingFilter filter, boolean isFullReport) {
		if (isFullReport) {
			usersToLowerCase(filter.getUser());
		} else {
			filter.setUser(Lists.newArrayList(userInfo.getName().toLowerCase()));
		}
	}

	/**
	 * Store shape info
	 */
	@Getter
	@ToString
	protected class ShapeInfo {
		private static final String DES_NAME_FORMAT = "Master: %s%sSlave:  %d x %s";
		private static final String DE_NAME_FORMAT = "%d x %s";
		private final boolean isDataEngine;
		private final String shape;
		private final String slaveShape;
		private final String slaveCount;
		private final boolean isExploratory;
		private final UserInstanceStatus status;

		private ShapeInfo(boolean isDataEngine, String shape, String slaveShape, String slaveCount, boolean
				isExploratory, UserInstanceStatus status) {
			this.isDataEngine = isDataEngine;
			this.shape = shape;
			this.slaveShape = slaveShape;
			this.slaveCount = slaveCount;
			this.isExploratory = isExploratory;
			this.status = status;
		}

		public ShapeInfo(String shape, UserInstanceStatus status) {
			this(false, shape, null, null, true, status);
		}

		ShapeInfo(String shape, String slaveShape, String slaveCount, UserInstanceStatus status) {
			this(false, shape, slaveShape, slaveCount, false, status);
		}


		ShapeInfo(String shape, String slaveCount, UserInstanceStatus status) {
			this(true, shape, null, slaveCount, false, status);
		}

		public String getName() {
			if (isExploratory) {
				return shape;
			} else {
				return clusterName();
			}
		}

		private String clusterName() {
			try {
				final Integer count = Integer.valueOf(slaveCount);
				return isDataEngine ? String.format(DE_NAME_FORMAT, count, shape) :
						String.format(DES_NAME_FORMAT, shape, System.lineSeparator(), count - 1, slaveShape);
			} catch (NumberFormatException e) {
				log.error("Cannot parse string {} to integer", slaveCount);
				return StringUtils.EMPTY;
			}
		}
	}
}
