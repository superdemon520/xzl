CREATE TABLE "s_message"."t_group_member_detail" (
    "id" BIGSERIAL NOT NULL,
    "session_id" bigint NOT NULL,
    "sender_id" bigint NOT NULL,
    "sender_role_type" smallint NOT NULL,

    "receiver_id" bigint,
    "receiver_unread_count" integer,
    "created_at" timestamp without time zone,
    "updated_at" timestamp without time zone,
    "deleted_at" timestamp without time zone,
    PRIMARY KEY ("id")
);
COMMENT ON TABLE "s_message"."t_group_member_detail" IS '群组人员详情 暂时统计未读数用';
COMMENT ON COLUMN "s_message"."t_group_member_detail"."session_id" IS '会话id';
COMMENT ON COLUMN "s_message"."t_group_member_detail"."sender_id" IS '发送人id';
COMMENT ON COLUMN "s_message"."t_group_member_detail"."sender_role_type" IS '发送人角色';
COMMENT ON COLUMN "s_message"."t_group_member_detail"."receiver_id" IS '接受人id';
COMMENT ON COLUMN "s_message"."t_group_member_detail"."receiver_unread_count" IS '未读数';

CREATE TRIGGER t_group_member_detail_create BEFORE
INSERT ON t_group_member_detail
FOR EACH ROW
EXECUTE PROCEDURE update_timestamp_when_created();



CREATE TRIGGER t_group_member_detail_update BEFORE
UPDATE ON t_group_member_detail
FOR EACH ROW
EXECUTE PROCEDURE update_timestamp_when_updated();


select setval('s_message.t_group_member_detail_id_seq',9999);
