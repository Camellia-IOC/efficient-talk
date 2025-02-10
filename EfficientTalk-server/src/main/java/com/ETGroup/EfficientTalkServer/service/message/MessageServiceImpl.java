package com.ETGroup.EfficientTalkServer.service.message;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ETGroup.EfficientTalkServer.config.ENUM.BasicMessageTitleManager;
import com.ETGroup.EfficientTalkServer.entity.PO.MessagePO;
import com.ETGroup.EfficientTalkServer.entity.request.message.CreateMessageQueryParam;
import com.ETGroup.EfficientTalkServer.entity.request.message.GetMessageListQueryParam;
import com.ETGroup.EfficientTalkServer.entity.response.message.MessageListResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessagePO> implements MessageService {
    @Resource
    private MessageMapper messageMapper;
    
    /**
     * 获取消息列表
     *
     * @param param 请求参数
     *
     * @return 消息列表
     */
    @Override
    public MessageListResponseVO getMessageList (GetMessageListQueryParam param) {
        MessageListResponseVO response = new MessageListResponseVO();
        response.setMessageList(messageMapper.getMessageListByReceiverId(param.getId()));
        
        return response;
    }
    
    /**
     * 创建新消息
     *
     * @param param 请求参数
     *
     * @return 是否成功创建
     */
    @Override
    public boolean createMessage (CreateMessageQueryParam param) {
        MessagePO message = new MessagePO();
        message.setType(param.getType());
        message.setOrigin(param.getOrigin());
        message.setReceiver(param.getReceiver());
        message.setTitle(param.getTitle());
        message.setContent(param.getContent());
        message.setCreateTime(LocalDateTime.now());
        message.setReadFlag(0);
        
        return messageMapper.insert(message) == 1;
    }
    
    /**
     * 创建注册消息
     *
     * @param userId   用户ID
     * @param username 用户名
     *
     * @return 注册消息
     */
    @Override
    public boolean createRegisterMessage (String userId, String username) {
        CreateMessageQueryParam message = new CreateMessageQueryParam();
        message.setType(0);
        message.setOrigin("0");
        message.setReceiver(userId);
        message.setTitle(BasicMessageTitleManager.TITLE_REGISTER_MESSAGE.getTitle());
        message.setContent(this.getRegisterMessage(userId, username));
        
        return createMessage(message);
    }
    
    /**
     * 创建关注消息
     *
     * @param userId     用户ID
     * @param followerId 粉丝ID
     *
     * @return 是否成功创建关注消息
     */
    @Override
    public boolean createFollowMessage (String userId, String followerId) {
        CreateMessageQueryParam message = new CreateMessageQueryParam();
        message.setType(0);
        message.setOrigin(followerId);
        message.setReceiver(userId);
        message.setTitle(BasicMessageTitleManager.TITLE_FOLLOW_MESSAGE.getTitle());
        message.setContent("你有新的粉丝啦，快来看看吧！");
        
        return createMessage(message);
    }
    
    
    /**
     * 获取注册成功消息内容
     *
     * @param userId   用户ID
     * @param username 用户名
     *
     * @return 注册成功消息内容
     */
    @Override
    public String getRegisterMessage (String userId, String username) {
        return "Hello " +
                       username +
                       "!" +
                       "欢迎你加入我们的大家庭,你的ID是：" +
                       userId +
                       ",请注意保存噢~";
    }
}