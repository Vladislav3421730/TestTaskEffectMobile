-- changeset admin:005
CREATE TYPE operation_type AS ENUM ('WITHDRAWAL', 'TRANSFER', 'RECHARGE');
CREATE TYPE operation_result_type AS ENUM ('SUCCESSFULLY','CARD_BLOCKED','CARD_EXPIRED','FAILED');

CREATE TABLE transaction
(
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount           DECIMAL(10, 2)        NOT NULL,
    operation        operation_type        NOT NULL,
    operation_result operation_result_type NOT NULL,
    card_id          UUID                  NOT NULL,
    target_card_id   UUID,
    timestamp        TIMESTAMP        DEFAULT now(),
    CONSTRAINT fk_transaction_card FOREIGN KEY (card_id) REFERENCES card (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_transaction_target_card FOREIGN KEY (target_card_id) REFERENCES card (id) ON DELETE SET NULL ON UPDATE CASCADE
);

