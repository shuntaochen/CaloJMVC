package org.caloch.core;

import org.caloch.beans.Roles;
import org.caloch.utils.MySqlDbContext;
import org.caloch.utils.PropertyUtil;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class MailSender {
    MySqlDbContext db;
    String mailAccount;
    String mailKey;

    public MailSender() {
        PropertyUtil propertyUtil = new PropertyUtil();
        this.mailAccount = propertyUtil.getMailAccount();
        this.mailKey = propertyUtil.getMailKey();
        this.db = new MySqlDbContext(propertyUtil.getDbUrl(), propertyUtil.getDbUser(), propertyUtil.getDbPassword());
    }


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

    private AtomicInteger couter = new AtomicInteger(0);
    private AtomicInteger total = new AtomicInteger();

    public void doSend() {
        db.connect();
        {
            ExecutorService service = new ThreadPoolExecutor(2, 20,
                    60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue(200));
            total.set(50);
            for (int i = 0; i < total.get(); i++) {//获取待发邮件，发送后设置为已发送
                service.execute(() -> {
                    //Object i=getby counter
                    int cur = couter.addAndGet(1);//不一定成功，进来就算+1，
                    try {
                        Roles r = new Roles();
                        r.setId(2);
                        send(mailAccount, mailKey, "from", "to", "Calo News Daily", "<h1>Hello</h1>", "Best regards");
                        System.out.println(String.format("thread email:%s", Thread.currentThread().getName()));
                        Object ret = db.select(r);
                        Object a = ret;
                        db.commit(false);//commit to change to sent for mail.

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }finally {
                        synchronized (this) {
                            if (total.get() == cur)//肯定会执行
                                db.commit(true);
                        }
                    }

                });
            }
            service.shutdown();
        }
    }


}
