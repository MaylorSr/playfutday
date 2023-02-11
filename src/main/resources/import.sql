insert into USER_ENTITY (id, username, password, avatar, biography, phone, birthday, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at) values ('9733327f-5447-4d9f-a59a-c904c3e20f0a', 'Levey', '9ymN0fNyv2', 'https://static.vecteezy.com/system/resources/previews/006/026/787/non_2x/avatar-profile-default-social-media-photo-icon-in-flat-style-vector.jpg', 'Postprocedural hematoma and seroma of a musculoskeletal structure following a procedure', 34, '1996-03-14', true, true, true, true, current_timestamp);
insert into USER_ENTITY (id, username, password, avatar, biography, phone, birthday, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at) values ('1b84f2a2-409b-4d4a-8c75-998a28a0d198', 'Wilmette', 'MwraCRtJub', 'https://static.vecteezy.com/system/resources/previews/006/026/787/non_2x/avatar-profile-default-social-media-photo-icon-in-flat-style-vector.jpg', 'Peripheral tear of lateral meniscus, current injury, right knee, sequela', 30, '1997-06-24', true, true, true, true, current_timestamp);
insert into USER_ENTITY (id, username, password, avatar, biography, phone, birthday, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at) values ('d750a992-6aa6-4f75-b418-ead273c7f2a0', 'Gerick', 'IfLWojE5ni', 'https://static.vecteezy.com/system/resources/previews/006/026/787/non_2x/avatar-profile-default-social-media-photo-icon-in-flat-style-vector.jpg', 'Abscess of anal and rectal regions', 87, '2001-08-12', true, true, true, true, current_timestamp);
insert into USER_ENTITY (id, username, password, avatar, biography, phone, birthday, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at) values ('79a06606-d4a8-4e81-b5e2-ef303724085b', 'Keen', 'ZPgST1d7n', 'https://static.vecteezy.com/system/resources/previews/006/026/787/non_2x/avatar-profile-default-social-media-photo-icon-in-flat-style-vector.jpg', 'Nondisplaced fracture of first metatarsal bone, left foot, subsequent encounter for fracture with routine healing', 21, '1992-05-01', true, true, true, true, current_timestamp);
insert into USER_ENTITY (id, username, password, avatar, biography, phone, birthday, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at) values ('d8825163-3195-4f7c-8142-a5fa44da7b16', 'Margie', 'oaWmjcKcu4l2', 'https://static.vecteezy.com/system/resources/previews/006/026/787/non_2x/avatar-profile-default-social-media-photo-icon-in-flat-style-vector.jpg', 'Trimethylaminuria', 60, '1991-10-05', true, true, true, true, current_timestamp);
insert into USER_ENTITY (id, username, password, avatar, biography, phone, birthday, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at) values ('65582b7a-3c16-4634-9753-a3645d1d29c3', 'Britteny', 'v09peJ', 'https://static.vecteezy.com/system/resources/previews/006/026/787/non_2x/avatar-profile-default-social-media-photo-icon-in-flat-style-vector.jpg', 'Displaced associated transverse-posterior fracture of unspecified acetabulum', 80, '2002-10-22', true, true, true, true, current_timestamp);

insert into POST_ENTITY(id, tag, description, image, upload_date, user_id) values ('1', '#cr7', 'Este es mi nuevo post señores', 'https://upload.wikimedia.org/wikipedia/commons/8/8c/Cristiano_Ronaldo_2018.jpg', current_timestamp, '9733327f-5447-4d9f-a59a-c904c3e20f0a');
insert into POST_ENTITY(id, tag, description, image, upload_date, user_id) values ('2', '#cr7', 'Es obvio que cr7 es mucho mejor que Messi!', 'https://upload.wikimedia.org/wikipedia/commons/b/b4/Lionel-Messi-Argentina-2022-FIFA-World-Cup_%28cropped%29.jpg', current_timestamp, '1b84f2a2-409b-4d4a-8c75-998a28a0d198');
insert into POST_ENTITY(id, tag, description, image, upload_date, user_id) values ('3', '#Messi', 'eh, tontitos, Messi es Dios', 'https://www.cronista.com/files/image/509/509816/63dea699da64d_360_480!.jpg?s=bcc9285b7707d85bb3d81888f5987fc8&d=1675538120', current_timestamp, 'd750a992-6aa6-4f75-b418-ead273c7f2a0');
insert into POST_ENTITY(id, tag, description, image, upload_date, user_id) values ('4', '#Neymar', 'Mucho Messi y Cr7 pero Neymar?', 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Bra-Cos_%281%29_%28cropped%29.jpg/640px-Bra-Cos_%281%29_%28cropped%29.jpg', current_timestamp, '79a06606-d4a8-4e81-b5e2-ef303724085b');

insert into COMMENTARY_ENTITY (id, message, post_id, user_id) values ('5', 'Vaya basura cr7!', '1', 'd750a992-6aa6-4f75-b418-ead273c7f2a0');
insert into COMMENTARY_ENTITY (id, message, post_id, user_id) values ('6', 'Viva cr7 cabron!', '1', '1b84f2a2-409b-4d4a-8c75-998a28a0d198');
insert into COMMENTARY_ENTITY (id, message, post_id, user_id) values ('7', '¿Pero quién es ese Neymar?', '4', 'd750a992-6aa6-4f75-b418-ead273c7f2a0');
insert into COMMENTARY_ENTITY (id, message, post_id, user_id) values ('8', 'Cr7 el mejor', '2', '9733327f-5447-4d9f-a59a-c904c3e20f0a');
insert into COMMENTARY_ENTITY (id, message, post_id, user_id) values ('9', 'Vaya basura cr7!', '1', 'd750a992-6aa6-4f75-b418-ead273c7f2a0');

insert into USER_ROLES (user_id, roles) values ('9733327f-5447-4d9f-a59a-c904c3e20f0a', 'ADMIN');
insert into USER_ROLES (user_id, roles) values ('1b84f2a2-409b-4d4a-8c75-998a28a0d198', 'USER');
insert into USER_ROLES (user_id, roles) values ('d750a992-6aa6-4f75-b418-ead273c7f2a0', 'USER');
insert into USER_ROLES (user_id, roles) values ('79a06606-d4a8-4e81-b5e2-ef303724085b', 'USER');
insert into USER_ROLES (user_id, roles) values ('d8825163-3195-4f7c-8142-a5fa44da7b16', 'USER');
insert into USER_ROLES (user_id, roles) values ('65582b7a-3c16-4634-9753-a3645d1d29c3', 'USER');