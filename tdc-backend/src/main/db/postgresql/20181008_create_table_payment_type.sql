--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

CREATE TABLE payment_type
(
    id integer NOT NULL,
    name character varying (50) NOT NULL,
    description character varying (100) NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
);
COMMENT ON TABLE payment_type IS 'The payment type data';
COMMENT ON COLUMN payment_type.id IS 'The payment type id';
COMMENT ON COLUMN payment_type.name IS 'The payment type name';
COMMENT ON COLUMN payment_type.description IS 'The payment type description';
COMMENT ON COLUMN payment_type.created IS 'The date of creation of the register';
COMMENT ON COLUMN payment_type.updated IS 'The date of update of the register';

CREATE SEQUENCE payment_type_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE payment_type_id_seq OWNED BY payment_type.id;
ALTER TABLE ONLY payment_type ALTER COLUMN id SET DEFAULT nextval('payment_type_id_seq'::regclass);
SELECT pg_catalog.setval('payment_type_id_seq', 1, false);
ALTER TABLE ONLY payment_type ADD CONSTRAINT payment_type_pkey PRIMARY KEY (id);

INSERT INTO payment_type (id, name, description, created, updated)
VALUES
(1, 'samples', 'Payment for samples', current_timestamp, current_timestamp),
(2, 'first_payment', 'First payment for the order', current_timestamp, current_timestamp),
(3, 'last_payment', 'Last payment for the order', current_timestamp, current_timestamp);