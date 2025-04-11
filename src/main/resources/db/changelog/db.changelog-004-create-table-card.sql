-- changeset admin:004
CREATE TYPE card_status AS ENUM ('ACTIVE', 'BLOCKED', 'EXPIRED');

CREATE TABLE public.card
(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    number VARCHAR(255) NOT NULL,
    balance DECIMAL(10,2) NOT NULL,
    status card_status NOT NULL,
    expiration_date DATE NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES public.users (id) ON DELETE CASCADE ON UPDATE CASCADE

)
