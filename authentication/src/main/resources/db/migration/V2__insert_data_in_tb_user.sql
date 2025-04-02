INSERT INTO
    tb_security (code, email, expiration_date)
VALUES
    (3222, 'flaviojunior.work@gmail.com', NOW());

INSERT INTO
    tb_user (created_at, name, surname, username, email, password, type_account, account_non_expired, account_non_locked, credentials_non_expired, enabled)
VALUES
    (NOW(), 'flávio', 'júnior', 'flavio-junior', 'flaviojunior.work@gmail.com', '$2a$10$IDEg.Gebd7tUPbnot8dbqeo4ExCed4BZzc5AfuJQ41k1q8L90594i', 'ADMIN', true, true, true, true);

INSERT INTO
    tb_user_security (fk_user, fk_security)
VALUES
    (1, 1);