package com.example.api.Service;

import com.example.api.DTO.Request.SendUserMessageRequest;
import com.example.api.DTO.Response.MessageListResponse;
import com.example.api.DTO.Response.UserMessageResponse;
import com.example.api.DTO.Response.UnreadMessageResponse;
import com.example.api.Entity.UserMessage;
import com.example.api.Entity.User;
import com.example.api.Repository.UserMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMessageService {
    @Autowired
    private final UserMessageRepository userMessageRepository;

    @Autowired
    private final UserHelperService userHelperService;

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;



    @Transactional
    public void sendMessage(String senderAccount, SendUserMessageRequest request){
        System.out.println("MessageService: sendMessage >> "+senderAccount);
        User sender = userHelperService.getUserByAccount(senderAccount);
        User receiver = userHelperService.getUserByAccount(request.getReceiverAccount());
        String receiverAccount = receiver.getUserAccount();

        UserMessage userMessage = new UserMessage();
        userMessage.setSender(sender);
        userMessage.setReceiver(receiver);
        userMessage.setMessage(request.getMessage());
        userMessage.setIsRead(false);
        userMessageRepository.save(userMessage);

        UserMessageResponse response = UserMessageResponse.fromUserMessage(userMessage);

        System.out.println("WebSocket 推送到 /server/user/messages/" + receiverAccount);
        messagingTemplate.convertAndSend("/server/user/messages/"+receiverAccount, response);

        UnreadMessageResponse unreadResponse = getUnreadCount(senderAccount, receiverAccount);
        System.out.println("WebSocket 推送到 /server/user/unread/" + receiverAccount);
        messagingTemplate.convertAndSend("/server/user/unread/"+receiverAccount, unreadResponse);
    }


    public List<UserMessageResponse> getConversation(String currentAccount, String targetAccount, Integer page, Integer size){
        System.out.println("MessageService: getConversation >> "+currentAccount+", "+targetAccount);
        userHelperService.getUserByAccount(targetAccount);

        Pageable pageable = PageRequest.of(page, size, Sort.by("send_at").descending());
        return userMessageRepository.findConversation(currentAccount, targetAccount, pageable)
                .stream()
                .map(UserMessageResponse::fromUserMessage)
                .toList();

    }


    public UnreadMessageResponse getUnreadCount(String currentAccount, String targetAccount){
        System.out.println("MessageService: getUnreadCount >> "+currentAccount+", "+targetAccount);
        userHelperService.getUserByAccount(targetAccount);

        Integer count = userMessageRepository.getUnreadCountByAccount(currentAccount, targetAccount);
        return new UnreadMessageResponse(targetAccount, count);
    }


    @Transactional
    public void readUnread(String currentAccount, String targetAccount){
        System.out.println("MessageService: getUnreadMessage >> "+currentAccount+", "+targetAccount);
        User receiver = userHelperService.getUserByAccount(targetAccount);
        userMessageRepository.readUnreadByAccount(currentAccount, targetAccount);

        UnreadMessageResponse unreadResponse = getUnreadCount(targetAccount, currentAccount);
        System.out.println("WebSocket 推送到 /server/user/unread/" + receiver.getUserAccount());
        messagingTemplate.convertAndSend("/server/user/unread/"+receiver.getUserAccount(), unreadResponse);
    }


    public List<MessageListResponse> getChatList(String userAccount, Integer page, Integer size){
        System.out.println("MessageService: getChatList >> "+userAccount);
        Pageable pageable = PageRequest.of(page, size, Sort.by("latestTime").descending());
        return userMessageRepository.getChatList(userAccount, pageable)
                .stream()
                .map(MessageListResponse::fromMessageView)
                .toList();
    }
}
