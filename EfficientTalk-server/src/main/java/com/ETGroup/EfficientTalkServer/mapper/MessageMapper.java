package com.ETGroup.EfficientTalkServer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ETGroup.EfficientTalkServer.entity.PO.MessagePO;
import com.ETGroup.EfficientTalkServer.entity.response.message.MessageResponseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<MessagePO> {
    /**
     * 获取消息列表
     *
     * @param receiverId 消息接收者ID
     *
     * @return 消息列表
     */
    @Select("SELECT messages.*,users.avatar as avatar,users.username as name FROM messages,users WHERE " +
                    "messages.receiver = #{receiverId} AND messages.receiver = users.user_id")
    List<MessageResponseVO> getMessageListByReceiverId(@Param("receiverId") Long receiverId);
}
