package home.mail;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static com.icegreen.greenmail.util.GreenMailUtil.getBody;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ActiveProfiles(value = "prod", inheritProfiles = false)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MailBoxTest {

    @Autowired
    private MailBox mailBox;

    @RegisterExtension
    public final GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    @Test
    public void sendConfirmation() throws MessagingException, IOException {
        mailBox.sendUserResults("John Doe", "jdoe@example.com", IOUtils.toString(new ClassPathResource("/data/results.json").getInputStream()));

        MimeMessage mimeMessage = mailMessage();
        assertEquals("jdoe@example.com", mimeMessage.getRecipients(Message.RecipientType.TO)[0].toString());

        String body = getBody(mimeMessage);
        assertTrue(body.contains("Introduction to Biolog"));

    }

    private MimeMessage mailMessage() {
        await().until(() -> greenMail.getReceivedMessages().length != 0);
        return greenMail.getReceivedMessages()[0];
    }
}