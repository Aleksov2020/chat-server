


CREATE TABLE IF NOT EXISTS media
(
    id bigserial primary key,
    name varchar(254) not null,
    type int not null,
    size serial not null,
    thumb_id bigserial,
    path varchar(254) not null
);

create table if not exists users
(
    id bigserial primary key,
    picture_id bigserial,
    date_birth date,
    phone varchar(11) not null,
    user_name varchar(100) not null,
    first_name varchar(100),
    middle_name varchar(100),
    last_name varchar(100),
    date_last_enter date not null,
    date_last_update date not null,
    date_create date not null,
    is_active boolean,
    is_reported boolean,
    is_banned boolean,

    constraint fk_img
        foreign key (picture_id)
            references media(id)
);

create table if not exists chat_rooms
(
    id bigserial primary key,
    chat_name varchar(254) not null,
    media_id bigserial,
    chat_type int not null,
    date_start date not null,
    date_last_update date not null,

    CONSTRAINT fk_img
        FOREIGN KEY(media_id)
            REFERENCES media(id)
            ON DELETE SET NULL
);

create table if not exists participant
(
    id bigserial primary key,
    chat_room_id bigserial,
    user_id bigserial,

    constraint fk_chat_room
        foreign key (chat_room_id)
            references chat_rooms(id),

    constraint fk_users
        foreign key (user_id)
            references users(id)
);
create table if not exists content
(
    id bigserial primary key,
    content varchar(2540)
);

create table if not exists content_list
(
    id bigserial primary key,
    content_id bigserial,
    media_id bigserial,

    constraint fk_content
        foreign key (content_id)
            references content(id),

    constraint fk_media
        foreign key (media_id)
            references media(id)
);


create table if not exists messages
(
    id bigserial primary key,
    sender_id bigserial,
    chat_id bigserial,
    content_id bigserial,
    forward_id bigserial,
    date_create date not null,

    CONSTRAINT fk_sender
        FOREIGN KEY(sender_id)
            REFERENCES users(id)
            ON DELETE SET NULL,

    CONSTRAINT fk_chat
        FOREIGN KEY(chat_id)
            REFERENCES chat_rooms(id)
            ON DELETE SET NULL,

    CONSTRAINT fk_content
        FOREIGN KEY(content_id)
            REFERENCES content(id)
            ON DELETE SET NULL,

    CONSTRAINT fk_forward
        FOREIGN KEY(forward_id)
            REFERENCES messages(id)
            ON DELETE SET NULL
);



create table if not exists deleted_messages
(
  id bigserial primary key,
  message_id bigserial,
  user_id bigserial,

  constraint fk_message
    foreign key (message_id)
        references messages(id),

  constraint fk_users
    foreign key (user_id)
        references users(id)
);

create table if not exists deleted_chat_rooms
(
  id bigserial primary key,
  chat_room_id bigserial,
  user_id bigserial,

  constraint fk_chat_room
    foreign key (chat_room_id)
        references chat_rooms(id),

  constraint fk_users
    foreign key (user_id)
        references users(id)
);



