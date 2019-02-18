package cn.xinzhili.chat.model;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.UnreadStatus;
import cn.xinzhili.chat.api.UserStatus;
import java.time.LocalDateTime;

public class MessageSession {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_message_session.id
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_message_session.session_id
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    private Long sessionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_message_session.user_id
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_message_session.role_type
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    private RoleType roleType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_message_session.user_status
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    private UserStatus userStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_message_session.unread_status
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    private UnreadStatus unreadStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_message_session.created_at
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    private LocalDateTime createdAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_message_session.updated_at
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    private LocalDateTime updatedAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_message.t_message_session.deleted_at
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    private LocalDateTime deletedAt;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_message_session.id
     *
     * @return the value of s_message.t_message_session.id
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_message_session.id
     *
     * @param id the value for s_message.t_message_session.id
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_message_session.session_id
     *
     * @return the value of s_message.t_message_session.session_id
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public Long getSessionId() {
        return sessionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_message_session.session_id
     *
     * @param sessionId the value for s_message.t_message_session.session_id
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_message_session.user_id
     *
     * @return the value of s_message.t_message_session.user_id
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_message_session.user_id
     *
     * @param userId the value for s_message.t_message_session.user_id
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_message_session.role_type
     *
     * @return the value of s_message.t_message_session.role_type
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public RoleType getRoleType() {
        return roleType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_message_session.role_type
     *
     * @param roleType the value for s_message.t_message_session.role_type
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_message_session.user_status
     *
     * @return the value of s_message.t_message_session.user_status
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public UserStatus getUserStatus() {
        return userStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_message_session.user_status
     *
     * @param userStatus the value for s_message.t_message_session.user_status
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_message_session.unread_status
     *
     * @return the value of s_message.t_message_session.unread_status
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public UnreadStatus getUnreadStatus() {
        return unreadStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_message_session.unread_status
     *
     * @param unreadStatus the value for s_message.t_message_session.unread_status
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public void setUnreadStatus(UnreadStatus unreadStatus) {
        this.unreadStatus = unreadStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_message_session.created_at
     *
     * @return the value of s_message.t_message_session.created_at
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_message_session.created_at
     *
     * @param createdAt the value for s_message.t_message_session.created_at
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_message_session.updated_at
     *
     * @return the value of s_message.t_message_session.updated_at
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_message_session.updated_at
     *
     * @param updatedAt the value for s_message.t_message_session.updated_at
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_message.t_message_session.deleted_at
     *
     * @return the value of s_message.t_message_session.deleted_at
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_message.t_message_session.deleted_at
     *
     * @param deletedAt the value for s_message.t_message_session.deleted_at
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_message.t_message_session
     *
     * @mbggenerated Sat May 12 11:22:50 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sessionId=").append(sessionId);
        sb.append(", userId=").append(userId);
        sb.append(", roleType=").append(roleType);
        sb.append(", userStatus=").append(userStatus);
        sb.append(", unreadStatus=").append(unreadStatus);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append("]");
        return sb.toString();
    }
}