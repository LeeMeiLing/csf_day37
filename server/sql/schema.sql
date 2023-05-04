create table posts(
    id INT NOT NULL AUTO_INCREMENT,
    blobc MEDIUMBLOB NOT NULL,
    title VARCHAR(100) NOT NULL,
    complain VARCHAR(100) NOT NULL,
    constraint pk_id primary key (id)
);