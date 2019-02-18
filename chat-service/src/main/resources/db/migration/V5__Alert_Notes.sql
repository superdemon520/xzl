COMMENT ON COLUMN "s_message"."t_message"."sender_role_type" IS '发送人角色 （0|患者 1|运营 2|医助 3|医生）';

COMMENT ON COLUMN "s_message"."t_message"."message_type" IS '消息类型 （0|文本 1|图片）';

COMMENT ON COLUMN "s_message"."t_message_session"."role_type" IS '用户角色 （0|患者 1|运营 2|医助 3|医生）';

COMMENT ON COLUMN "s_message"."t_message_session"."user_status" IS '用户状态 （0|正常 1|禁言 2|失效）';

COMMENT ON COLUMN "s_message"."t_message_session"."unread_status" IS '未读数类型 （0|数字 1红点）';

COMMENT ON COLUMN "s_message"."t_session"."type" IS '群组业务类型 （0|患者可见 1|患者不可见）';

COMMENT ON COLUMN "s_message"."t_session"."session_type" IS '会话类型 （0|群组 1|单聊 2|系统发起）';