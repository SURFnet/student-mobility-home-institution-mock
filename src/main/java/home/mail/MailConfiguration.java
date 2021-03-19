package home.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
public class MailConfiguration {

    @Value("${email.from}")
    private String emailFrom;

    @Autowired
    private JavaMailSender mailSender;

    @Bean
    @Profile({"!test", "!dev"})
    public MailBox mailSenderProd() throws IOException {
        return new MailBox(mailSender, emailFrom);
    }

    @Bean
    @Profile({"test", "dev"})
    @Primary
    public MailBox mailSenderDev(Environment environment) throws IOException {
        return new MockMailBox(mailSender, emailFrom, environment);
    }


}
