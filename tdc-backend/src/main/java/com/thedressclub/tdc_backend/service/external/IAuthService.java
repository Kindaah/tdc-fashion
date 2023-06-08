/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service.external;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;

/**
 * Interface for the Auth0 Service.
 *
 * @author Edson Ruiz Ramirez.
 */
public interface IAuthService
{
    /**
     * Add a new user in auth0
     *
     * @param memberEmail The user mail.
     *
     * @return The auth key from the entity on auth0.
     */
    String addUser(String memberEmail);

    /**
     * Gets the bank token using plaid system.
     *
     * @param authKey the authKey of the user.
     * @param newPassword the new password for the user.
     *
     * @return True if the password was changed.
     */
    boolean changePassword(String authKey, String newPassword);

    /**
     * Sets new authAPI.
     *
     * @param authAPI New value of authAPI.
     */
    void setAuthAPI(AuthAPI authAPI);

    /**
     * Sets new managementAPI.
     *
     * @param managementAPI New value of managementAPI.
     */
    void setManagementAPI(ManagementAPI managementAPI);
}
