--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE product
    DROP COLUMN sketch_file_urls;
ALTER TABLE product
    DROP COLUMN techinal_sheet_file_urls;

ALTER TABLE product
    ADD COLUMN sketch_file_urls character varying(500) NOT NULL;
ALTER TABLE product
    ADD COLUMN techinal_sheet_file_urls character varying(500) NOT NULL;
ALTER TABLE product
    ADD COLUMN model_file_urls character varying(500);