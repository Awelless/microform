create extension if not exists pgcrypto;

insert into usr (id, email, password, first_name, last_name, phone_number)
    values (1, 't@gmail.com', '123456', 'Igor', 'Shmeltsov', '375294440022');

update usr set password = crypt(password, gen_salt('bf', 12));

insert into field (id, label, type, is_required, is_active)
    values (2, 'Full Name',            'SINGLE_LINE_TEXT', true,  true),
           (3, 'Date of birth',        'DATE'            , true,  true),
           (4, 'Sex',                  'RADIO_BUTTON'    , false, true),
           (5, 'Programming language', 'COMBOBOX'        , true,  true),
           (6, 'Freelance',            'CHECKBOX'        , true,  true),
           (7, 'Salary',               'COMBOBOX'        , true,  true);

insert into field_option (field_id, option)
values (4, 'Male'),
       (4, 'Female');

insert into field_option (field_id, option)
    values (5, 'Java'),
           (5, 'C/C++'),
           (5, 'Python'),
           (5, 'Ruby'),
           (5, 'JavaScript'),
           (5, 'PHP'),
           (5, 'other / not a dev');

insert into field_option (field_id, option)
    values (7, '<= 500$'),
           (7, '501 - 1000$'),
           (7, '1001 - 2000$'),
           (7, '2001 - 4000$'),
           (7, '> 4000$');

insert into response (id)
    values (8), (9);

insert into response_field (response_id, field_id, value)
    values (8, 2, 'Igor Shmeltsov'),
           (8, 3, '2002-12-17'),
           (8, 4, 'Male'),
           (8, 5, 'Java'),
           (8, 6, 'false'),
           (8, 7, '<= 500$');

insert into response_field (response_id, field_id, value)
    values (9, 2, 'Gleb Shilo'),
           (9, 3, '2003-07-11'),
           (9, 4, 'Male'),
           (9, 5, 'other / not a dev'),
           (9, 6, 'false'),
           (9, 7, '501 - 1000$');