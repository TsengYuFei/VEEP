package com.example.api.Service;

import com.example.api.DTO.Request.SendMessageRequest;
import com.example.api.DTO.Response.SendMessageResponse;
import com.example.api.Entity.Message;
import com.example.api.Entity.User;
import com.example.api.Repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    private final SingleUserService singleUserService;

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;



    public void sendMessage(String senderAccount, SendMessageRequest request){
        System.out.println("MessageService: sendMessage >> "+senderAccount);
        User sender = singleUserService.getUserByAccount(senderAccount);
        User receiver = singleUserService.getUserByAccount(request.getReceiverAccount());

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessage(request.getMessage());
        message.setIsRead(false);
        messageRepository.save(message);

        SendMessageResponse response = SendMessageResponse.fromMessage(message);

        System.out.println("WebSocket 推送到 /server/messages/" + receiver.getUserAccount());
        messagingTemplate.convertAndSend("/server/messages/"+receiver.getUserAccount(), response);
    }
}
