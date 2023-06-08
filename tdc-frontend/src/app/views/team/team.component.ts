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
import { UserService } from 'src/app/services/user.service';
import { LayoutComponent } from 'src/app/components/layout/layout.component';
import { User } from 'src/app/models/user';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { FormValidatorHelper } from 'src/app/helpers/form/formValidatorHelper';
import { AdminService } from 'src/app/services/admin.service';
import { NotificationUtil } from 'src/app/utilities/notificationUtil';

/**
 * @author Daniel Mejia
 * @file team.component.ts
 * @description Team view component.
 */

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.css']
})
export class TeamComponent implements OnInit {

  /**
   * The team list.
   *
   * @type { User[] }
   */
  team: User[];

  /**
   * The team types.
   *
   * @type { { [key: string]: string[] } }
   */
  teamTypes: { [key: string]: string[] } = {
    'All': ['ADMIN', 'DESIGNER'],
    'Administrators': ['ADMIN'],
    'Designers': ['DESIGNER']
  };

  /**
   * The active type.
   *
   * @type { String }
   */
  activeType = 'All';

  /**
   * The register form group.
   *
   *  @type { FormGroup }
   */
  registerForm: FormGroup;

  /**
   * The form validator helpero to validates the FormGroup.
   *
   * @type { FormValidatorHelper<FormGroup> }
   */
  formValidatorHelper: FormValidatorHelper<FormGroup>;

  /**
   * Creates an instance of TeamComponent.
   *
   * @param { UserService } userService The user service.
   * @param { LayoutComponent } layout The layout component.
   * @param { FormBuilder } formBuilder The form builder.
   * @param { NgbModal } modalService The modal service.
   * @param { NotificationUtil } notificationUtil The notification util.
   * @param { AdminService } adminService The admin service.
   */
  constructor(
    private userService: UserService,
    private layout: LayoutComponent,
    private formBuilder: FormBuilder,
    private modalService: NgbModal,
    private notificationUtil: NotificationUtil,
    private adminService: AdminService
  ) {
    this.registerForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      companyEmail: ['', [Validators.required, Validators.email]],
      roleOption: [this.teamTypes['Administrators'][0]]
    });

    this.formValidatorHelper = new FormValidatorHelper(this.registerForm);
  }

  /**
   * Callback on init component.
   */
  ngOnInit(): void {
    this.getTeamMembers();
  }

  /**
   * Gets team members.
   */
  getTeamMembers(): void {
    this.layout.spinner.show();
    this.userService.getAllUsers(this.teamTypes['All'])
      .subscribe(this.setTeam, () => this.layout.spinner.hide());
  }

  /**
   * Sets the team.
   *
   * @param { User[] } team The team.
   */
  setTeam = (team: User[]): void => {
    this.team = team;
    this.layout.spinner.hide();
  }

  /**
   * Open a modal service to add the team member.
   *
   * @param { any } content The modal content.
   */
  openAddMemberModal(content: any): void {
    this.modalService.open(content);
  }

  /**
   * Adds a team member.
   */
  addTeamMember(): void {
    const teamMeber = this.registerForm.value;
    const { roleOption } = teamMeber;
    const onTeamMemberAdded = (user: User) => {
      this.team.push(user);
      this.layout.spinner.hide();
      this.notificationUtil.onSuccess('Team member added sucessfully!');
      this.registerForm.reset();
    };

    this.layout.spinner.show();
    this.adminService.addUserTeam(teamMeber, roleOption)
      .subscribe(onTeamMemberAdded, () => this.layout.spinner.hide());
  }

  /**
   * Check is the user is admin.
   *
   * @param { User } user The user to check.
   *
   * @returns { boolean } True if the user is admin, false otherwise.
   */
  isAdmin(user: User): boolean {
    return user.role.name === 'ADMIN';
  }

  /**
   * Getter for the team types list.
   *
   * @returns { string[] } The team types list.
   */
  get teamTypesList(): string[] {
    return Object.keys(this.teamTypes);
  }

  /**
   * Getter for the filtered team.
   *
   * @returns { User[] } The filtered team.
   */
  get filterTeam(): User[] {
    return this.team ? this.team.filter(user => this.teamTypes[this.activeType].includes(user.role.name)) : [];
  }
}
