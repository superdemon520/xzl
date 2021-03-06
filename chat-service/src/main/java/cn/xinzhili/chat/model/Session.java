package cn.xinzhili.chat.model;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.SessionType;
import cn.xinzhili.chat.api.Type;
import java.time.LocalDateTime;

public class Session {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_session.id
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_session.initiator_id
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    private Long initiatorId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_session.initiator_role_type
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    private RoleType initiatorRoleType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_session.organization_id
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    private Long organizationId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_session.type
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    private Type type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_session.session_type
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    private SessionType sessionType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_session.created_at
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    private LocalDateTime createdAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_session.updated_at
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    private LocalDateTime updatedAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_session.deleted_at
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    private LocalDateTime deletedAt;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_session.id
     *
     * @return the value of s_message.t_session.id
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_session.id
     *
     * @param id the value for s_message.t_session.id
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_session.initiator_id
     *
     * @return the value of s_message.t_session.initiator_id
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public Long getInitiatorId() {
        return initiatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_session.initiator_id
     *
     * @param initiatorId the value for s_message.t_session.initiator_id
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public void setInitiatorId(Long initiatorId) {
        this.initiatorId = initiatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_session.initiator_role_type
     *
     * @return the value of s_message.t_session.initiator_role_type
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public RoleType getInitiatorRoleType() {
        return initiatorRoleType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_session.initiator_role_type
     *
     * @param initiatorRoleType the value for s_message.t_session.initiator_role_type
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public void setInitiatorRoleType(RoleType initiatorRoleType) {
        this.initiatorRoleType = initiatorRoleType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_session.organization_id
     *
     * @return the value of s_message.t_session.organization_id
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public Long getOrganizationId() {
        return organizationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_session.organization_id
     *
     * @param organizationId the value for s_message.t_session.organization_id
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_session.type
     *
     * @return the value of s_message.t_session.type
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public Type getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_session.type
     *
     * @param type the value for s_message.t_session.type
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_session.session_type
     *
     * @return the value of s_message.t_session.session_type
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public SessionType getSessionType() {
        return sessionType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_session.session_type
     *
     * @param sessionType the value for s_message.t_session.session_type
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_session.created_at
     *
     * @return the value of s_message.t_session.created_at
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_session.created_at
     *
     * @param createdAt the value for s_message.t_session.created_at
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_session.updated_at
     *
     * @return the value of s_message.t_session.updated_at
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_session.updated_at
     *
     * @param updatedAt the value for s_message.t_session.updated_at
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_session.deleted_at
     *
     * @return the value of s_message.t_session.deleted_at
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_session.deleted_at
     *
     * @param deletedAt the value for s_message.t_session.deleted_at
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_session
     *
     * @mbggenerated Tue May 08 17:07:36 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", initiatorId=").append(initiatorId);
        sb.append(", initiatorRoleType=").append(initiatorRoleType);
        sb.append(", organizationId=").append(organizationId);
        sb.append(", type=").append(type);
        sb.append(", sessionType=").append(sessionType);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append("]");
        return sb.toString();
    }
}