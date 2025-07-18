--liquibase formatted sql
--changeset ARMAL:03 splitStatements:true

CREATE SEQUENCE seq_form_id
    AS BIGINT
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    START WITH 1
    NO CYCLE
    OWNED BY form.id;

CREATE SEQUENCE seq_personal_detail_id
    AS BIGINT
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    START WITH 1
    NO CYCLE
    OWNED BY personal_detail.id;

CREATE SEQUENCE seq_address_id
    AS BIGINT
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    START WITH 1
    NO CYCLE
    OWNED BY address.id;

