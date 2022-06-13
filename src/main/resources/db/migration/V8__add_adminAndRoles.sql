INSERT INTO "roles"("name") VALUES('ROLE_ADMIN'),
                                   ('ROLE_LOGIST');

INSERT INTO "users"("name","surname","email","password") VALUES ('admin','adminow','admin@admin.com','$2a$12$LzAeTYdYiCkyJcLb3gEkdeh4x7uJXh.f596pJtjbEaXwfwt7p4KZe');

INSERT INTO "roles_users"("user_id","role_id") VALUES ((SELECT id FROM "users" WHERE "email"='admin@admin.com'
                                                                            AND "name"='admin' AND "surname"='adminow'),
                                               (SELECT id FROM "roles" WHERE "name"='ROLE_ADMIN'));