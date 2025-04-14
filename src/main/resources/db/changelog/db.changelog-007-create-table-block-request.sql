CREATE TYPE block_status AS ENUM ('CREATED', 'COMPLETED','REJECTED');

CREATE TABLE block_request
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    uuid         NOT NULL,
    card_id    UUID         NOT NULL,
    status     block_status NOT NULL,
    created_at TIMESTAMP        DEFAULT now(),
    updated_at TIMESTAMP        DEFAULT now(),
    CONSTRAINT fk_card_block_request FOREIGN KEY (card_id) REFERENCES public.card (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_users_block_request FOREIGN KEY (user_id) REFERENCES public.users (id) ON DELETE CASCADE ON UPDATE CASCADE

);
