-- changeset admin:003
CREATE TABLE public.user_role
(
    user_id uuid NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES public.users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES public.role (id) ON DELETE CASCADE ON UPDATE CASCADE
);
