package utils;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String senderMail = dotenv.get("SENDER_MAIL");
    private static final String recipientMail = dotenv.get("RECIPIENT_MAIL");
    private static final String appPasswordMail = dotenv.get("APP_PASSWORD_MAIL");

    private EmailUtils() {
    }

    public static void sendMailReport(String reportPath) {
        try {
            Message message = new MimeMessage(getMailSession());

            // Mail infos
            message.setFrom(new InternetAddress(senderMail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientMail));
            message.setSubject("[VirtuoFormation - Rapport de test d'IHM automatique]");

            // Text body
            MimeBodyPart mimeBodyText = new MimeBodyPart();
            mimeBodyText.setText(getTextMessage(), "utf-8", "html");

            // Attached file
            MimeBodyPart mimeBodyAttach = new MimeBodyPart();
            mimeBodyAttach.attachFile(new File(System.getProperty("user.dir") + "/" + reportPath));

            // Build mail object
            MimeMultipart mailBody = new MimeMultipart();
            mailBody.addBodyPart(mimeBodyText);
            mailBody.addBodyPart(mimeBodyAttach);

            message.setContent(mailBody);
            Transport.send(message);

            Log.info("[Mailer] Report mail send successfully");

        } catch (Exception e) {
            Log.error("[Mailer] Can't send report mail");
        }
    }

    private static Session getMailSession() {
        // SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderMail, appPasswordMail);
            }
        });
    }

    private static String getTextMessage() {
        return MessageFormat.format("""
                        Hello,<br>
                        <br>
                        Attached to this email, you will find the XXXXXX UI test report dated <b>{0}</b>. <br>
                        It is an .html file, readable by any browser (double-click to open). <br>
                        <br>
                        In case of a test failure, the screenshot is not attached to this email. Please consult the report directly on the server. <br>
                        <br>
                        Have a nice day, <br>
                        QA Automation Team <br>
                        <br>
                        <i>This is an automated email, please do not reply.</i>
                    """
                , new SimpleDateFormat(UtilsConstants.READABLE_DATE_FORMAT).format(new Date()));
    }
}
