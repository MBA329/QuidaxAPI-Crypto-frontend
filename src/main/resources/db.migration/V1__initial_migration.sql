create table transactions
(
    id                 bigint auto_increment
        primary key,
    phone_number       varchar(15)                            not null,
    network            varchar(10)                            not null,
    data_plan_code     varchar(25)                            not null,
    amount_naira       decimal(10, 2)                         not null,
    amount_crypto      decimal(10, 2)                         not null,
    crypto_currency    varchar(25)                            not null,
    wallet_id          binary(16) default bin_to_uuid(uuid()) not null,
    transaction_status varchar(15)                            not null,
    delivery_status    varchar(15)                            not null,
    request_id         varchar(30)                            not null,
    transaction_hash   varchar(50)                            null,
    transaction_id     varchar(30)                            null,
    created_at         datetime   default current_timestamp() not null,
    constraint transactions_wallets_id_fk
        foreign key (wallet_id) references wallets (id)
);

create table wallets
(
    id              binary(16) default uuid_to_bin(uuid()) not null
        primary key,
    deposit_address varchar(50)                            not null,
    crypto_currency varchar(15)                            not null,
    user_id         binary(16) default uuid_to_bin(uuid()) not null,
    constraint wallets_users_id_fk
        foreign key (user_id) references users (id)
);

create table users
(
    id       binary(16) default uuid_to_bin(uuid()) not null
        primary key,
    email    varchar(50)                            not null,
    password varchar(255)                           not null
);



