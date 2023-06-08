--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

CREATE TABLE order_user_shipping_address
(
    id integer NOT NULL,
    id_shipping_address integer NOT NULL,
    id_order_user integer NOT NULL,
    type character varying (50) NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
);
COMMENT ON TABLE order_user_shipping_address IS 'Table to relate the relation between the order and the user with the shipping address';
COMMENT ON COLUMN order_user_shipping_address.id IS 'The order user shipping address id';
COMMENT ON COLUMN order_user_shipping_address.id_shipping_address IS 'The shipping address id';
COMMENT ON COLUMN order_user_shipping_address.id_order_user IS 'The order user id';
COMMENT ON COLUMN order_user_shipping_address.type IS 'The order user shipping address type';
COMMENT ON COLUMN order_user_shipping_address.created IS 'The date of creation of the register';
COMMENT ON COLUMN order_user_shipping_address.updated IS 'The date of update of the register';

CREATE SEQUENCE order_user_shipping_address_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE order_user_shipping_address_id_seq OWNED BY order_user_shipping_address.id;
ALTER TABLE ONLY order_user_shipping_address ALTER COLUMN id SET DEFAULT nextval('order_user_shipping_address_id_seq'::regclass);
SELECT pg_catalog.setval('order_user_shipping_address_id_seq', 1, false);
ALTER TABLE ONLY order_user_shipping_address ADD CONSTRAINT order_user_shipping_address_pkey PRIMARY KEY (id);
ALTER TABLE ONLY order_user_shipping_address
    ADD CONSTRAINT order_user_shipping_address_fkey_sa FOREIGN KEY (id_shipping_address) REFERENCES shipping_address(id);