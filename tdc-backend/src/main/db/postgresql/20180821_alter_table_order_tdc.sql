--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

ALTER TABLE order_tdc DROP COLUMN status;
ALTER TABLE order_tdc DROP COLUMN step;
ALTER TABLE order_tdc DROP COLUMN id_posted_order;

ALTER TABLE order_tdc ADD COLUMN id_state integer NOT NULL;
ALTER TABLE order_tdc ADD COLUMN id_shipping_address integer;
ALTER TABLE order_tdc ADD COLUMN id_shipping_info integer;
ALTER TABLE order_tdc ADD COLUMN initial_quote numeric(10,2);
ALTER TABLE order_tdc ADD COLUMN final_quote numeric(10,2);
ALTER TABLE order_tdc ADD COLUMN comment character varying(500);
ALTER TABLE order_tdc ADD COLUMN origin_country character varying(50);
ALTER TABLE order_tdc ADD COLUMN delivery_time timestamp;

COMMENT ON COLUMN order_tdc.id_state IS 'The state id';
COMMENT ON COLUMN order_tdc.id_shipping_address IS 'The shipping address id';
COMMENT ON COLUMN order_tdc.id_shipping_info IS 'The shipping info id';
COMMENT ON COLUMN order_tdc.initial_quote IS 'The initial quote';
COMMENT ON COLUMN order_tdc.final_quote IS 'The final quote';
COMMENT ON COLUMN order_tdc.comment IS 'The comment';
COMMENT ON COLUMN order_tdc.origin_country IS 'The origin country';
COMMENT ON COLUMN order_tdc.delivery_time IS 'The delivery time';
