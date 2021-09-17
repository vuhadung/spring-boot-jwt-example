drop table if exists MATCHES cascade;
drop table if exists SUBMISSIONS cascade;
drop table if exists COURSES cascade;
drop table if exists LANGUAGES cascade;
drop table if exists USER_ROLE cascade;
drop table if exists ROLES cascade;
drop table if exists USERS cascade;

drop sequence if exists COURSE_SEQ;
drop sequence if exists LANG_SEQ;
drop sequence if exists MATCH_SEQ;
drop sequence if exists ROLE_SEQ;
drop sequence if exists SUBMISSION_SEQ;
drop sequence if exists USER_ROLE_SEQ;
drop sequence if exists USER_SEQ;

create sequence COURSE_SEQ start 1 increment 1;
create sequence LANG_SEQ start 1 increment 1;
create sequence MATCH_SEQ start 1 increment 1;
create sequence ROLE_SEQ start 1 increment 1;
create sequence SUBMISSION_SEQ start 1 increment 1;
create sequence USER_ROLE_SEQ start 1 increment 1;
create sequence USER_SEQ start 1 increment 1;

    create table COURSES (
       ID int8 not null,
        CREATED_DATE timestamp,
        SUBMISSION_DEADLINE timestamp,
        IS_BACKUP boolean,
        COURSE_NAME varchar(255),
        PATH_TO_FILE varchar(255),
        UPDATED_DATE timestamp,
        primary key (ID)
    );

    create table LANGUAGES (
       ID int8 not null,
        NAME varchar(255),
        primary key (ID)
    );

    create table MATCHES (
       ID int8 not null,
        CREATED_DATE timestamp,
        ERROR_MESSAGE varchar(1000),
        PATH_TO_AWAY_MATCH_RESULT varchar(255),
        PATH_TO_HOME_MATCH_RESULT varchar(255),
        RESULT_PUBLISHED boolean,
        UPDATED_DATE timestamp,
        AWAY_MATCH_WINNER_ID int8,
        BACKUP_COURSE_ID int8,
        COURSE_ID int8,
        WINNER_ID int8,
        HOME_MATCH_WINNER_ID int8,
        PARENT_MATCH_0_ID int8,
        PARENT_MATCH_1_ID int8,
        PLAYER_0_ID int8,
        PLAYER_1_ID int8,
        primary key (ID)
    );

    create table ROLES (
       ID int8 not null,
        DESCRIPTION varchar(255),
        NAME varchar(255),
        primary key (ID)
    );

    create table SUBMISSIONS (
       ID int8 not null,
        CREATED_DATE timestamp,
        PATH_TO_FILE varchar(255),
        UPDATED_DATE timestamp,
        LANGUAGE_ID int8,
        USER_ID int8,
        primary key (ID)
    );

    create table USER_ROLE (
       ID int8 not null,
        ROLE_ID int8,
        USER_ID int8,
        primary key (ID)
    );

    create table USERS (
       ID int8 not null,
        ACCESS_TOKEN varchar(255),
        CREATED_DATE timestamp,
        DISPLAY_NAME varchar(255),
        EMAIL varchar(255),
        PASSWORD varchar(255),
        UPDATED_DATE timestamp,
        USER_NAME varchar(255),
        primary key (ID)
    );

    alter table MATCHES 
       add constraint FKr2ohi5jsschs8qxjpqaoayr45 
       foreign key (AWAY_MATCH_WINNER_ID) 
       references USERS;

    alter table MATCHES 
       add constraint FK5je1dme7qc5u7renc6bvelcr9 
       foreign key (BACKUP_COURSE_ID) 
       references COURSES;

    alter table MATCHES 
       add constraint FKfokqqfl172aqbhwqk0lwa7vj5 
       foreign key (COURSE_ID) 
       references COURSES;

    alter table MATCHES 
       add constraint FKrmhd5g0f8fsdkgpr2jxlo41lj 
       foreign key (WINNER_ID) 
       references USERS;

    alter table MATCHES 
       add constraint FK5uoqwrffoun71cvx66mywjabw 
       foreign key (HOME_MATCH_WINNER_ID) 
       references USERS;

    alter table MATCHES 
       add constraint FKkoyphxsas4bjdcdu3pckw773h 
       foreign key (PARENT_MATCH_0_ID) 
       references MATCHES;

    alter table MATCHES 
       add constraint FK3rc49v9xpt5m6c2hbi57s0ajq 
       foreign key (PARENT_MATCH_1_ID) 
       references MATCHES;

    alter table MATCHES 
       add constraint FKnkgt8ncwu6d8ljjby92xak3eu 
       foreign key (PLAYER_0_ID) 
       references USERS;

    alter table MATCHES 
       add constraint FKab75k0dbotra80jq0icrnh89v 
       foreign key (PLAYER_1_ID) 
       references USERS;

    alter table SUBMISSIONS 
       add constraint FKrao7vakvldnp9rvn93q2nlhdg 
       foreign key (LANGUAGE_ID) 
       references LANGUAGES;

    alter table SUBMISSIONS 
       add constraint FKosfcthai5iqw0s2vqp2q7qcyt 
       foreign key (USER_ID) 
       references USERS;

    alter table USER_ROLE 
       add constraint FKcv7h6lq93jnxs6trp7xq1x5sn 
       foreign key (ROLE_ID) 
       references ROLES;

    alter table USER_ROLE 
       add constraint FKr4wtg8onirvrqs1ailjjjsx0y 
       foreign key (USER_ID) 
       references USERS;

-- Initial data 	   
INSERT INTO ROLES (ID, DESCRIPTION, NAME) VALUES (NEXTVAL('ROLE_SEQ'), 'Admin role', 'ADMIN');
INSERT INTO ROLES (ID, DESCRIPTION, NAME) VALUES (NEXTVAL('ROLE_SEQ'), 'User role', 'USER');
INSERT INTO USERS (ID, EMAIL, PASSWORD, USER_NAME, DISPLAY_NAME, ACCESS_TOKEN, CREATED_DATE, UPDATED_DATE) VALUES (NEXTVAL('USER_SEQ'), 'admin@fortna.com', '{JgZMrA8MGJ3E05evWWI5nbjFfk0Ms4C1P4rPFR4k8sM=}64354914afcc6eb99aa180519cb364e5', 'admin', 'Administrator', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO USER_ROLE (ID, ROLE_ID, USER_ID) VALUES(NEXTVAL('USER_ROLE_SEQ'), (SELECT ID FROM ROLES WHERE NAME = 'ADMIN'), (SELECT ID FROM USERS WHERE USER_NAME = 'admin'));
INSERT INTO LANGUAGES (ID, NAME) VALUES (NEXTVAL('LANG_SEQ'), 'C++ 11');
INSERT INTO LANGUAGES (ID, NAME) VALUES (NEXTVAL('LANG_SEQ'), 'JAVA 8');

