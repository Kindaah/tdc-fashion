--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

CREATE TABLE payment
(
    id integer NOT NULL,
    id_order integer NOT NULL,
    status character varying (50) NOT NULL,
    status_description character varying (100) NOT NULL,
    id_payment_type integer NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
);
COMMENT ON TABLE payment IS 'The payment data';
COMMENT ON COLUMN payment.id IS 'The payment id';
COMMENT ON COLUMN payment.id_order IS 'The order id associated with the payment';
COMMENT ON COLUMN payment.status IS 'The status of the payment';
COMMENT ON COLUMN payment.status_description IS 'The status description of the payment';
COMMENT ON COLUMN payment.id_payment_type IS 'The payment type id associated with the payment';
COMMENT ON COLUMN payment.created IS 'The date of creation of the register';
COMMENT ON COLUMN payment.updated IS 'The date of update of the register';

CREATE SEQUENCE payment_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE payment_id_seq OWNED BY payment.id;
ALTER TABLE ONLY payment ALTER COLUMN id SET DEFAULT nextval('payment_id_seq'::regclass);
SELECT pg_catalog.setval('payment_id_seq', 1, false);
ALTER TABLE ONLY payment ADD CONSTRAINT payment_pkey PRIMARY KEY (id);
ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_fkey FOREIGN KEY (id_payment_type) REFERENCES payment_type(id);
ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_order_fkey FOREIGN KEY (id_order) REFERENCES order_tdc(id);