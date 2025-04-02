CREATE TABLE IF NOT EXISTS tb_user(
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    created_at TIMESTAMP NOT NULL,
    name VARCHAR(30) NULL,
    surname VARCHAR(30) NULL,
    username VARCHAR(20) NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(300) NOT NULL,
    type_account ENUM('ADMIN', 'USER') NOT NULL,
    account_non_expired bit(1) NOT NULL,
    account_non_locked bit(1) NOT NULL,
    credentials_non_expired bit(1) NOT NULL,
    enabled bit(1) NOT NULL
);

alter table tb_user add constraint uc_tb_user_email unique(email);

CREATE TABLE IF NOT EXISTS tb_security(
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    code INT NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL,
    expiration_date TIMESTAMP NOT NULL
);

alter table tb_security add constraint uc_tb_security_code unique(code);
alter table tb_security add constraint uc_tb_security_email unique(email);


CREATE TABLE IF NOT EXISTS tb_user_security(
    fk_user INT REFERENCES tb_user(id),
    fk_security INT REFERENCES tb_security(id),
    PRIMARY KEY (fk_user, fk_security)
);

CREATE TABLE IF NOT EXISTS tb_recover_password(
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    code INT NOT NULL,
    email VARCHAR(50) NOT NULL,
    expiration_date TIMESTAMP NOT NULL
);

alter table tb_recover_password add constraint uc_tb_recover_password_code unique(code);
alter table tb_recover_password add constraint uc_tb_recover_password_email unique(email);
