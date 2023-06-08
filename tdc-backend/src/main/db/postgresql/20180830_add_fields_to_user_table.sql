--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE user_tdc RENAME full_name TO first_name;
ALTER TABLE user_tdc ADD COLUMN last_name character varying(50);
ALTER TABLE user_tdc ADD COLUMN company_email character varying(50) UNIQUE;
ALTER TABLE user_tdc ADD COLUMN title character varying(50);
ALTER TABLE user_tdc ADD COLUMN phone_number character varying(20);
ALTER TABLE user_tdc ADD COLUMN auth_key character varying(100) UNIQUE;

CREATE INDEX company_email_index ON user_tdc (company_email);
CREATE INDEX auth_key_index ON user_tdc (auth_key);

COMMENT ON COLUMN user_tdc.first_name IS 'The user first name';
COMMENT ON COLUMN user_tdc.last_name IS 'The user last name';
COMMENT ON COLUMN user_tdc.company_email IS 'The user company email';
COMMENT ON COLUMN user_tdc.title IS 'The user title';
COMMENT ON COLUMN user_tdc.phone_number IS 'The user phone number';
COMMENT ON COLUMN user_tdc.auth_key IS 'The user authentication key';