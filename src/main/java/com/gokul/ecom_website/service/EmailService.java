package com.gokul.ecom_website.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public  void sendEmailwithToken(String sendEmail,String token) throws MessagingException {
        String resetUrl="http://localhost:4200/reset-password?token="+token;
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper message=new MimeMessageHelper(mimeMessage);
        message.setTo(sendEmail);
        message.setFrom("no-reply.ecom.com");
        message.setSubject("Reset your password");

        String html="<p>please click the  link to reset password <br> <a href=\""+resetUrl+"\">"+"link"+"</a></p>";
        message.setText(html,true);
        javaMailSender.send(mimeMessage);
    }


}
