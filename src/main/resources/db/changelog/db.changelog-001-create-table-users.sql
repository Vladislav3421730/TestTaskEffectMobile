-- changeset admin:001
CREATE TABLE public.users
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(50)  UNIQUE CHECK (POSITION('@' IN email) > 1),
    password   VARCHAR(255) NOT NULL
);
