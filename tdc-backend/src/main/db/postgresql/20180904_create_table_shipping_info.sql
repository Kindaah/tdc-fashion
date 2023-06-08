--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

CREATE TABLE shipping_info
(
    id integer NOT NULL,
    company character varying(100) NOT NULL,
    tracking_id character varying (50) NOT NULL,
    details character varying (200) NOT NULL,
    created TIMESTAMP,
    updated TIMESTAMP
);
COMMENT ON TABLE shipping_info IS 'Shipping info data';
COMMENT ON COLUMN shipping_info.id IS 'The shipping info id';
COMMENT ON COLUMN shipping_info.company IS 'The shipping info company';
COMMENT ON COLUMN shipping_info.tracking_id IS 'The shipping info tacking id';
COMMENT ON COLUMN shipping_info.details IS 'The shipping info details';
COMMENT ON COLUMN shipping_info.created IS 'The date of creation of the register';
COMMENT ON COLUMN shipping_info.updated IS 'The date of update of the register';

CREATE SEQUENCE shipping_info_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE shipping_info_id_seq OWNED BY shipping_info.id;
ALTER TABLE ONLY shipping_info ALTER COLUMN id SET DEFAULT nextval('shipping_info_id_seq'::regclass);
SELECT pg_catalog.setval('shipping_info_id_seq', 1, false);
ALTER TABLE ONLY shipping_info ADD CONSTRAINT shipping_info_pkey PRIMARY KEY (id);