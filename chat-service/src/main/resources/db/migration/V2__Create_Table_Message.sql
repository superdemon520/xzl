CREATE TABLE "s_message"."t_message" (
    "id" BIGSERIAL NOT NULL,
    "sender_id" bigint NOT NULL,
    "sender_role_type" smallint NOT NULL,
    "session_id" bigint NOT NULL,
    "content" text NOT NULL,
    "message_type" smallint NOT NULL,
    "receiver_id" bigint,
    "created_at" timestamp without time zone,
    "updated_at" timestamp without time zone,
    "deleted_at" timestamp without time zone,
    PRIMARY KEY ("id")
) TABLESPACE "pg_default";
COMMENT ON COLUMN "s_message"."t_message"."sender_id" IS '发送人id';
COMMENT ON COLUMN "s_message"."t_message"."sender_role_type" IS '发送人类型';
COMMENT ON COLUMN "s_message"."t_message"."session_id" IS '接收群组';
COMMENT ON COLUMN "s_message"."t_message"."content" IS '消息内容';
COMMENT ON COLUMN "s_message"."t_message"."message_type" IS '消息类型';
COMMENT ON COLUMN "s_message"."t_message"."receiver_id" IS '消息接受人';



CREATE TRIGGER t_message_create BEFORE
INSERT ON t_message
FOR EACH ROW
EXECUTE PROCEDURE update_timestamp_when_created();



CREATE TRIGGER t_message_update BEFORE
UPDATE ON t_message
FOR EACH ROW
EXECUTE PROCEDURE update_timestamp_when_updated();


select setval('s_message.t_message_id_seq',9999);
