--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE ONLY order_shipping_address
    ADD CONSTRAINT order_shipping_address_fkey_ot FOREIGN KEY (id_order) REFERENCES order_tdc(id);
ALTER TABLE ONLY order_shipping_address
    ADD CONSTRAINT order_shipping_address_fkey_ut FOREIGN KEY (id_user) REFERENCES user_tdc(id);
