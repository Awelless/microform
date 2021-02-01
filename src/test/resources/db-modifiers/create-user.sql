create extension if not exists pgcrypto;

insert into usr(id, email, password, first_name, last_name, phone_number) values
    (1, 't@gmail.com', '123456', 'Igor', 'Shmeltsov', '375296667755');

update usr set password = crypt(password, gen_salt('bf', 12));
