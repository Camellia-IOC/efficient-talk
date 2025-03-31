// 聊天对象信息
export class ChatInfoObject {
    userId;
    userName;
    userAvatar;
    isGroup;
}

// 聊天列表对象信息
export class ChatListObject {
    userId;
    userName;
    userAvatar;
    unreadCount;
    isGroup;
    creator;
    lastMessage;
    lastMessageTime;
}