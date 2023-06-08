--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

CREATE TABLE state
(
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    created date NOT NULL,
    updated date NOT NULL
);
COMMENT ON TABLE state IS 'State data';
COMMENT ON COLUMN state.id IS 'The state id';
COMMENT ON COLUMN state.name IS 'The state name';
COMMENT ON COLUMN state.created IS 'The date of creation of the register';
COMMENT ON COLUMN state.updated IS 'The date of update of the register';

CREATE SEQUENCE state_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE state_id_seq OWNED BY state.id;
ALTER TABLE ONLY state ALTER COLUMN id SET DEFAULT nextval('state_id_seq'::regclass);
SELECT pg_catalog.setval('state_id_seq', 1, false);
ALTER TABLE ONLY state ADD CONSTRAINT state_pkey PRIMARY KEY (id);

INSERT INTO state (id, name, created, updated)
VALUES
(1, 'Incomplete', current_timestamp, current_timestamp),
(2, 'Pending Initial Quote', current_timestamp, current_timestamp),
(3, 'Pending Initial Quote Approval', current_timestamp, current_timestamp),
(4, 'Pending 3D Model', current_timestamp, current_timestamp),
(5, 'Pending 3D Model Admin Approval', current_timestamp, current_timestamp),
(6, 'Pending Final Quote and Lead Time', current_timestamp, current_timestamp),
(7, 'Pending 3D Model and Final Quote Approval', current_timestamp, current_timestamp),
(8, 'Request Samples', current_timestamp, current_timestamp),
(9, 'Samples in Production', current_timestamp, current_timestamp),
(10, 'Confirm Samples', current_timestamp, current_timestamp),
(11, 'Confirm Order Production', current_timestamp, current_timestamp),
(12, 'Order in Production', current_timestamp, current_timestamp),
(13, 'Pending Order Last Payment', current_timestamp, current_timestamp),
(14, 'Order Complete', current_timestamp, current_timestamp),
(15, 'Order Rejected', current_timestamp, current_timestamp);