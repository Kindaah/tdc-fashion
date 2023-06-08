--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

DROP TABLE features;

CREATE TABLE feature
(
    id integer NOT NULL,
    feature_size varchar(20) NOT NULL,
    color varchar NOT NULL,
    quantity integer NOT NULL,
    sample_quantity integer DEFAULT NULL,
    id_product integer NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
);

CREATE SEQUENCE feature_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE feature_id_seq OWNED BY feature.id;
ALTER TABLE ONLY feature ALTER COLUMN id SET DEFAULT nextval('feature_id_seq'::regclass);
SELECT pg_catalog.setval('feature_id_seq', 1, false);
ALTER TABLE ONLY feature
    ADD CONSTRAINT feature_primary_key PRIMARY KEY (id);
ALTER TABLE ONLY feature
    ADD CONSTRAINT feature_key FOREIGN KEY (id_product) REFERENCES product(id) ON DELETE RESTRICT;

COMMENT ON TABLE feature IS 'Feature data for a product';
COMMENT ON COLUMN feature.id IS 'The feature id';
COMMENT ON COLUMN feature.feature_size IS 'The feature size';
COMMENT ON COLUMN feature.color IS 'The feature color';
COMMENT ON COLUMN feature.quantity IS 'The feature quantity';
COMMENT ON COLUMN feature.sample_quantity IS 'The feature sample quantity';
COMMENT ON COLUMN feature.id_product IS 'The feature id product relationship';
COMMENT ON COLUMN feature.created IS 'The feature created date';
COMMENT ON COLUMN feature.updated IS 'The feature updated date';
