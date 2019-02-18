CREATE TABLE "s_message"."t_member_status" (
    "id" BIGSERIAL NOT NULL,
    "doctor_id" bigint NOT NULL,
    "assistant_id" bigint NOT NULL,
    "assistant_status" smallint NOT NULL,
    "created_at" timestamp without time zone,
    "updated_at" timestamp without time zone,
    "deleted_at" timestamp without time zone,
    PRIMARY KEY ("id")
);
COMMENT ON TABLE "s_message"."t_member_status" IS '医助状态表';

CREATE TRIGGER t_member_status_create BEFORE
INSERT ON t_member_status
FOR EACH ROW
EXECUTE PROCEDURE update_timestamp_when_created();



CREATE TRIGGER t_member_status_update BEFORE
UPDATE ON t_member_status
FOR EACH ROW
EXECUTE PROCEDURE update_timestamp_when_updated();


select setval('s_message.t_member_status_id_seq',9999);
