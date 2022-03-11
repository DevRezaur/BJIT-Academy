INSERT INTO roles
VALUES(1, 'ROLE_ADMIN');

INSERT INTO roles
VALUES(2, 'ROLE_USER');

INSERT INTO users (user_id, full_name, username, password)
VALUES('11111', 'Mohammad Shamsus Saleehin', 'saleehin@bjitgroup.com', 'iamadmin');

INSERT INTO users (user_id, full_name, username, password)
VALUES('11364', 'Rezaur Rahman', 'rahman.rezaur@bjitgroup.com', 'iamuser');

INSERT INTO user_role
VALUES('11111', 1);

INSERT INTO user_role
VALUES('11364', 2);