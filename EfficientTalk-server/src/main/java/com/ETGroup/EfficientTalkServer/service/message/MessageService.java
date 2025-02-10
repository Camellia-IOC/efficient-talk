package com.ETGroup.EfficientTalkServer.service.message;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ETGroup.EfficientTalkServer.entity.PO.MessagePO;
import com.ETGroup.EfficientTalkServer.entity.message.request.CreateMessageQueryParam;
import com.ETGroup.EfficientTalkServer.entity.message.request.GetMessageListQueryParam;
import com.ETGroup.EfficientTalkServer.entity.message.response.MessageListResponseVO;
import org.springframework.stereotype.Service;

@Service
public interface MessageService extends IService<MessagePO> {
    /**
     * 获取消息列表
     *
     * @param request 请求参数
     *
     * @return 消息列表
     */
    MessageListResponseVO getMessageList (GetMessageListQueryParam param);
    
    /**
     * 创建新消息
     *
     * @param request 请求参数
     *
     * @return 是否成功创建
     */
    boolean createMessage (CreateMessageQueryParam param);
    
    /**
     * 创建注册消息
     *
     * @param userId   用户ID
     * @param username 用户名
     *
     * @return 注册消息
     */
    boolean createRegisterMessage (String userId, String username);
    
    /**
     * 创建关注消息
     *
     * @param userId     用户ID
     * @param followerId 粉丝ID
     *
     * @return 是否成功创建关注消息
     */
    boolean createFollowMessage (String userId, String followerId);
    
    /**
     * 获取注册成功消息内容
     *
     * @param userId   用户ID
     * @param username 用户名
     *
     * @return 注册成功消息内容
     */
    String getRegisterMessage (String userId, String username);
}
