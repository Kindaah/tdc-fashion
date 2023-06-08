--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE shipping_address DROP COLUMN phone_number;
ALTER TABLE shipping_address DROP COLUMN country_code;
ALTER TABLE shipping_address ADD COLUMN phone_number character varying(20);
ALTER TABLE shipping_address ADD COLUMN country_code character varying(10);
COMMENT ON COLUMN shipping_address.country_code IS 'The shipping address country code';
COMMENT ON COLUMN shipping_address.phone_number IS 'The shipping address phone number';