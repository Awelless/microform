create sequence hibernate_sequence start 1 increment 1;

create table usr (
    id int8 not null,
    email varchar(255) not null,
    password varchar(255) not null,
    first_name varchar(255),
    last_name varchar(255),
    phone_number varchar(15),
    primary key (id)
);

create table field (
    id int8 not null,
    label varchar(255) not null,
    type varchar(31) not null,
    is_required boolean not null,
    is_active boolean not null,
    primary key (id)
);

create table field_option (
    field_id int8 not null,
    option varchar(255) not null
);

create table response (
    id int8 not null,
    primary key (id)
);

create table response_field (
    response_id int8 not null,
    field_id int8 not null,
    value varchar(255)
);

alter table if exists field_option
    add constraint field_option_field_fk
        foreign key (field_id) references field;

alter table if exists response_field
    add constraint  response_field_response_fk
        foreign key (response_id) references response;

alter table if exists response_field
    add constraint  response_field_field_fk
        foreign key (field_id) references field;