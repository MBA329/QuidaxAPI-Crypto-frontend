alter table transactions
    change created_at is_terminated boolean default true not null;


