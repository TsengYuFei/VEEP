package com.example.api.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${app.site-url}")
    private String siteURL;

    @Value("${spring.mail.username}")
    private String myMail;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String code){
        System.out.println("EmailService: sendEmail >> "+toEmail);
        String subject = "請驗證您的信箱";
        String senderName = "VEEP";
        String verifyURL = siteURL + "/user/verify?code=" + code;

        String content = "<p>您好,</p>"
                + "<p>請點以下連結完成驗證:</p>"
                + "<a href=\"" + verifyURL + "\">驗證我的帳號</a>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(myMail, senderName);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
