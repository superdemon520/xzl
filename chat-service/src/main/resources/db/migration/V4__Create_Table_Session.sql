CREATE TABLE "s_message"."t_session" (
    "id" BIGSERIAL NOT NULL,
    "initiator_id" bigint,
    "initiator_role_type" smallint,
    "organization_id" bigint,
    "type" smallint,
    "session_type" smallint,
    "created_at" timestamp without time zone,
    "updated_at" timestamp without time zone,
    "deleted_at" timestamp without time zone,
    PRIMARY KEY ("id")
) TABLESPACE "pg_default";
COMMENT ON TABLE "s_message"."t_session" IS '群组';
COMMENT ON COLUMN "s_message"."t_session"."initiator_id" IS '发起人';
COMMENT ON COLUMN "s_message"."t_session"."initiator_role_type" IS '发起人角色';
COMMENT ON COLUMN "s_message"."t_session"."organization_id" IS '机构id';
COMMENT ON COLUMN "s_message"."t_session"."type" IS '群组业务类型';
COMMENT ON COLUMN "s_message"."t_session"."session_type" IS '会话类型';


CREATE TRIGGER t_session_create BEFORE
INSERT ON t_session
FOR EACH ROW
EXECUTE PROCEDURE update_timestamp_when_created();



CREATE TRIGGER t_session_update BEFORE
UPDATE ON t_session
FOR EACH ROW
EXECUTE PROCEDURE update_timestamp_when_updated();


select setval('s_message.t_session_id_seq',9999);
