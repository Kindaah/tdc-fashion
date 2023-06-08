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
import * as moment from 'moment';

// Models
import { CountDTO } from '../../dto/countDTO';

/**
 * @author Cristian Ricardo
 * @file card.component.css
 * @description Card styles
 */

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {

  /**
   * Object countDTO
   *
   * @type { CountDTO }
   */
  @Input() countDTO: CountDTO;

  /**
   * The formatted date for the view.
   *
   * @type { any }
   */
  formattedDate: any;

  /**
   * Creates an instance of CardComponent.
   */
  constructor() { }

  /**
   * Callback on init component.
   */
  ngOnInit() {
    this.formattedDate = moment.utc(this.countDTO.entity.updatedAt)
      .startOf('minute')
      .fromNow();
  }

}
