package com.openAI.firstProject;

import com.openAI.firstProject.service.ETLService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FirstProjectApplicationTests {

	@Autowired
	private ETLService etlService;

	@Test
	void contextLoads() {
		etlService.loadDataFromPdf();
	}

}
