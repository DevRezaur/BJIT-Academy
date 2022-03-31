-- insert data in roles table
INSERT INTO roles
VALUES(1, 'ROLE_ADMIN');

INSERT INTO roles
VALUES(2, 'ROLE_USER');

-- insert data in users table
INSERT INTO users (user_id, full_name, username, password, department, image_url)
VALUES('11111', 'Mohammad Shamsus Saleehin', 'saleehin@bjitgroup.com', 'saleehin', 'Management', 'https://devrezaur.com/File-Bucket/image/default_user.png');

INSERT INTO users (user_id, full_name, username, password, department, image_url)
VALUES('22222', 'Nani Gopal', 'nani@bjitgroup.com', 'nanigopal', 'Management', 'https://devrezaur.com/File-Bucket/image/default_user.png');

INSERT INTO users (user_id, full_name, username, password, department, image_url)
VALUES('10595', 'Rezaur Rahman', 'rahman.rezaur@bjitgroup.com', 'rezaur', 'Java', 'https://devrezaur.com/File-Bucket/image/default_user.png');

INSERT INTO users (user_id, full_name, username, password, department, image_url)
VALUES('10597', 'Sanzida Sultana', 'sanzida.sultana@bjitgroup.com', 'sanzida', 'Java', 'https://devrezaur.com/File-Bucket/image/default_user.png');

INSERT INTO users (user_id, full_name, username, password, department, image_url)
VALUES('10472', 'Kazi Taneem', 'kazi.taneem@bjitgroup.com', 'taneem', 'Blockchain', 'https://devrezaur.com/File-Bucket/image/default_user.png');

INSERT INTO users (user_id, full_name, username, password, department, image_url)
VALUES('10621', 'Izazul Saad', 'izazul.saad@bjitgroup.com', 'saad', 'SQA', 'https://devrezaur.com/File-Bucket/image/default_user.png');

-- insert data in user_role table
INSERT INTO user_role
VALUES('11111', 1);

INSERT INTO user_role
VALUES('22222', 1);

INSERT INTO user_role
VALUES('10595', 2);

INSERT INTO user_role
VALUES('10597', 2);

INSERT INTO user_role
VALUES('10472', 2);

INSERT INTO user_role
VALUES('10621', 2);

-- insert data in batches table
INSERT INTO batches
VALUES(100, 'Java Batch - 04', 'This is a sample description of Java batch 04. This batch started at October, ' ||
        '2021 and expects to end at December 2021', 'https://devrezaur.com/File-Bucket/image/spring.jpg');

INSERT INTO batches
VALUES(200, 'SAP Batch - 01', 'This is a sample description of SAP batch 01. This batch started at October, ' ||
        '2021 and expects to end at December 2021', 'https://devrezaur.com/File-Bucket/image/default_course.jpg');

INSERT INTO batches
VALUES(300, 'Python Batch - 02', 'This is a sample description of Python batch 02. This batch started at January, ' ||
        '2022 and expects to end at March 2022', 'https://devrezaur.com/File-Bucket/image/python.png');

-- insert data in posts table
INSERT INTO posts(post_id, batch_id, user_id, create_date, description, resource_name, resources_link)
VALUES(101, 100, '11111', '2021-10-09 20:35:17.978', 'Tomorrow we will have a training class on Spring Security. ' ||
        'Before joining the session please have a look the the attached document. Session Time: 15 October 3.00 PM ' ||
        'Meet Link: meet.google.com/obu-dnhu-csq', 'Spring Security.docx',
       'https://devrezaur.com/File-Bucket/file/Spring Security.docx');

INSERT INTO posts(post_id, batch_id, user_id, create_date, description, resource_name, resources_link)
VALUES(102, 100, '11111', '2021-10-10 20:35:17.978', 'Tomorrow we will have a training class on Git Gerrit. ' ||
        'Before joining the session please have a look the the attached document. ', 'Git Gerrit Commands.pdf',
       'https://devrezaur.com/File-Bucket/file/Git Gerrit Commands.pdf');

INSERT INTO posts(post_id, batch_id, user_id, create_date, description, resource_name, resources_link)
VALUES(103, 100, '11111', '2021-10-11 20:35:17.978', 'Tomorrow there will be no training class due to government holiday. ' ||
        'We will resume our activities from the day after tomorrow. Happy holiday. Cheers.', '',
       'https://devrezaur.com/File-Bucket/file/');

-- insert data in enrolled_users table
INSERT INTO enrolled_users
VALUES(100, '10595');

INSERT INTO enrolled_users
VALUES(100, '10597');

INSERT INTO enrolled_users
VALUES(100, '22222');

-- insert data in messages table
INSERT INTO messages(batch_id, create_date, full_name, message)
VALUES(100, '2021-10-11 20:35:17.118', 'Rezaur Rahman', 'Hi, can anybody help me with git commands?');

INSERT INTO messages(batch_id, create_date, full_name, message)
VALUES(100, '2021-10-11 20:35:17.478', 'Mohammad Shamsus Saleehin', 'Sure, what kind of help do you want? Feel free to ask.');

INSERT INTO messages(batch_id, create_date, full_name, message)
VALUES(100, '2021-10-11 20:35:17.978', 'Rezaur Rahman', 'What is the purpose of git remote?');

INSERT INTO messages(batch_id, create_date, full_name, message)
VALUES(100, '2021-10-11 20:35:18.278', 'Mohammad Shamsus Saleehin', 'Git remote indicates the remote repository. The repository, that is stored in the cloud.');

INSERT INTO messages(batch_id, create_date, full_name, message)
VALUES(100, '2021-10-11 20:35:18.778', 'Rezaur Rahman', 'Okay I understood. Thank you.');