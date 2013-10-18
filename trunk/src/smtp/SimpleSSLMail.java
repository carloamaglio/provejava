package smtp;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;


public class SimpleSSLMail {

    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_HOST_PORT = 465;
    private static final String SMTP_AUTH_USER = "amaglio.c@gmail.com";
    private static final String SMTP_AUTH_PWD  = "marameo00";

    public static void main(String[] args) throws Exception{
       new SimpleSSLMail().test();
    }

    public void test() throws Exception{
        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", SMTP_HOST_NAME);
        props.put("mail.smtps.auth", "true");
//		props.put("mail.smtps.quitwait", "false");

        Session mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject("Testing SMTP-SSL");
        message.setContent("This is a test", "text/plain");

        message.addRecipient(Message.RecipientType.TO, new InternetAddress("carloamaglio@bmb-spa.com"));

        transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);

        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
}
