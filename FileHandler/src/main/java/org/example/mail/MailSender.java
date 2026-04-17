package org.example.mail;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.*;

import java.util.Properties;
import java.util.logging.Logger;

public class MailSender {

    private static final Logger logger = Logger.getLogger(MailSender.class.getName());

    public static void sendMail(String encFile) {
        logger.info("Попытка отправки файла !!!");

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "127.0.0.1");
            props.put("mail.smtp.port", "1025");

            Session session = Session.getInstance(props);

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("monitor@gpa.local"));
            msg.setRecipient(Message.RecipientType.TO,
                    new InternetAddress("operator@gpa.local"));
            msg.setSubject("Зашифрованный срез данных");

            MimeMultipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Зашифрованный файл данных во вложении.");

            MimeBodyPart filePart = new MimeBodyPart();
            filePart.setDataHandler(new DataHandler(new FileDataSource(encFile)));
            filePart.setFileName("snapshot.enc");

            multipart.addBodyPart(textPart);
            multipart.addBodyPart(filePart);
            msg.setContent(multipart);

            Transport.send(msg);
            logger.info("Письмо отправлено успешно на 127.0.0.1:1025");

        } catch (Exception e) {
            logger.warning("Отправка не удалась (сервер не поднят): " + e.getMessage());
        }
    }
}
