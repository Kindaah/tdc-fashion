--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

CREATE TABLE shipping_address
(
    id integer NOT NULL,
    contact_name character varying(100),
    country character varying(50),
    street_address character varying(100),
    street_address_add_info character varying(100),
    state character varying(50),
    city character varying(50),
    postal_code character varying(20),
    country_code integer,
    phone_number integer,
    created TIMESTAMP,
    updated TIMESTAMP
);
COMMENT ON TABLE shipping_address IS 'Shipping address data';
COMMENT ON COLUMN shipping_address.id IS 'The shipping address id';
COMMENT ON COLUMN shipping_address.contact_name IS 'The contact name';
COMMENT ON COLUMN shipping_address.country IS 'The country';
COMMENT ON COLUMN shipping_address.street_address IS 'The street address';
COMMENT ON COLUMN shipping_address.street_address_add_info IS 'The street address additional info';
COMMENT ON COLUMN shipping_address.state IS 'The state';
COMMENT ON COLUMN shipping_address.city IS 'The city';
COMMENT ON COLUMN shipping_address.postal_code IS 'The postal code';
COMMENT ON COLUMN shipping_address.country_code IS 'The country code';
COMMENT ON COLUMN shipping_address.phone_number IS 'The phone number';
COMMENT ON COLUMN shipping_address.created IS 'The date of creation of the register';
COMMENT ON COLUMN shipping_address.updated IS 'The date of update of the register';

CREATE SEQUENCE shipping_address_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE shipping_address_id_seq OWNED BY shipping_address.id;
ALTER TABLE ONLY shipping_address ALTER COLUMN id SET DEFAULT nextval('shipping_address_id_seq'::regclass);
SELECT pg_catalog.setval('shipping_address_id_seq', 1, false);
ALTER TABLE ONLY shipping_address ADD CONSTRAINT shipping_address_pkey PRIMARY KEY (id);