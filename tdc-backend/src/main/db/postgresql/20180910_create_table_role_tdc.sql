--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

CREATE TABLE role_tdc
(
    id integer NOT NULL,
    name character varying (50) NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
);
COMMENT ON TABLE role_tdc IS 'The role data';
COMMENT ON COLUMN role_tdc.id IS 'The role id';
COMMENT ON COLUMN role_tdc.name IS 'The role name';
COMMENT ON COLUMN role_tdc.created IS 'The date of creation of the register';
COMMENT ON COLUMN role_tdc.updated IS 'The date of update of the register';

CREATE SEQUENCE role_tdc_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE role_tdc_id_seq OWNED BY role_tdc.id;
ALTER TABLE ONLY role_tdc ALTER COLUMN id SET DEFAULT nextval('role_tdc_id_seq'::regclass);
SELECT pg_catalog.setval('role_tdc_id_seq', 1, false);
ALTER TABLE ONLY role_tdc ADD CONSTRAINT role_tdc_pkey PRIMARY KEY (id);

INSERT INTO role_tdc (id, name, created, updated)
VALUES
(1, 'ADMIN', current_timestamp, current_timestamp),
(2, 'CUSTOMER', current_timestamp, current_timestamp),
(3, 'DESIGNER', current_timestamp, current_timestamp);