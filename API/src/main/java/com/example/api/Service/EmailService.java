package com.example.api.Service;

import com.example.api.Entity.User;
import com.example.api.Exception.BadRequestException;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${app.site-url}")
    private String siteURL;

    @Value("${spring.mail.username}")
    private String myMail;

    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private final UserRepository userRepository;



    public void sendVerificationEmail(String toEmail, String code, String userName){
        System.out.println("EmailService: sendVerificationEmail >> " + toEmail);
        String subject = "Please Verify Your Email Address for XBITURAL";
        String senderName = "XBITURAL Team";
        String verifyURL = siteURL + "/user/verify?code=" + code;

        String content = "<p>Dear " + userName + ",</p>"
                + "<p>Thank you for joining <strong>XBITURAL</strong>!</p>"
                + "<p>To complete your registration and activate your account, please verify your email address by clicking the link below:</p>"
                + "<p><a href=\"" + verifyURL + "\">Verify My Account</a></p>"
                + "<br>"
                + "<p>If you did not create an XBITURAL account, you can safely ignore this email. Your account will not be activated without verification.</p>"
                + "<p>If you have any questions or need assistance, feel free to contact our support team at any time.</p>"
                + "<br>"
                + "<p>Best regards,</p>"
                + "<p>The XBITURAL Team</p>";

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


    public void sendResetPasswordEmail(String toEmail, String token, String userName){
        System.out.println("EmailService: sendResetPasswordEmail >> " + toEmail);
        String subject = "Reset Your XBITURAL Password";
        String senderName = "XBITURAL Team";
        String resetURL = siteURL + "/user/reset_password?token=" + token;

        String content = "<p>Dear " + userName + ",</p>"
                + "<p>We received a request to reset your <strong>XBITURAL</strong> account password.</p>"
                + "<p>To reset your password, click the link below:</p>"
                + "<p><a href=\"" + resetURL + "\">Reset My Password</a></p>"
                + "<br>"
                + "<p>If you did not request this, please ignore this email. This link will expire shortly.</p>"
                + "<br>"
                + "<p>Best regards,</p>"
                + "<p>The XBITURAL Team</p>";

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



    public void resetPasswordEmail(String account){
        System.out.println("EmailService: resetPasswordEmail >> "+account);
        User user = userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));

        String randomCode = UUID.randomUUID().toString();
        user.setResetPasswordToken(randomCode);
        userRepository.save(user);
        sendResetPasswordEmail(user.getMail(), randomCode, user.getName());
    }


    public void resendVerificationEmail(String account){
        System.out.println("EmailService: resendVerificationEmail >> " + account);
        User user = userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
        if(user.getIsVerified()) throw new BadRequestException("User of user account < "+account+" > has already been verified.");

        String randomCode = UUID.randomUUID().toString();
        user.setVerificationCode(randomCode);
        userRepository.save(user);
        sendVerificationEmail(user.getMail(), randomCode, user.getName());
    }
}
