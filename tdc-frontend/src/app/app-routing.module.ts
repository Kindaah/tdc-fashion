/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginGuard } from './guards/login.guard';
import { DashboardComponent } from './views/dashboard/dashboard.component';
import { OrderComponent } from './views/order/order.component';
import { LoginComponent } from './views/login/login.component';
import { RegisterComponent } from './views/register/register.component';
import { OrdersComponent } from './views/orders/orders.component';
import { ProfileComponent } from './views/profile/profile.component';
import { AddressesComponent } from './views/addresses/addresses.component';
import { UsersComponent } from './views/users/users.component';
import { CompanyInfoComponent } from './views/company-info/company-info.component';
import { TeamComponent } from './views/team/team.component';

/**
 * @file app.routing.module.ts
 * @description App router
 */
const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [LoginGuard]
  },
  {
    path: 'orders',
    canActivate: [LoginGuard],
    children: [
      {
        path: '',
        component: OrdersComponent
      },
      {
          path: 'new',
          component: OrderComponent
      },
      {
          path: ':id',
          component: OrderComponent
      }
    ]
  },
  {
    path: 'users',
    canActivate: [LoginGuard],
    children: [
      {
        path: '',
        component: UsersComponent
      },
      {
          path: ':id/company',
          component: CompanyInfoComponent
      }
    ]
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [LoginGuard]
  },
  {
    path: 'addresses',
    component: AddressesComponent,
    canActivate: [LoginGuard]
  },
  {
    path: 'team',
    component: TeamComponent,
    canActivate: [LoginGuard]
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ],
  providers: [ LoginGuard ]
})

export class AppRoutingModule {}
