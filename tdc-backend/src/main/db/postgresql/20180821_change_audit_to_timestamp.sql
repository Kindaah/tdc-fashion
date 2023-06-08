--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE user_tdc DROP COLUMN created;
ALTER TABLE user_tdc DROP COLUMN updated;
ALTER TABLE order_tdc DROP COLUMN created;
ALTER TABLE order_tdc DROP COLUMN updated;
ALTER TABLE product DROP COLUMN created;
ALTER TABLE product DROP COLUMN updated;

ALTER TABLE user_tdc ADD COLUMN created TIMESTAMP;
ALTER TABLE user_tdc ADD COLUMN updated TIMESTAMP;
ALTER TABLE order_tdc ADD COLUMN created TIMESTAMP;
ALTER TABLE order_tdc ADD COLUMN updated TIMESTAMP;
ALTER TABLE product ADD COLUMN created TIMESTAMP;
ALTER TABLE product ADD COLUMN updated TIMESTAMP;