--liquibase formatted sql
--changeset ARMAL:01 splitStatements:true

create table form (
    id int primary key,
    fk_personal_detail_id int unique ,
    fk_address_id int unique,
    session_id varchar(200) not null ,
    finalized boolean not null default false
);

CREATE TABLE personal_detail (
    id int primary key,
    fk_form_id  int unique not null,
    first_name varchar(50),
    last_name varchar(50),
    email varchar(50)
);

CREATE TABLE address (
    id  int primary key,
    fk_form_id int unique not null,
    house_number int not null,
    street varchar(50) not null,
    city  varchar(50),
    post_code varchar(50) not null
);



