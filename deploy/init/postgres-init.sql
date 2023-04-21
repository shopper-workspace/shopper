CREATE USER "order-service-user" WITH PASSWORD 'password' CREATEDB;
CREATE DATABASE "order-service"
    WITH
    OWNER = "order-service-user"
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

CREATE USER "inventory-service-user" WITH PASSWORD 'password' CREATEDB;
CREATE DATABASE "inventory-service"
    WITH
    OWNER = "inventory-service-user"
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;