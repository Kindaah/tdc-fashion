--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE state ADD COLUMN sequence integer;
COMMENT ON COLUMN state.sequence IS 'The sequence of the states';

UPDATE state SET sequence = 1 where id = 1;
UPDATE state SET sequence = 2 where id = 2;
UPDATE state SET sequence = 3 where id = 3;
UPDATE state SET sequence = 4 where id = 4;
UPDATE state SET sequence = 5 where id = 5;
UPDATE state SET sequence = 6 where id = 6;
UPDATE state SET sequence = 7 where id = 7;
UPDATE state SET sequence = 8 where id = 8;
UPDATE state SET sequence = 9 where id = 9;
UPDATE state SET sequence = 10 where id = 10;
UPDATE state SET sequence = 11 where id = 11;
UPDATE state SET sequence = 12 where id = 12;
UPDATE state SET sequence = 13 where id = 13;
UPDATE state SET sequence = 14 where id = 14;
UPDATE state SET sequence = 16 where id = 15;
UPDATE state SET sequence = 15 where id = 16;