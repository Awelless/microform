insert into field(id, label, type, is_active, is_required) values
    (2, 'Checkbox', 'CHECKBOX', true, true),
    (3, 'Combobox', 'COMBOBOX', true, true);

insert into field_option(field_id, option) values
    (3, 'Option 1'),
    (3, 'Option 2');