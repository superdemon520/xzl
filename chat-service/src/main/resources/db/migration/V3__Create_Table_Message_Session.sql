CREATE TABLE "s_message"."t_message_session" (
    "id" BIGSERIAL NOT NULL,
    "session_id" bigint NOT NULL,
    "user_id" bigint NOT NULL,
    "role_type" smallint NOT NULL,
    "unread_count" integer NOT NULL,
    "user_status" smallint,
    "unread_status" smallint,
    "read_at" timestamp without time zone,
    "created_at" timestamp without time zone,
    "updated_at" timestamp without time zone,
    "deleted_at" timestamp without time zone,
    PRIMARY KEY ("id")
) TABLESPACE "pg_default";
COMMENT ON COLUMN "s_message"."t_message_session"."session_id" IS '群组id';
COMMENT ON COLUMN "s_message"."t_message_session"."user_id" IS '用户id';
COMMENT ON COLUMN "s_message"."t_message_session"."role_type" IS '用户角色';
COMMENT ON COLUMN "s_message"."t_message_session"."unread_count" IS '用户未读消息数';
COMMENT ON COLUMN "s_message"."t_message_session"."user_status" IS '用户状态';
COMMENT ON COLUMN "s_message"."t_message_session"."read_at" IS '未读数清零时间';
COMMENT ON COLUMN "s_message"."t_message_session"."unread_status" IS '未读数类型（数字、红点）';



CREATE TRIGGER t_message_session_create BEFORE
INSERT ON t_message_session
FOR EACH ROW
EXECUTE PROCEDURE update_timestamp_when_created();



CREATE TRIGGER t_message_session_update BEFORE
UPDATE ON t_message_session
FOR EACH ROW
EXECUTE PROCEDURE update_timestamp_when_updated();


select setval('s_message.t_message_session_id_seq',9999);
