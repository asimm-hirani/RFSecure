drop table if exists users;
drop table if exists visitors;
create table users (
    id integer primary key autoincrement,
    username text not null,
    password text not null,
    admin text not null,
    security text not null,
    worker text not null
);
insert into users (id, username, password, admin, security, worker)
values (1, 'test', '12345', '1', '0', '0');
create table visitors (
    id integer primary key autoincrement,
    firstName text not null,
    lastName text not null,
    regTimestamp timestamp,
    image text not null,
    idNum bigint
);
