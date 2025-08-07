package com.example.api.Service;

import com.example.api.DTO.Request.SendExpoGroupMessageRequest;
import com.example.api.DTO.Response.ExpoGroupMessageResponse;
import com.example.api.Entity.Expo;
import com.example.api.Entity.ExpoGroupMessage;
import com.example.api.Entity.User;
import com.example.api.Repository.ExpoGroupMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpoGroupMessageService {
    @Autowired
    private final ExpoGroupMessageRepository expoGroupMessageRepository;

    @Autowired
    private final UserHelperService userHelperService;

    @Autowired
    private final ExpoHelperService expoHelperService;

    @Autowired
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
}
