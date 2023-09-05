package com.heritart.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    private Session session;
    private String host;
    private String from;

    public MailSender(String fromAddress, String smtp, String password){
        this.from = fromAddress;
        this.host = smtp;

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };

        this.session = Session.getDefaultInstance(properties, auth);

    }

    public void send(String toAddress, String subject, String text){

        // session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
        }

        catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }
}
