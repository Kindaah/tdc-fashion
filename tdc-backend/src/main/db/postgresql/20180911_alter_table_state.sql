--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE state ADD COLUMN filter character varying(50);
COMMENT ON COLUMN state.filter IS 'The filter of the state';

UPDATE state SET filter = 'ALL' where id = 1;
UPDATE state SET filter = 'ADMIN' where id = 2;
UPDATE state SET filter = 'ALL' where id = 3;
UPDATE state SET filter = 'DESIGNER' where id = 4;
UPDATE state SET filter = 'ADMIN' where id = 5;
UPDATE state SET filter = 'ADMIN' where id = 6;
UPDATE state SET filter = 'ALL' where id = 7;
UPDATE state SET filter = 'ALL' where id = 8;
UPDATE state SET filter = 'DESIGNER' where id = 9;
UPDATE state SET filter = 'ALL' where id = 10;
UPDATE state SET filter = 'ALL' where id = 11;
UPDATE state SET filter = 'ADMIN' where id = 12;
UPDATE state SET filter = 'ADMIN' where id = 13;
UPDATE state SET filter = 'ADMIN' where id = 14;
UPDATE state SET filter = 'ALL' where id = 15;