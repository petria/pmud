package org.freakz.pmud.pmudclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@Slf4j
public class PMudClientApplication {

    public static void main(String[] args) {

        System.out.print(">> Start ...\n");

        ConfigurableApplicationContext ctx = new
                SpringApplicationBuilder(PMudClientApplication.class).web(WebApplicationType.NONE).run(args);

        System.out.print(">> Main done!\n");


    }

}
