/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';

/**
 * @author Daniel Mejia
 * @file layout.component.ts
 * @description Layout component
 */

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})

export class LayoutComponent implements OnInit {

  /**
   * Creates an instance of LayoutComponent.
   *
   * @param spinner Spinner propertie of the LayoutComponent
   */
  constructor(public spinner: NgxSpinnerService) { }

  /**
   * Callback on init component.
   */
  ngOnInit() {
  }

}
