package com.example.api.Service;

import com.example.api.DTO.Request.SendExpoGroupMessageRequest;
import com.example.api.DTO.Response.ExpoGroupMessageResponse;
import com.example.api.Entity.Expo;
import com.example.api.Entity.ExpoGroupMessage;
import com.example.api.Entity.User;
import com.example.api.Repository.ExpoGroupMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpoGroupMessageService {
    private final ExpoGroupMessageRepository expoGroupMessageRepository;
    private final UserHelperService userHelperService;
    private final ExpoHelperService expoHelperService;
    private final SimpMessagingTemplate messagingTemplate;



    @Transactional
    public void sendGroupMessage(Integer expoID, String account, SendExpoGroupMessageRequest request){
        System.out.println("ExpoGroupMessageService: sendGroupMessage >> "+expoID+", "+account);
        User sender = userHelperService.getUserByAccount(account);
        Expo expo = expoHelperService.getExpoByID(expoID);

        ExpoGroupMessage message = new ExpoGroupMessage();
        message.setSender(sender);
        message.setExpo(expo);
        message.setMessage(request.getMessage());
        expoGroupMessageRepository.save(message);

        ExpoGroupMessageResponse response = ExpoGroupMessageResponse.fromExpoGroupMessage(message);

        System.out.println("WebSocket 推送到 /server/group/messages/" + expoID);
        messagingTemplate.convertAndSend("/server/group/messages/"+expoID, response);

    }


    public List<ExpoGroupMessageResponse> getConversation(Integer expoID, String account, Integer page, Integer size){
        System.out.println("ExpoGroupMessageService: getConversation >> "+expoID+", "+account);
        userHelperService.getUserByAccount(account);
        expoHelperService.getExpoByID(expoID);

        Pageable pageable = PageRequest.of(page, size, Sort.by("send_at").descending());
        return expoGroupMessageRepository.findConversation(expoID, pageable)
                .stream()
                .map(ExpoGroupMessageResponse::fromExpoGroupMessage)
                .toList();

    }


    @Transactional
    public void deleteMessageByExpoID(Integer expoID){
        System.out.println("ExpoGroupMessageService: deleteMessageByExpoID >> "+expoID);
        expoHelperService.getExpoByID(expoID);
        expoGroupMessageRepository.deleteByExpo_ExpoID(expoID);
    }
}
