-- Roles
insert into ROLE (created, created_by, modified, modified_by, description, name)
values (current_timestamp, 'jselva', current_timestamp, 'jselva', 'Admin', 'ROLE_ADMIN'),
       (current_timestamp, 'jselva', current_timestamp, 'jselva', 'Read', 'ROLE_READ');

-- User admin
insert into user_nisum (created, created_by, modified, modified_by, email, is_active, last_login, name, password, token)
values (current_timestamp, 'jselva', current_timestamp, 'jselva', 'juanorlandoselvamadrigal@gmail.com', true, current_timestamp, 'Juan Selva', '$2b$10$oo3Ciy0PNVPH1sSbklfLO./fDH8G7Rm6LbYn/zdvUQNDtqQeN4MNi', '');

insert into PHONE (created, created_by, modified, modified_by, city_code, country_code, number, user_id)
select current_timestamp, 'jselva', current_timestamp, 'jselva', '10000', '505', '89999900', id
from user_nisum;

insert into USER_HAS_ROLE (user_id, role_id)
select (select max(id) from user_nisum) as id_user, (select min(id) from role) as id_role;

commit;