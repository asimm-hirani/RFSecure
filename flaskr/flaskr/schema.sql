drop table if exists users;
drop table if exists visitors;
drop table if exists logs;
drop table if exists levels;
create table users (
    id integer primary key autoincrement,
    username text not null,
    password text not null,
    admin text not null,
    security text not null,
    worker text not null
);
insert into users (id, username, password, admin, security, worker)
values (0, 'admin', '$2b$12$C38/X/W9YOreaFM/LtMpfO9En4swA0Q/tmjgZHpofWimcs.cQ4E0S', '1', '0', '0');
insert into users (id, username, password, admin, security, worker)
values (1, 'security', '$2b$12$C38/X/W9YOreaFM/LtMpfO9En4swA0Q/tmjgZHpofWimcs.cQ4E0S', '0', '1', '0');
insert into users (id, username, password, admin, security, worker)
values (2, 'worker', '$2b$12$C38/X/W9YOreaFM/LtMpfO9En4swA0Q/tmjgZHpofWimcs.cQ4E0S', '0', '0', '1');
create table visitors (
    id integer primary key autoincrement,
    firstName text not null,
    lastName text not null,
    regTimestamp text not null,
    image text not null,
    idNum text not null,
    access text not null
);
insert into visitors (id, firstName, lastName, regTimestamp, image, idNum, access)
values (0, 'Bob', 'Builder', '2014-10-18 21:31:12', 'img.jpeg', '903567890', 'employee');
create table logs (
    id integer primary key autoincrement,
    location text not null,
    regTimestamp text not null,
    idNum text not null
);
create table levels (
    id integer primary key autoincrement,
    idNum text not null,
    access text[]
);
