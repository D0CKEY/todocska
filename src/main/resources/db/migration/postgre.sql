CREATE TABLE IF NOT EXISTS public.roles
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(45) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.roles
    OWNER to postgres;



CREATE TABLE IF NOT EXISTS public.todo
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    megnevezes text COLLATE pg_catalog."default" NOT NULL,
    hatarido date NOT NULL,
    kesz boolean NOT NULL,
    gid bigint NOT NULL,
    CONSTRAINT pk_todo PRIMARY KEY (id),
    CONSTRAINT fk_todo_on_gid FOREIGN KEY (gid)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.todo
    OWNER to postgres;



CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    username character varying(45) COLLATE pg_catalog."default" NOT NULL,
    nev character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password character varying(64) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    profilkep character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uc_users_username UNIQUE (username)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;



CREATE TABLE IF NOT EXISTS public.users_role
(
    role_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT pk_users_role PRIMARY KEY (role_id, user_id),
    CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id)
        REFERENCES public.roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users_role
    OWNER to postgres;

INSERT INTO public.roles VALUES ('1', 'ROLE_ADMIN');
INSERT INTO public.roles VALUES ('2', 'ROLE_USER');

