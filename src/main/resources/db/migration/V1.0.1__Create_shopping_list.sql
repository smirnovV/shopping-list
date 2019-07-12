create table purchase
(
    id  bigint primary key,
    title varchar(50) not null,
    actual boolean not null,
    date timestamp,
    period bigint not null
);

create sequence hibernate_sequence;