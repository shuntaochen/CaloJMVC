package org.caloch.core;

import org.caloch.utils.MySqlDbContext;
import org.caloch.utils.PropertyUtil;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class MailSender {

    private void send(String account, String key, String from, String to, String subject, String contentHtml, String text) {
        String host = "smtp.qq.com";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, key);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(contentHtml,
                    "text/html");
            message.setText(text);
            if (false) {
                Multipart multipart = new MimeMultipart();
                BodyPart messageBodyPart = new MimeBodyPart();
                multipart.addBodyPart(messageBodyPart);
                messageBodyPart = new MimeBodyPart();
                String filename = "file.txt";
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);
            }
            Transport.send(message);
            System.out.println("Sent message successfully....from ");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void doSend() {
        PropertyUtil propertyUtil = new PropertyUtil();
        MySqlDbContext db = new MySqlDbContext(propertyUtil.getDbUrl(), propertyUtil.getDbUser(), propertyUtil.getDbPassword());
        db.connect();
        {
            ExecutorService service = new ThreadPoolExecutor(2, 20,
                    60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue(200));
            for (int i = 0; i < 5; i++) {//获取待发邮件，发送后设置为已发送
                service.execute(() -> {
                    send(propertyUtil.getMailAccount(), propertyUtil.getMailKey(), "from", "to", "Calo News Daily", "<h1>Hello</h1>", "Best regards");
                    db.commit(false);//commit to change to sent for mail.
                    System.out.println(String.format("thread name:%s", Thread.currentThread().getName()));
                });
            }
            service.shutdown();
        }
        db.commit(true);//commit and close conn /or rollback,
    }


}
