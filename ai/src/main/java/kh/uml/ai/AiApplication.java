package kh.uml.ai;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;

import lombok.extern.log4j.Log4j2;


@Log4j2
@SpringBootApplication(exclude = {
	GroovyTemplateAutoConfiguration.class})
public class AiApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(AiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info(" ######################  RUNNING... ###################### ");
	}

}
