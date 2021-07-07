create database if not exists kafka_communication;

use kafka_communication;

create table if not exists kafka_wages (
    name varchar(32) not null,
    surname varchar(32) not null,
    wage double not null,
    eventTime varchar(32) not null,
    id varchar(36) primary key
);