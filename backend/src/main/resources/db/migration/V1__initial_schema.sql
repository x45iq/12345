create table if not exists t_course
(
    id      uuid PRIMARY KEY,
    c_title text check (length(trim(c_title)) > 0)
);
create table t_user
(
    id         uuid primary key,
    c_username varchar not null unique
);
create table t_user_password
(
    id         serial primary key,
    id_user    uuid not null unique references t_user (id),
    c_password text
);