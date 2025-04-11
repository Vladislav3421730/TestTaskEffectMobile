-- changeset admin:002
CREATE SEQUENCE public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE public.role
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('role_id_seq'),
    name VARCHAR(255) NOT NULL
);

ALTER TABLE public.role OWNER TO postgres;
ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;
