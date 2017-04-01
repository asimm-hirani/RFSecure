drop table if exists users;
drop table if exists people;
create table users (
    id integer primary key autoincrement,
    username text not null,
    'password' text not null
);
create table people (
    id integer primary key autoincrement,
    certificate bigint,
    firstName text not null,
    lastName text not null,
    regTimestamp timestamp,
    image text not null,
    idNum bigint
);
