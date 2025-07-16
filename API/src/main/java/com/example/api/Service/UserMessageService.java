package com.example.api.Service;

import com.example.api.DTO.Request.SendMessageRequest;
import com.example.api.DTO.Response.MessageListResponse;
import com.example.api.DTO.Response.MessageResponse;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMessageService {
    @Autowired
    private final UserMessageRepository userMessageRepository;

    @Autowired
    private final SingleUserService singleUserService;

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;



    public void sendMessage(String senderAccount, SendMessageRequest request){
        System.out.println("MessageService: sendMessage >> "+senderAccount);
        User sender = singleUserService.getUserByAccount(senderAccount);
        User receiver = singleUserService.getUserByAccount(request.getReceiverAccount());

        UserMessage userMessage = new UserMessage();
        userMessage.setSender(sender);
        userMessage.setReceiver(receiver);
        userMessage.setMessage(request.getMessage());
        userMessage.setIsRead(false);
        userMessageRepository.save(userMessage);

        MessageResponse response = MessageResponse.fromMessage(userMessage);

        System.out.println("WebSocket 推送到 /server/messages/" + receiver.getUserAccount());
        messagingTemplate.convertAndSend("/server/messages/"+receiver.getUserAccount(), response);

        String receiverAccount = receiver.getUserAccount();
        UnreadMessageResponse unreadResponse = getUnreadCount(senderAccount, receiverAccount);
        System.out.println("WebSocket 推送到 /server/unread/" + receiver.getUserAccount());
        messagingTemplate.convertAndSend("/server/unread/"+receiver.getUserAccount(), unreadResponse);
    }


    public List<MessageResponse> getConversation(String currentAccount, String targetAccount, Integer page, Integer size){
        System.out.println("MessageService: getConversation >> "+currentAccount+", "+targetAccount);
        singleUserService.getUserByAccount(targetAccount);

        Pageable pageable = PageRequest.of(page, size, Sort.by("send_at").descending());
        return userMessageRepository.findConversation(currentAccount, targetAccount, pageable)
                .stream()
                .map(MessageResponse::fromMessage)
                .toList();

    }


    public UnreadMessageResponse getUnreadCount(String currentAccount, String targetAccount){
        System.out.println("MessageService: getUnreadCount >> "+currentAccount+", "+targetAccount);
        singleUserService.getUserByAccount(targetAccount);

        Integer count = userMessageRepository.getUnreadCountByAccount(currentAccount, targetAccount);
        return new UnreadMessageResponse(targetAccount, count);
    }


    public void readUnread(String currentAccount, String targetAccount){
        System.out.println("MessageService: getUnreadMessage >> "+currentAccount+", "+targetAccount);
        User receiver = singleUserService.getUserByAccount(targetAccount);
        userMessageRepository.readUnreadByAccount(currentAccount, targetAccount);

        UnreadMessageResponse unreadResponse = getUnreadCount(targetAccount, currentAccount);
        System.out.println("WebSocket 推送到 /server/unread/" + receiver.getUserAccount());
        messagingTemplate.convertAndSend("/server/unread/"+receiver.getUserAccount(), unreadResponse);
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
