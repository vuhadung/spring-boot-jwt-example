drop table if exists user_role;
drop table if exists roles;
drop table if exists users;
drop sequence if exists role_seq;
drop sequence if exists user_role_seq;
drop sequence if exists user_seq;

    create table roles (
        id int8 not null,
        description varchar(255),
        name varchar(255),
        primary key (id)
    );

    create table user_role (
       id int8 not null,
        role_id int8,
        user_id int8,
        primary key (id)
    );

    create table users (
       id int8 not null,
        email varchar(255),
        password varchar(255),
        username varchar(255),
		access_token varchar(255),
        primary key (id)
    );

    alter table user_role 
       add constraint FK_role 
       foreign key (role_id) 
       references roles;

    alter table user_role 
       add constraint FK_user 
       foreign key (user_id) 
       references users;

create sequence role_seq start 1 increment 1;
create sequence user_role_seq start 1 increment 1;
create sequence user_seq start 1 increment 1;

ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('user_seq');
ALTER TABLE roles ALTER COLUMN id SET DEFAULT nextval('role_seq');
ALTER TABLE user_role ALTER COLUMN id SET DEFAULT nextval('user_role_seq');
	   
INSERT INTO roles (description, name) VALUES ('Admin role', 'ADMIN');
INSERT INTO roles (description, name) VALUES ('User role', 'USER');

INSERT INTO users (email, password, username, access_token) VALUES ('', '{JgZMrA8MGJ3E05evWWI5nbjFfk0Ms4C1P4rPFR4k8sM=}64354914afcc6eb99aa180519cb364e5', 'admin', '');

INSERT INTO user_role (role_id, user_id) VALUES((SELECT id from roles where name = 'ADMIN'), (SELECT id from users where username = 'admin'));


