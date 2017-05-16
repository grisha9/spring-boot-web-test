DROP TABLE IF EXISTS contact;


CREATE TABLE contact (
  id         INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(60) NOT NULL,
  last_name  VARCHAR(40) NOT NULL,
  birth_date date,
  DESCRIPTION VARCHAR (2000),
  PHOTO BLOB,
  version int not null DEFAULT 0,
  UNIQUE UQ_CONSTRAINt_NAME(first_name, last_name),
  PRIMARY KEY(id)
);
