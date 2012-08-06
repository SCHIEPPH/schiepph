drop table sender_profile;
drop table poc_table;
drop table public_keys_table;

create table sender_profile (
      id                        varchar(50)  not null,
      user_id                   varchar(50)  not null,
      password                  varchar(50)  not null,
      oid                       varchar(50)  not null,
      unique_id                 varchar(50)  not null,
      preferred_format          int(2)  not null,
      facility_type             int(2)  not null,
      transport_layer           int(2)  not null,
      status                    int(2)  not null,
      host                      varchar(75)  not null,
      secure_port               int(5),
      unsecure_port             int(5),
      reporting_channel         varchar(100)  not null,
      street_1                  varchar(100)  not null,
      street_2                  varchar(100)  not null,
      city                      varchar(100)  not null,
      state                     varchar(30)  not null,
      zipcode                   varchar(15)  not null,
      estimated_frequncy        varchar(100)  not null,
      estimated_average_size    varchar(100)  not null,
      estimated_average_count   varchar(100)  not null,
      estimated_xmit_time       varchar(100)  not null,
      hours_of_operation        varchar(100)  not null,

      primary key (id)
);

create table poc_table (
      id                  varchar(50)  not null,
      sender_profile_id   varchar(50)  not null,
      poc_name            varchar(100)  not null,
      primary_email       varchar(100)  not null,
      secondary_email     varchar(100)  not null,
      primary_phone       varchar(100)  not null,
      secondary_phone     varchar(100)  not null,

      primary key (id),
      constraint fk_poc_sender_profile foreign key (sender_profile_id) references sender_profile(id)
);

create table public_keys_table (
      id                  varchar(50)  not null,
      sender_profile_id   varchar(50)  not null,
      key_name            varchar(100)  not null,
      public_key          varchar(2000)  not null,

      primary key (id),
      constraint fk_key_sender_profile foreign key (sender_profile_id) references sender_profile(id)
);
