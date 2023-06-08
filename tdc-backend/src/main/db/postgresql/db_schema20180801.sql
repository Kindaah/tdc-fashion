/*

 This source code is the confidential, proprietary information of
 Dress Club here in, you may not disclose such Information,
 and may only use it in accordance with the terms of the license
 agreement you entered into with Dress Club.

 2018: Dress Club.
 All Rights Reserved.

*/

CREATE TABLE "user"
(
    id integer NOT NULL,
    full_name character varying(200) NOT NULL,
    id_company integer,
    id_role integer
);
COMMENT ON TABLE "user" IS 'User data';
COMMENT ON COLUMN "user"."id" IS 'The user id';
COMMENT ON COLUMN "user"."full_name" IS 'The user full name';
COMMENT ON COLUMN "user"."id_company" IS 'The user company id in company table';
COMMENT ON COLUMN "user"."id_role" IS 'The user role id in role table';

CREATE SEQUENCE user_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE user_id_seq OWNED BY "user".id;
ALTER TABLE ONLY "user" ALTER COLUMN id SET DEFAULT nextval('user_id_seq'::regclass);
SELECT pg_catalog.setval('user_id_seq', 1, false);
ALTER TABLE ONLY "user" ADD CONSTRAINT user_pkey PRIMARY KEY (id);

CREATE TABLE "order" (
    status character varying(50) NOT NULL,
    reference_po character varying(50) NOT NULL,
    step character varying(50) NOT NULL,
    created date NOT NULL,
    updated date NOT NULL,
    id integer NOT NULL,
    id_posted_order integer,
    id_user integer NOT NULL
);
CREATE SEQUENCE order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE order_id_seq OWNED BY "order".id;
ALTER TABLE ONLY "order" ALTER COLUMN id SET DEFAULT nextval('order_id_seq'::regclass);
SELECT pg_catalog.setval('order_id_seq', 1, false);
ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_pkey PRIMARY KEY (id);
ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_fkey FOREIGN KEY (id_user) REFERENCES "user"(id) ON DELETE RESTRICT;


CREATE TABLE product (
    description character varying(200) NOT NULL,
    sketch_file_urls character varying(500)[] NOT NULL,
    techinal_sheet_file_urls character varying(500)[] NOT NULL,
    id_order integer NOT NULL,
    created date NOT NULL,
    updated date NOT NULL,
    id integer NOT NULL
);
CREATE SEQUENCE product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE product_id_seq OWNED BY product.id;
ALTER TABLE ONLY product ALTER COLUMN id SET DEFAULT nextval('product_id_seq'::regclass);
SELECT pg_catalog.setval('product_id_seq', 1, false);
ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);
ALTER TABLE ONLY product
    ADD CONSTRAINT product_fkey FOREIGN KEY (id_order) REFERENCES "order"(id) ON DELETE RESTRICT;


CREATE TABLE quote (
    quote numeric(8,2) NOT NULL,
    id_order integer NOT NULL,
    id integer NOT NULL
);
CREATE SEQUENCE quote_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE quote_id_seq OWNED BY quote.id;
ALTER TABLE ONLY quote ALTER COLUMN id SET DEFAULT nextval('quote_id_seq'::regclass);
SELECT pg_catalog.setval('quote_id_seq', 1, false);
ALTER TABLE ONLY quote
    ADD CONSTRAINT quote_pkey PRIMARY KEY (id);
ALTER TABLE ONLY quote
    ADD CONSTRAINT quote_fkey FOREIGN KEY (id_order) REFERENCES "order"(id) ON DELETE RESTRICT;


CREATE TABLE features (
    color character varying(500) NOT NULL,
    size character varying(100) NOT NULL,
    id_product integer NOT NULL,
    quantity integer NOT NULL,
    created date NOT NULL,
    updated date NOT NULL,
    id integer NOT NULL
);
CREATE SEQUENCE features_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE features_id_seq OWNED BY features.id;
ALTER TABLE ONLY features ALTER COLUMN id SET DEFAULT nextval('features_id_seq'::regclass);
SELECT pg_catalog.setval('features_id_seq', 1, false);
ALTER TABLE ONLY features
    ADD CONSTRAINT features_pkey PRIMARY KEY (id);
ALTER TABLE ONLY features
    ADD CONSTRAINT features_fkey FOREIGN KEY (id_product) REFERENCES product(id) ON DELETE RESTRICT;
