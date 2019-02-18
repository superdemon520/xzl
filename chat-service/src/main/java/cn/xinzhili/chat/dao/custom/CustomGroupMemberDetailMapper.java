package cn.xinzhili.chat.dao.custom;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.model.GroupMemberDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author by Loki on 17/5/18.
 */
public interface CustomGroupMemberDetailMapper {

  int updateUnread(@Param("sessionId") Long sessionId, @Param("senderId") Long senderId,
      @Param("senderRoleType") RoleType senderRoleType, @Param("receiverId") Long receiverId,
      @Param("count") int count);

  int cleanUnread(@Param("senderId") Long userId, @Param("roleType") RoleType roleType,
      @Param("sessionId") Long sessionId, @Param("receiverId") Long receiverId);


}
