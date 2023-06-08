--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

DROP TABLE order_user CASCADE;
DROP TABLE order_user_shipping_address CASCADE;

ALTER TABLE shipping_address ADD COLUMN id_user INTEGER;
COMMENT ON COLUMN shipping_address.id_user IS 'The creation user id';
ALTER TABLE ONLY shipping_address
    ADD CONSTRAINT shipping_address_user_fkey FOREIGN KEY (id_user) REFERENCES user_tdc(id);

ALTER TABLE order_tdc ADD COLUMN id_user INTEGER;
COMMENT ON COLUMN order_tdc.id_user IS 'The creation user id';
ALTER TABLE ONLY order_tdc
    ADD CONSTRAINT order_tdc_user_fkey FOREIGN KEY (id_user) REFERENCES user_tdc(id);

