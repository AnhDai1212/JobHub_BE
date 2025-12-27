package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.services.EmailService;
import com.daita.datn.services.OtpService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final OtpService otpService;
    private final SpringTemplateEngine springTemplateEngine;
    @Value("${MAIL_USERNAME}")
    private String fromEmail;
    public static final String TITLE_SUBJECT_EMAIL = "Verification code notification";

    @Override
    public boolean sendOtpByEmail(String email) {
        String otp = otpService.generateOtp(email);
        try {
            Context context = new Context();
            context.setVariable("otp", otp);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(TITLE_SUBJECT_EMAIL);

            String htmlContent = springTemplateEngine.process("EmailTemplate", context);
            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.MAIL_SEND_FAILED);
        }
    }
}