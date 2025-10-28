package com.bank.System_V1.services.impl;

import com.bank.System_V1.dto.EmailDetails;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(emailDetails.getRecipient());
            message.setText(emailDetails.getMessage());
            message.setSubject(emailDetails.getSubject());
            mailSender.send(message);
            System.out.println("Mail sent successfully");
        }catch (MailException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmailWithAttachment(EmailDetails emailDetails) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper ;
        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            mimeMessageHelper.setText(emailDetails.getMessage(),true);

            FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
            mimeMessageHelper.addAttachment(file.getFilename(),file);
            mailSender.send(mimeMessage);
            log.info(file.getFilename() + "Mail sent successfully to user with email : " + emailDetails.getRecipient());
        }catch (Exception e){
             throw new RuntimeException(e);
        }

    }
}
