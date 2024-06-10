package ru.gbp.bot;



import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gbp.bot.config.BotConfigProperties;

@SpringBootTest
@Slf4j
class BotApplicationTests {



	@Autowired
	private  BotConfigProperties botConfigProperties;



	@Test
	void contextLoads() {
	}

	@Test
	void setBotConfigProperties(){
		Assertions.assertNotNull(botConfigProperties.getName());
		log.info(botConfigProperties.getName());
		Assertions.assertNotNull(botConfigProperties.getToken());
		log.info(botConfigProperties.getToken());
	}

}
