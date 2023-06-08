--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE user_tdc DROP COLUMN title;
ALTER TABLE user_tdc DROP COLUMN id_company;
ALTER TABLE user_tdc ADD COLUMN company_name character varying(100);
