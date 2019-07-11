package org.freakz.pmud.pmudclient;

import ch.qos.logback.classic.Level;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@Slf4j
public class PMudClientApplication {

    public static void main(String[] args) {

//        log.debug("Disabling LogBack logger!");
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.OFF);

        SpringApplication app = new SpringApplication(PMudClientApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);

    }

}
