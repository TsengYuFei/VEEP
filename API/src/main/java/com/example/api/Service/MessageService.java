package com.example.api.Service;

import com.example.api.DTO.Request.SendMessageRequest;
import com.example.api.DTO.Response.MessageResponse;
import com.example.api.Entity.Message;
import com.example.api.Entity.User;
import com.example.api.Repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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

        MessageResponse response = MessageResponse.fromMessage(message);

        System.out.println("WebSocket 推送到 /server/messages/" + receiver.getUserAccount());
        messagingTemplate.convertAndSend("/server/messages/"+receiver.getUserAccount(), response);
    }

    public List<MessageResponse> getConversation(String currentAccount, String targetAccount, Integer page, Integer size){
        System.out.println("MessageService: getConversation >> "+currentAccount+", "+targetAccount);
        singleUserService.getUserByAccount(targetAccount);

        Pageable pageable = PageRequest.of(page, size, Sort.by("send_at").descending());
        return messageRepository.findConversation(currentAccount, targetAccount, pageable)
                .stream()
                .map(MessageResponse::fromMessage)
                .toList();

    }
}
