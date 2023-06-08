/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Component, OnInit, Input } from '@angular/core';

/**
 * @author Cristian Ricardo
 * @file coments.component.ts
 * @description Coments component
 */

@Component({
  selector: 'app-coments',
  templateUrl: './coments.component.html',
  styleUrls: ['./coments.component.css']
})

export class ComentsComponent implements OnInit {

  @Input('comment') comment: string;

  /**
   * Creates an instance of ComentsComponent.
   */
  constructor() { }

  /**
   * Callback on init component.
   */
  ngOnInit() {
  }

}
