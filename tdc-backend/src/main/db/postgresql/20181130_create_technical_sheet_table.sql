--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

CREATE TABLE technical_sheet_row
(
    id INTEGER NOT NULL,
    id_product INTEGER NOT NULL,
    description CHARACTER VARYING (200) NOT NULL,
    tol DECIMAL NOT NULL,
    twelve CHARACTER VARYING (255) NOT NULL,
    sizes TEXT NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
);

COMMENT ON TABLE technical_sheet_row IS 'The technical sheet row form of the product';
COMMENT ON COLUMN technical_sheet_row.id IS 'The technical sheet row form id';
COMMENT ON COLUMN technical_sheet_row.id_product IS 'The product id associated with the technical sheet row';
COMMENT ON COLUMN technical_sheet_row.description IS 'The technical sheet row description column';
COMMENT ON COLUMN technical_sheet_row.twelve IS 'The technical sheet row twelve column';
COMMENT ON COLUMN technical_sheet_row.sizes IS 'The technical sheet row sizes columns';
COMMENT ON COLUMN technical_sheet_row.created IS 'The date of creation of the register';
COMMENT ON COLUMN technical_sheet_row.updated IS 'The date of update of the register';

CREATE SEQUENCE technical_sheet_row_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE technical_sheet_row_id_seq OWNED BY technical_sheet_row.id;
ALTER TABLE ONLY technical_sheet_row ALTER COLUMN id SET DEFAULT nextval('technical_sheet_row_id_seq'::regclass);
SELECT pg_catalog.setval('technical_sheet_row_id_seq', 1, false);
ALTER TABLE ONLY technical_sheet_row ADD CONSTRAINT technical_sheet_pkey PRIMARY KEY (id);
ALTER TABLE ONLY technical_sheet_row
    ADD CONSTRAINT technical_product_fkey FOREIGN KEY (id_product) REFERENCES product(id);