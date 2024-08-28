create table events
(
    id       int not null auto_increment,
    name     varchar(50) not null,
    cost     int         not null,
    duration int         not null,
    primary key (id)
);