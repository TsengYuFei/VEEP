package com.example.api.Service;

import com.example.api.Entity.User;
import com.example.api.Exception.BadRequestException;
import com.example.api.Other.JwtUtil;
import com.example.api.Repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.api.Other.GenerateCodeTool.generateRandomCode;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String myMail;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final UserHelperService userHelperService;



    public void sendVerificationEmail(String toEmail, String code, String userName){
        System.out.println("EmailService: sendVerificationEmail >> " + toEmail);
        String subject = "Please Verify Your Email Address for XBITURAL";
        String senderName = "XBITURAL Team";

        String content =
                "<div style='font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>"
                        + "<h2 style='color: #4CAF50; text-align: center;'>XBITURAL Email Verification</h2>"
                        + "<p>Dear <strong>" + userName + "</strong>,</p>"
                        + "<p>Thank you for joining <strong>XBITURAL</strong>!</p>"
                        + "<p>To complete your registration and activate your account, please verify your email address using the code below:</p>"
                        + "<div style='text-align: center; margin: 20px 0;'>"
                        + "    <span style='display: inline-block; font-size: 28px; font-weight: bold; color: #d9534f; letter-spacing: 4px; padding: 10px 20px; border: 2px dashed #d9534f; border-radius: 5px;'>" + code + "</span>"
                        + "</div>"
                        + "<p style='text-align: center; font-size: 14px; color: #777;'>Please complete verification within <strong>10 minutes</strong></p>"
                        + "<br>"
                        + "<p>If you did not create an XBITURAL account, you can safely ignore this email. Your account will not be activated without verification.</p>"
                        + "<p>If you have any questions or need assistance, feel free to contact our support team at any time.</p>"
                        + "<br>"
                        + "<p>Best regards,</p>"
                        + "<p><strong>The XBITURAL Team</strong></p>"
                        + "</div>";


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


    public void sendResetPasswordEmail(String toEmail, String code, String userName){
        System.out.println("EmailService: sendResetPasswordEmail >> " + toEmail);
        String subject = "Reset Your XBITURAL Password";
        String senderName = "XBITURAL Team";

        String content =
                "<div style='font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>"
                        + "<h2 style='color: #d9534f; text-align: center;'>XBITURAL Password Reset</h2>"
                        + "<p>Dear <strong>" + userName + "</strong>,</p>"
                        + "<p>We received a request to reset your <strong>XBITURAL</strong> account password.</p>"
                        + "<p>To reset your password, please use the code below:</p>"
                        + "<div style='text-align: center; margin: 20px 0;'>"
                        + "    <span style='display: inline-block; font-size: 28px; font-weight: bold; color: #4CAF50; letter-spacing: 4px; padding: 10px 20px; border: 2px dashed #4CAF50; border-radius: 5px;'>" + code + "</span>"
                        + "</div>"
                        + "<p style='text-align: center; font-size: 14px; color: #777;'>Please complete verification within <strong>30 minutes</strong></p>"
                        + "<br>"
                        + "<p>If you did not request this, you can safely ignore this email. Your password will remain unchanged.</p>"
                        + "<p>If you have any questions or need assistance, feel free to contact our support team at any time.</p>"
                        + "<br>"
                        + "<p>Best regards,</p>"
                        + "<p><strong>The XBITURAL Team</strong></p>"
                        + "</div>";


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


    public void resetPasswordEmail(String userAccountOrMail){
        System.out.println("EmailService: resetPasswordEmail >> "+userAccountOrMail);
        User user = userHelperService.getUserByAccountOrMail(userAccountOrMail);

        String resetCode = generateRandomCode(6);
        LocalDateTime deadline = LocalDateTime.now().plusMinutes(JwtUtil.RESET_PASSWORD_VALIDITY_MINUTES);

        user.setResetPasswordCode(resetCode);
        user.setResetPasswordDateline(deadline);
        userRepository.save(user);

        sendResetPasswordEmail(user.getMail(), resetCode, user.getName());
    }


    public void resendVerificationEmail(String userAccountOrMail){
        System.out.println("EmailService: resendVerificationEmail >> " + userAccountOrMail);
        User user = userHelperService.getUserByAccountOrMail(userAccountOrMail);
        if(user.getIsVerified()) throw new BadRequestException("User of user account or email < "+userAccountOrMail+" > has already been verified.");

        String verifyCode = generateRandomCode(6);
        LocalDateTime deadline = LocalDateTime.now().plusMinutes(JwtUtil.VERIFY_CODE_VALIDITY_MINUTES);

        user.setVerificationCode(verifyCode);
        user.setVerifyDateline(deadline);
        userRepository.save(user);

        sendVerificationEmail(user.getMail(), verifyCode, user.getName());
    }
}
