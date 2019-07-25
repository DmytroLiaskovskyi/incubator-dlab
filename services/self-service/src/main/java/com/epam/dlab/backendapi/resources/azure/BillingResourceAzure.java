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

package com.epam.dlab.backendapi.resources.azure;

import com.epam.dlab.auth.UserInfo;
import com.epam.dlab.backendapi.resources.dto.azure.AzureBillingFilter;
import com.epam.dlab.backendapi.service.BillingService;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import org.bson.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Provides API to get or export billing information.
 */
@Path("/billing")
@Consumes(MediaType.APPLICATION_JSON)
public class BillingResourceAzure {

    @Inject
    private BillingService billingService;

    /**
     * Returns the billing report.
     *
     * @param userInfo user info.
     * @param filter   filter for billing data.
     */
    @POST
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Document getBillingReport(@Auth UserInfo userInfo, @Valid @NotNull AzureBillingFilter filter) {
        return billingService.getBillingReport(userInfo, filter);
    }

    /**
     * Returns the billing report in csv file.
     *
     * @param userInfo user info.
     * @param filter   filter for report data.
     */

    @POST
    @Path("/report/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @SuppressWarnings("unchecked")
    public Response downloadBillingReport(@Auth UserInfo userInfo, @Valid @NotNull AzureBillingFilter filter) {
        return Response.ok(billingService.downloadReport(userInfo, filter))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + billingService.getReportFileName(userInfo, filter) + "\"")
                .build();
    }
}