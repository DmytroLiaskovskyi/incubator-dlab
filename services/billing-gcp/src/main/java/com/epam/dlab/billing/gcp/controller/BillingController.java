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

package com.epam.dlab.billing.gcp.controller;

import com.epam.dlab.billing.gcp.dao.BillingDAO;
import com.epam.dlab.dto.billing.BillingData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BillingController {

    private final BillingDAO billingDAO;

    public BillingController(BillingDAO billingDAO) {
        this.billingDAO = billingDAO;
    }

    @GetMapping
    public ResponseEntity<List<BillingData>> getBilling(@RequestParam List<String> dlabIds) {
        return new ResponseEntity<>(billingDAO.getBillingReport(dlabIds), HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<List<BillingData>> getBilling(@RequestParam("date-start") String dateStart,
                                                        @RequestParam("date-end") String dateEnd,
                                                        @RequestParam("dlab-id") String dlabId,
                                                        @RequestParam("product") List<String> products) {
        return new ResponseEntity<>(billingDAO.getBillingReport(dateStart, dateEnd, dlabId, products), HttpStatus.OK);
    }
}
