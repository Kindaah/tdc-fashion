--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

CREATE TABLE company
(
    id integer NOT NULL,
    name character varying (255) NOT NULL,
    country character varying (255) NOT NULL,
    state character varying (255) NOT NULL,
    city character varying (255) NOT NULL,
    street_address character varying (255) NOT NULL,
    address_additional_info character varying (255),
    postal_code character varying (100),
    website character varying (255),
    trademark_name character varying (255),
    trademark_registration_number character varying (255),
    sales_tax_permit character varying (255),
    dun_number character varying (255),
    ein_number character varying (255),
    nearest_airport character varying (255),
    second_nearest_airport character varying (255),
    line_business character varying (255),
    created timestamp NOT NULL,
    updated timestamp NOT NULL
);

COMMENT ON TABLE company IS 'The company data';
COMMENT ON COLUMN company.id IS 'The company id';
COMMENT ON COLUMN company.name IS 'The company name';
COMMENT ON COLUMN company.country IS 'The company country';
COMMENT ON COLUMN company.state IS 'The company state';
COMMENT ON COLUMN company.city IS 'The company city';
COMMENT ON COLUMN company.street_address IS 'The company street address';
COMMENT ON COLUMN company.address_additional_info IS 'The company address additional information';
COMMENT ON COLUMN company.postal_code IS 'The company postal code';
COMMENT ON COLUMN company.website IS 'The company website';
COMMENT ON COLUMN company.trademark_name IS 'The company trademark name';
COMMENT ON COLUMN company.trademark_registration_number IS 'The company trademark registration number';
COMMENT ON COLUMN company.sales_tax_permit IS 'The company sales tax permit';
COMMENT ON COLUMN company.dun_number IS 'The company dun number';
COMMENT ON COLUMN company.ein_number IS 'The company ein number';
COMMENT ON COLUMN company.nearest_airport IS 'The company nearest airport';
COMMENT ON COLUMN company.second_nearest_airport IS 'The company second nearest airport';
COMMENT ON COLUMN company.line_business IS 'The company line business';
COMMENT ON COLUMN company.created IS 'The date of creation of the register';
COMMENT ON COLUMN company.updated IS 'The date of update of the register';

CREATE SEQUENCE company_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE company_id_seq OWNED BY company.id;
ALTER TABLE ONLY company ALTER COLUMN id SET DEFAULT nextval('company_id_seq'::regclass);
SELECT pg_catalog.setval('company_id_seq', 1, false);
ALTER TABLE ONLY company ADD CONSTRAINT company_pkey PRIMARY KEY (id);