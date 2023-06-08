--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE "order" DROP CONSTRAINT order_fkey;

ALTER TABLE "order" RENAME TO order_tdc;

ALTER TABLE ONLY order_tdc
    ADD CONSTRAINT order_fkey FOREIGN KEY (id_user) REFERENCES user_tdc(id) ON DELETE RESTRICT;