package io.reflectoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.reflectoring.service.InputValidator;

@SpringBootTest
@AutoConfigureMockMvc
public class InputValidatorTests {

	
	@Test
	void getValidatedUuid() throws Exception{
		String validUuid = "4ce71561-a897-4be5-b308-bceb0de88016";
		String formatErrorUuid = "4ce7-1561a897-4be5-b308-bceb0de88016";
		String typoErrorUuid = "4ce7l561-a897-4be5-b308-bceb0de88016";
		assertNotNull(InputValidator.getValidatedUuid(validUuid));
		assertNull(InputValidator.getValidatedUuid(formatErrorUuid));
		assertNull(InputValidator.getValidatedUuid(typoErrorUuid));
	}

	
}
