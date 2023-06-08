/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

/**
 * @author Daniel Mejia
 * @file userHelper.ts
 * @description Action commons to manage user data
 */

const sessionTokenKey = 'access_token';
const sessionUserAuthKey = 'user_auth_key';
const sessionUserCompanyEmail = 'user_company_email_key';
const sessionUserRole = 'user_role';
const userRoles = {
  'ADMIN': '1',
  'CUSTOMER': '2',
  'DESIGNER': '3'
};

/**
 * Sets the access information in local storage.
 *
 * @param { any } data the access data.
 */
export const setSessionData = (data: any): void => {
  localStorage.setItem(sessionTokenKey, data.accessToken);
};

/**
 * Set user data in local storage.
 *
 * @param { any } user the user data to save in session.
 */
export const setUserData = (user: any): void => {
  localStorage.setItem(sessionUserAuthKey, user.authKey);
  localStorage.setItem(sessionUserCompanyEmail, user.companyEmail);
  localStorage.setItem(sessionUserRole, `${user.role.id}`);

};

/**
 * Get user data from local storage.
 *
 * @returns { any } the user data.
 */
export const getUserData = (): any => ({
  authKey: localStorage.getItem(sessionUserAuthKey),
  companyEmail: localStorage.getItem(sessionUserCompanyEmail),
  role: localStorage.getItem(sessionUserRole)
});

/**
 * Remove all data of localStorage.
 */
export const cleanLocalStorage = (): void => {
  localStorage.clear();
};

/**
 * Validate if user already logged.
 *
 * @returns { boolean } true if session is valid, false otherwise.
 */
export const isSessionValid = (): boolean => {
  const token = localStorage.getItem(sessionTokenKey);
  return token && typeof token !== 'undefined';
};

/**
 * Gets the access token.
 *
 * @returns { string } the access token.
 */
export function getAccessToken(): string {
  return localStorage.getItem(sessionTokenKey);
}

/**
 * check if the session user has the role.
 *
 * @param { string } role the user role to check.
 *
 * @returns true if the user has the role, false otherwhise.
 */
export const sessionUserIs = (role: string): boolean => {
  return localStorage.getItem(sessionUserRole) === userRoles[role];
};
