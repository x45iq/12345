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
insert into t_user(id, c_username)
values ('d6f98709-cc3e-4f54-b461-579132861655', 'user');
insert into t_user_password(id_user, c_password)
values ('d6f98709-cc3e-4f54-b461-579132861655', '{noop}password');