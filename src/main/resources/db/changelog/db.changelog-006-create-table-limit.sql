-- changeset admin:006
CREATE TABLE limits
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    card_id       UUID NOT NULL,
    daily_limit   DECIMAL(10, 2) DEFAULT 300.00,
    monthly_limit DECIMAL(10, 2) DEFAULT 3400.00,
    created_at    TIMESTAMP DEFAULT now(),
    updated_at    TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_card_limits FOREIGN KEY (card_id) REFERENCES public.card (id) ON DELETE CASCADE ON UPDATE CASCADE
);
