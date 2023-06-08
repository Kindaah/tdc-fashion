--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE ONLY order_shipping_address DROP CONSTRAINT order_shipping_address_fkey_sa;
ALTER TABLE order_shipping_address DROP COLUMN id_shipping_address;
ALTER SEQUENCE order_shipping_address_id_seq RENAME TO order_user_id_seq;
ALTER TABLE order_shipping_address RENAME TO order_user;