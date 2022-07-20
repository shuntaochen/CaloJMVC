package org.caloch.core;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class MailSender {
    public void doSend() {
        // 收件人电子邮箱
        String to = "xxx@qq.com";

        // 发件人电子邮箱
        String from = "xxx@qq.com";

        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com";  //QQ 邮件服务器

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("xxx@qq.com", "qq邮箱授权码"); //发件人邮件用户名、授权码
            }
        });

        try {
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头部头字段
            message.setSubject("This is the Subject Line!");
            // 发送 HTML 消息, 可以插入html标签
            message.setContent("<h1>This is actual message</h1>",
                    "text/html");

            // 设置消息体
            message.setText("This is actual message");
            {

                // 创建多重消息
                Multipart multipart = new MimeMultipart();

                // 创建消息部分
                BodyPart messageBodyPart = new MimeBodyPart();
                // 设置文本消息部分
                multipart.addBodyPart(messageBodyPart);

                // 附件部分
                messageBodyPart = new MimeBodyPart();
                String filename = "file.txt";
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
            }

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....from ");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}