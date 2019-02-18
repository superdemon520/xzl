package cn.xinzhili.chat.dao;

import cn.xinzhili.chat.model.MemberStatus;
import cn.xinzhili.chat.model.MemberStatusExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface MemberStatusMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    int countByExample(MemberStatusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    int deleteByExample(MemberStatusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    @Update({
        "update s_message.t_member_status",
        "set deleted_at = now()",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    @Select({
        "insert into s_message.t_member_status (doctor_id, assistant_id, ",
        "assistant_status, ",
        "created_at, updated_at, ",
        "deleted_at)",
        "values (#{doctorId,jdbcType=BIGINT}, #{assistantId,jdbcType=BIGINT}, ",
        "#{assistantStatus,jdbcType=SMALLINT,typeHandler=cn.xinzhili.xutils.mybatis.typehandlers.EnumCodeHandler}, ",
        "#{createdAt,jdbcType=TIMESTAMP}, #{updatedAt,jdbcType=TIMESTAMP}, ",
        "#{deletedAt,jdbcType=TIMESTAMP})", 
        " returning id"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    long insertReturningId(MemberStatus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    @Insert({
        "insert into s_message.t_member_status (doctor_id, assistant_id, ",
        "assistant_status, ",
        "created_at, updated_at, ",
        "deleted_at)",
        "values (#{doctorId,jdbcType=BIGINT}, #{assistantId,jdbcType=BIGINT}, ",
        "#{assistantStatus,jdbcType=SMALLINT,typeHandler=cn.xinzhili.xutils.mybatis.typehandlers.EnumCodeHandler}, ",
        "#{createdAt,jdbcType=TIMESTAMP}, #{updatedAt,jdbcType=TIMESTAMP}, ",
        "#{deletedAt,jdbcType=TIMESTAMP})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(MemberStatus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    long insertSelectiveReturningId(MemberStatus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    int insertSelective(MemberStatus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    List<MemberStatus> selectByExample(MemberStatusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    @Select({
        "select",
        "id, doctor_id, assistant_id, assistant_status, created_at, updated_at, deleted_at",
        "from s_message.t_member_status",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("cn.xinzhili.chat.dao.MemberStatusMapper.BaseResultMap")
    MemberStatus selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    int updateByExampleSelective(@Param("record") MemberStatus record, @Param("example") MemberStatusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    int updateByExample(@Param("record") MemberStatus record, @Param("example") MemberStatusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    int updateByPrimaryKeySelective(MemberStatus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_member_status
     *
     * @mbggenerated Tue May 15 10:37:13 CST 2018
     */
    @Update({
        "update s_message.t_member_status",
        "set doctor_id = #{doctorId,jdbcType=BIGINT},",
          "assistant_id = #{assistantId,jdbcType=BIGINT},",
          "assistant_status = #{assistantStatus,jdbcType=SMALLINT,typeHandler=cn.xinzhili.xutils.mybatis.typehandlers.EnumCodeHandler},",
          "created_at = #{createdAt,jdbcType=TIMESTAMP},",
          "updated_at = #{updatedAt,jdbcType=TIMESTAMP},",
          "deleted_at = #{deletedAt,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MemberStatus record);
}