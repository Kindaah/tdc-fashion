/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ColorPickerModule } from 'ngx-color-picker';
import { VirtualScrollModule } from 'angular2-virtual-scroll';
import { NgxSpinnerModule } from 'ngx-spinner';
import { LoginGuard } from './guards/login.guard';
import { ErrorInterceptor } from './helpers/errorInterceptor';
import { NgxStripeModule } from 'ngx-stripe';
import { AppRoutingModule } from './/app-routing.module';
import { JwtModule } from '@auth0/angular-jwt';
import { FileDropModule } from 'ngx-file-drop';
import { SnotifyModule, SnotifyService, ToastDefaults } from 'ng-snotify';
import { RecaptchaModule, RECAPTCHA_SETTINGS, RecaptchaSettings, RECAPTCHA_LANGUAGE } from 'ng-recaptcha';
import { RecaptchaFormsModule } from 'ng-recaptcha/forms';
import { MatButtonModule, MatIconModule } from '@angular/material';

// Environment
import { environment } from '../environments/environment';

// Helpers
import * as UserHelper from './helpers/userHelper';

// Components
import { AppComponent } from './app.component';
import { MenuComponent } from './components/menu/menu.component';
import { CardComponent } from './components/card/card.component';
import { ProductComponent } from './components/product/product.component';
import { LayoutComponent } from './components/layout/layout.component';
import { SamplesComponent } from './components/samples/samples.component';
import { ComentsComponent } from './components/coments/coments.component';
import { AddressInfoComponent } from './components/address-info/address-info.component';
import { AddressFormModalComponent } from './components/address-form-modal/address-form-modal.component';
import { CurrencyMaskModule } from 'ng2-currency-mask';
import { ShippingInfoModalComponent } from './components/shipping-info-modal/shipping-info-modal.component';
import { ShippingInfoComponent } from './components/shipping-info/shipping-info.component';
import { PaymentInfoComponent } from './components/payment-info/payment-info.component';
import { TechnicalSheetFormComponent } from './components/technical-sheet-form/technical-sheet-form.component';
import { AddressFormComponent } from './components/address-form/address-form.component';

// Views
import { LoginComponent } from './views/login/login.component';
import { RegisterComponent } from './views/register/register.component';
import { DashboardComponent } from './views/dashboard/dashboard.component';
import { OrdersComponent } from './views/orders/orders.component';
import { OrderComponent } from './views/order/order.component';
import { ProfileComponent } from './views/profile/profile.component';
import { AddressesComponent } from './views/addresses/addresses.component';
import { CompanyInfoComponent } from './views/company-info/company-info.component';
import { UsersComponent } from './views/users/users.component';
import { TeamComponent } from './views/team/team.component';

/**
 * @file app.module.ts
 * @description App module
 */
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    DashboardComponent,
    OrdersComponent,
    OrderComponent,
    ProfileComponent,
    AddressesComponent,
    LayoutComponent,
    ProductComponent,
    MenuComponent,
    CardComponent,
    SamplesComponent,
    ComentsComponent,
    AddressInfoComponent,
    AddressFormModalComponent,
    AddressInfoComponent,
    ShippingInfoModalComponent,
    ShippingInfoComponent,
    PaymentInfoComponent,
    CompanyInfoComponent,
    UsersComponent,
    TechnicalSheetFormComponent,
    TeamComponent,
    AddressFormComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    VirtualScrollModule,
    NgxSpinnerModule,
    ColorPickerModule,
    CurrencyMaskModule,
    NgxStripeModule.forRoot(environment.payment.stripeKey),
    JwtModule.forRoot({
      config: {
        tokenGetter: UserHelper.getAccessToken,
        whitelistedDomains: [environment.apiDomain]
      }
    }),
    FileDropModule,
    SnotifyModule,
    RecaptchaModule,
    RecaptchaFormsModule,
    MatButtonModule,
    MatIconModule
  ],
  providers: [
    LoginGuard,
    LoginComponent,
    LayoutComponent,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    },
    {
      provide: 'SnotifyToastConfig',
      useValue: ToastDefaults
    },
    SnotifyService,
    {
      provide: RECAPTCHA_LANGUAGE,
      useValue: 'en',
    },
    {
      provide: RECAPTCHA_SETTINGS,
      useValue: {
        siteKey: environment.recaptchaKey,
      } as RecaptchaSettings
    }
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    ProductComponent,
    AddressFormModalComponent,
    ShippingInfoModalComponent
  ]
})

export class AppModule { }
