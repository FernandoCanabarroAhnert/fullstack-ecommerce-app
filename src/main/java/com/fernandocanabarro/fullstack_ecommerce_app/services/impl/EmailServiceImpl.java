package com.fernandocanabarro.fullstack_ecommerce_app.services.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.fernandocanabarro.fullstack_ecommerce_app.services.EmailService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private SendGrid sendGrid;

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    private String sendgridEmail = "ahnertfernando499@gmail.com";

    @Async
    public void sendEmail(Mail mail){
        Request request = new Request();
        try{
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            if (response.getStatusCode() >= 400) {
                throw new BadRequestException("Falha ao enviar ao e-mail");
            }
        }
        catch (IOException e){
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Mail createConfirmationEmailTemplate(String username,String email, String code){
        Map<String,Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("activation_code", code);
        variables.put("baseUrl",baseUrl);

        Context context = new Context();
        context.setVariables(variables);
        
        String template = springTemplateEngine.process("email/activation-code", context);

        Email emailTo = new Email(email);
        Email from = new Email(sendgridEmail,"Projeto Full-Stack");
        Content content = new Content("text/html", template);

        return new Mail(from, "Ativação de Conta", emailTo, content);
    }

    @Override
    public Mail createPasswordRecoverTemplate(String username,String email, String code){
        Map<String,Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("password_recover_code", code);
        variables.put("baseUrl",baseUrl);

        Context context = new Context();
        context.setVariables(variables);
        
        String template = springTemplateEngine.process("email/password-recover", context);

        Email emailTo = new Email(email);
        Email from = new Email(sendgridEmail,"Projeto Full-Stack");
        Content content = new Content("text/html", template);

        return new Mail(from, "Recuperação de Senha", emailTo, content);
    }

    public void sendOrderSummaryEmail(String username, String email, Long orderId) {
        Map<String,Object> variables = Map.of(
            "username",username,
            "orderId", orderId,
            "baseUrl",baseUrl
        );
        Context context = new Context();
        context.setVariables(variables);

        String template = springTemplateEngine.process("email/order-summary", context);

        Email emailTo = new Email(email);
        Email from = new Email(sendgridEmail,"Projeto Full-Stack");
        Content content = new Content("text/html", template);

        Mail mail = new Mail(from, "Resumo de Pedido", emailTo, content);

        sendEmail(mail);
    }
}
