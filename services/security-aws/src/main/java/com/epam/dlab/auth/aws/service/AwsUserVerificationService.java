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

package com.epam.dlab.auth.aws.service;

import com.amazonaws.services.identitymanagement.model.AccessKeyMetadata;
import com.amazonaws.services.identitymanagement.model.User;
import com.epam.dlab.auth.UserInfo;
import com.epam.dlab.auth.UserVerificationService;
import com.epam.dlab.auth.aws.dao.AwsUserDAO;
import com.epam.dlab.exceptions.DlabException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class AwsUserVerificationService implements UserVerificationService {

	private final AwsUserDAO awsUserDAO;

	@Inject
	public AwsUserVerificationService(AwsUserDAO awsUserDAO) {
		this.awsUserDAO = awsUserDAO;
	}

	@Override
	public void verify(UserInfo userInfo) {
		verifyAwsUser(userInfo);
		verifyAwsKeys(userInfo);
	}


	private User verifyAwsUser(UserInfo userInfo) {
		try {
			User awsUser = awsUserDAO.getAwsUser(userInfo.getName());
			if (awsUser != null) {
				userInfo.setAwsUser(true);
				return awsUser;
			} else {
				throw new DlabException("Please contact AWS administrator to create corresponding IAM User");
			}
		} catch (RuntimeException e) {
			throw new DlabException("Please contact AWS administrator to create corresponding IAM User", e);
		}
	}

	private List<AccessKeyMetadata> verifyAwsKeys(UserInfo userInfo) {

		userInfo.getKeys().clear();

		try {
			List<AccessKeyMetadata> keys = awsUserDAO.getAwsAccessKeys(userInfo.getName());
			if (keys == null || keys.isEmpty()
					|| keys.stream().noneMatch(k -> "Active".equalsIgnoreCase(k.getStatus()))) {

				throw new DlabException("Cannot get aws access key for user " + userInfo.getName());
			}
			keys.forEach(e -> userInfo.addKey(e.getAccessKeyId(), e.getStatus()));

			return keys;
		} catch (RuntimeException e) {
			throw new DlabException("Please contact AWS administrator to activate your Access Key", e);
		}
	}
}
