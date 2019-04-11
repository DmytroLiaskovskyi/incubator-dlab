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

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import {
  ModalModule,
  UploadKeyDialogModule,
  ProgressDialogModule,
  BubbleModule,
  ConfirmationDialogModule
} from '../shared';
import { MaterialModule } from '../shared/material.module';

import { ManagementComponent } from './management.component';
import {
  ManagementGridComponent,
  ConfirmationDialog
} from './management-grid/management-grid.component';
import { ComputationalResourcesModule } from '../resources/computational/computational-resources-list';




import { FormControlsModule } from '../shared/form-controls';
import { BackupDilogComponent } from './backup-dilog/backup-dilog.component';
import {
  ManageEnvironmentComponent,
  ConfirmActionDialogComponent
} from './manage-environment/manage-environment-dilog.component';

import { GroupNameValidationDirective } from './manage-roles-groups/group-name-validarion.directive';
import { DirectivesModule } from '../core/directives';

import { SsnMonitorComponent } from './ssn-monitor/ssn-monitor.component';
import { ManageRolesGroupsComponent, ConfirmDeleteUserAccountDialogComponent } from './manage-roles-groups/manage-roles-groups.component';

export * from './management.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ModalModule,
    UploadKeyDialogModule,
    ProgressDialogModule,
    BubbleModule,
    ConfirmationDialogModule,
    ComputationalResourcesModule,
    FormControlsModule,
    DirectivesModule,
    MaterialModule
  ],
  declarations: [
    ManagementComponent,
    ManagementGridComponent,

    GroupNameValidationDirective,
    BackupDilogComponent,
    ManageEnvironmentComponent,
    ConfirmationDialog,
    ConfirmActionDialogComponent,
    ConfirmDeleteUserAccountDialogComponent,
    SsnMonitorComponent,
    ManageRolesGroupsComponent
  ],
  entryComponents: [ConfirmationDialog, ConfirmActionDialogComponent, ConfirmDeleteUserAccountDialogComponent],
  exports: [ManagementComponent]
})
export class ManagenementModule {}