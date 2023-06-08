--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE user_tdc DROP COLUMN company_name;
ALTER TABLE user_tdc ADD COLUMN id_company integer;
ALTER TABLE ONLY user_tdc
    ADD CONSTRAINT user_tdc_fkey_company FOREIGN KEY (id_company) REFERENCES company(id);
COMMENT ON COLUMN user_tdc.id_company IS 'The company id reference for user';