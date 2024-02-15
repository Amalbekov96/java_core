INSERT INTO training_type (training_type)
VALUES ('FITNESS');
INSERT INTO training_type (training_type)
VALUES ('YOGA');
INSERT INTO training_type (training_type)
VALUES ('WEIGHT_LIFTING');
INSERT INTO training_type (training_type)
VALUES ('CARDIO');

INSERT INTO users (first_name, last_name, username, password, is_active, role)
VALUES ('Kushtar', 'Amalbekov', 'Kushtar.Amalbekov', '$2a$12$YpUtSKzK0i6PKJLYp9VruO7kOMjX56n551RSmTgJBYqIvQKO3D/xC',
        true, 'TRAINER');
INSERT INTO users (first_name, last_name, username, password, is_active, role)
VALUES ('Aiperi', 'Adylova', 'Aiperi.Adylova', '$2a$12$Ki9eoKu3WrnGuCk0n5shV.zaNmjGBiZ06P7Rud7UGCg.CJF88q9JW
', true, 'TRAINER');
INSERT INTO users (first_name, last_name, username, password, is_active, role)
VALUES ('Eldiyar', 'Toktomamatov', 'Eldiyar.Toktomamatov', '$2a$12$Ki9eoKu3WrnGuCk0n5shV.zaNmjGBiZ06P7Rud7UGCg.CJF88q9JW
', true, 'TRAINEE');
INSERT INTO users (first_name, last_name, username, password, is_active, role)
VALUES ('Kanysh', 'Abdyrakmanova', 'Kanysh.Abdyrakmanova', '$2a$12$Ki9eoKu3WrnGuCk0n5shV.zaNmjGBiZ06P7Rud7UGCg.CJF88q9JW
', true, 'TRAINEE');
INSERT INTO users (first_name, last_name, username, password, is_active, role)
VALUES ('Kairat', 'Uzenov', 'Kairat.Uzenov', '$2a$12$Ki9eoKu3WrnGuCk0n5shV.zaNmjGBiZ06P7Rud7UGCg.CJF88q9JW
', true, 'TRAINEE');

INSERT INTO trainer (specialization_id, user_id)
VALUES ((SELECT id FROM training_type WHERE training_type = 'FITNESS'),
        (SELECT user_id FROM Users WHERE username = 'Kushtar.Amalbekov'));
INSERT INTO trainer (specialization_id, user_id)
VALUES ((SELECT id FROM training_type WHERE training_type = 'YOGA'),
        (SELECT user_id FROM Users WHERE username = 'Aiperi.Adylova'));

INSERT INTO trainee (date_of_birth, address, user_id)
VALUES (CURRENT_DATE, '123 Street, City', (SELECT user_id FROM Users WHERE username = 'Kanysh.Abdyrakmanova'));
INSERT INTO trainee (date_of_birth, address, user_id)
VALUES (CURRENT_DATE, '456 Avenue, Town', (SELECT user_id FROM Users WHERE username = 'Eldiyar.Toktomamatov'));
INSERT INTO trainee (date_of_birth, address, user_id)
VALUES (CURRENT_DATE, '456 Avenue, Town', (SELECT user_id FROM Users WHERE username = 'Kairat.Uzenov'));

