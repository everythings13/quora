
--USERS table is created to store the details of all the users
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users
  (
     id            SERIAL,
     uuid          VARCHAR(200) NOT NULL,
     firstname     VARCHAR(30) NOT NULL,
     lastname      VARCHAR(30) NOT NULL,
     username      VARCHAR(30) UNIQUE NOT NULL,
     email         VARCHAR(50) UNIQUE NOT NULL,
     password      VARCHAR(255) NOT NULL,
     salt          VARCHAR(200) NOT NULL,
     country       VARCHAR(30),
     aboutme       VARCHAR(50),
     dob           VARCHAR(30),
     role          VARCHAR(30),
     contactnumber VARCHAR(30),
     PRIMARY KEY (id)
  );

INSERT INTO users
            (id,
             uuid,
             firstname,
             lastname,
             username,
             email,
             password,
             salt,
             country,
             aboutme,
             dob,
             role,
             contactnumber)
VALUES      (1024,
             'rdtrdtdyt',
             'Abhi',
             'Mahajan',
             'abhi',
             'a@gmail.com',
             '507FF5FED1CAC746',
             '8Xt6jxoCI3MWsVaKY/1ySAp2qzlb2Z7P89+vDrb1o6U=',
             'India',
             'I am @ UpGrad',
             '22-10-1995',
             'admin',
             '1222333333' );

--USER_AUTH table is created to store the login information of all the users
DROP TABLE IF EXISTS user_auth CASCADE;

CREATE TABLE IF NOT EXISTS user_auth
  (
     id           BIGSERIAL PRIMARY KEY,
     uuid         VARCHAR(200) NOT NULL,
     user_id      INTEGER NOT NULL,
     access_token VARCHAR(500) NOT NULL,
     expires_at   TIMESTAMP NOT NULL,
     login_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     logout_at    TIMESTAMP NULL
  );

ALTER TABLE user_auth
  ADD CONSTRAINT fk_user_auth_user_id FOREIGN KEY(user_id) REFERENCES users(id)
  ON DELETE CASCADE;

--QUESTION table is created to store the questions related information posted by any user in the Application
DROP TABLE IF EXISTS question CASCADE;

CREATE TABLE IF NOT EXISTS question
  (
     id      SERIAL,
     uuid    VARCHAR(200) NOT NULL,
     content VARCHAR(500) NOT NULL,
     date    TIMESTAMP NOT NULL,
     user_id INTEGER NOT NULL,
     PRIMARY KEY(id),
     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
  );

--ANSWER table is created to store the answers related information in reply to any question posted in the Application
DROP TABLE IF EXISTS answer CASCADE;

CREATE TABLE IF NOT EXISTS answer
  (
     id          SERIAL,
     uuid        VARCHAR(200) NOT NULL,
     ans         VARCHAR(255) NOT NULL,
     date        TIMESTAMP NOT NULL,
     user_id     INTEGER NOT NULL,
     question_id INTEGER NOT NULL,
     PRIMARY KEY(id),
     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
     FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
  );