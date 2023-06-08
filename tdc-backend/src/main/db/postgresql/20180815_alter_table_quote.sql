--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE quote DROP CONSTRAINT quote_fkey;

ALTER TABLE ONLY quote
    ADD CONSTRAINT quote_fkey FOREIGN KEY (id_order) REFERENCES order_tdc(id) ON DELETE RESTRICT;