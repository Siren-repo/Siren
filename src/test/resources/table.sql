DROP TABLE IF EXISTS users;
CREATE TABLE users(
    id bigint NOT NULL AUTO_INCREMENT,
    password varchar(255) DEFAULT NULL,
    created_datetime datetime(6) DEFAULT NULL,
    modified_date_time datetime(6) DEFAULT NULL,
    email varchar(255) DEFAULT NULL,
    allergy varchar(255) DEFAULT NULL,
    is_deleted tinyint DEFAULT NULL,
    nick_name varchar(6) DEFAULT NULL,
    phone varchar(255) DEFAULT NULL,
    role varchar(255) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_member_email (email)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;