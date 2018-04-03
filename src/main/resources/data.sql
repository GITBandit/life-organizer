INSERT INTO user (id, username, password, first_name, last_name, enabled) VALUES (1, 'user1', '$2a$04$T3t./rwzYhI8aigPpHvvzOLZKh8c0yN3lj47kDji8j/XwLwAna4Pu', 'UserName', 'UserLastName', true);
INSERT INTO user (id, username, password, first_name, last_name, enabled) VALUES (2, 'user2', '$2a$04$pzOlghcbYMyo15pDNRVFHuUdxdRxL9a2mWHBE7dznkPtvNSWxy6gm', 'UserName2', 'UserLastName2', true);
INSERT INTO user_role (id, role, username) VALUES (1, 'ROLE_ADMIN', 'user1');
INSERT INTO user_role (id, role, username) VALUES (2, 'ROLE_USER', 'user2');
INSERT INTO personal_data (id, birth_date, user_id) VALUES (1, '2016-03-29', 2);