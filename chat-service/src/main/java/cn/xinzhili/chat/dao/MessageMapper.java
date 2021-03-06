package cn.xinzhili.chat.dao;

import cn.xinzhili.chat.model.Message;
import cn.xinzhili.chat.model.MessageExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface MessageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    int countByExample(MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    int deleteByExample(MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    @Update({
        "update s_message.t_message",
        "set deleted_at = now()",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    @Select({
        "insert into s_message.t_message (sender_id, sender_role_type, ",
        "session_id, content, ",
        "message_type, ",
        "receiver_id, created_at, ",
        "updated_at, deleted_at)",
        "values (#{senderId,jdbcType=BIGINT}, #{senderRoleType,jdbcType=SMALLINT,typeHandler=cn.xinzhili.xutils.mybatis.typehandlers.EnumCodeHandler}, ",
        "#{sessionId,jdbcType=BIGINT}, #{content,jdbcType=VARCHAR}, ",
        "#{messageType,jdbcType=SMALLINT,typeHandler=cn.xinzhili.xutils.mybatis.typehandlers.EnumCodeHandler}, ",
        "#{receiverId,jdbcType=BIGINT}, #{createdAt,jdbcType=TIMESTAMP}, ",
        "#{updatedAt,jdbcType=TIMESTAMP}, #{deletedAt,jdbcType=TIMESTAMP})", 
        " returning id"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    long insertReturningId(Message record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    @Insert({
        "insert into s_message.t_message (sender_id, sender_role_type, ",
        "session_id, content, ",
        "message_type, ",
        "receiver_id, created_at, ",
        "updated_at, deleted_at)",
        "values (#{senderId,jdbcType=BIGINT}, #{senderRoleType,jdbcType=SMALLINT,typeHandler=cn.xinzhili.xutils.mybatis.typehandlers.EnumCodeHandler}, ",
        "#{sessionId,jdbcType=BIGINT}, #{content,jdbcType=VARCHAR}, ",
        "#{messageType,jdbcType=SMALLINT,typeHandler=cn.xinzhili.xutils.mybatis.typehandlers.EnumCodeHandler}, ",
        "#{receiverId,jdbcType=BIGINT}, #{createdAt,jdbcType=TIMESTAMP}, ",
        "#{updatedAt,jdbcType=TIMESTAMP}, #{deletedAt,jdbcType=TIMESTAMP})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(Message record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    long insertSelectiveReturningId(Message record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    int insertSelective(Message record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    List<Message> selectByExample(MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    @Select({
        "select",
        "id, sender_id, sender_role_type, session_id, content, message_type, receiver_id, ",
        "created_at, updated_at, deleted_at",
        "from s_message.t_message",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("cn.xinzhili.chat.dao.MessageMapper.BaseResultMap")
    Message selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    int updateByExampleSelective(@Param("record") Message record, @Param("example") MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    int updateByExample(@Param("record") Message record, @Param("example") MessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    int updateByPrimaryKeySelective(Message record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    @Update({
        "update s_message.t_message",
        "set sender_id = #{senderId,jdbcType=BIGINT},",
          "sender_role_type = #{senderRoleType,jdbcType=SMALLINT,typeHandler=cn.xinzhili.xutils.mybatis.typehandlers.EnumCodeHandler},",
          "session_id = #{sessionId,jdbcType=BIGINT},",
          "content = #{content,jdbcType=VARCHAR},",
          "message_type = #{messageType,jdbcType=SMALLINT,typeHandler=cn.xinzhili.xutils.mybatis.typehandlers.EnumCodeHandler},",
          "receiver_id = #{receiverId,jdbcType=BIGINT},",
          "created_at = #{createdAt,jdbcType=TIMESTAMP},",
          "updated_at = #{updatedAt,jdbcType=TIMESTAMP},",
          "deleted_at = #{deletedAt,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Message record);
}