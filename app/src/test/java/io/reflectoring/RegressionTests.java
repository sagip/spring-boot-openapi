package io.reflectoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.Mockito;
import java.nio.charset.Charset;

import org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

@SpringBootTest
@AutoConfigureMockMvc
class RegressionTests {
	
	private String ClientBodyTemplate = "{\"name\":\"%s\",\"email\":\"%s\"}";
	private String PositionBodyTemplate = "{\"name\":\"%s\",\"location\":\"%s\",\"uuid\":\"%s\"}";
	
	private String existingUuid = "c58ea483-658e-4f74-8175-6c7057992a03";
	
	@Autowired
	private MockMvc mockMvc;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Test
	void createClientOk() throws Exception{
		String actual = mockMvc.perform(  
				post("/client")
				.contentType(APPLICATION_JSON_UTF8)
				.content(String.format(ClientBodyTemplate, "rest client name", "goodemail@restassured.com")))
				.andExpect(status().is(201)).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	void createClientEmailAlreadyExists() throws Exception{
		String actual = mockMvc.perform(  
				post("/client")
				.contentType(APPLICATION_JSON_UTF8)
				.content(String.format(ClientBodyTemplate, "rest client name", "roger.alpha@asdf.com")))
				.andExpect(status().is(400)).andReturn().getResponse().getContentAsString();
		String expect = "{\"data\":null,\"messages\":[\"client cannot be created, it already exists: id: 1, name: Roger Alpha, email: roger.alpha@asdf.com, uuid: "+existingUuid+"\"],\"status\":false}";
		JSONAssert.assertEquals(expect,  actual, JSONCompareMode.LENIENT);
	}
	
	@Test
	void createClientNameIs101CharsLong() throws Exception{
		String actual = mockMvc.perform(  
				post("/client")
				.contentType(APPLICATION_JSON_UTF8)
				.content(String.format(ClientBodyTemplate, "a".repeat(101), "rogersdfds.alpha@asdf.com")))
				.andExpect(status().is(400)).andReturn().getResponse().getContentAsString();
		String expect = "{\"data\":null,\"messages\":[\"name lenght is not in (1,100) range\"],\"status\":false}";
		JSONAssert.assertEquals(expect,  actual, JSONCompareMode.LENIENT);
	}
	/*
	@Test
	public void createPosition() throws Exception{
		String con = String.format(PositionBodyTemplate, "position name", "position location", existingUuid);
		System.out.println(con);
		String actual = mockMvc.perform(  
				post("/position")
				.contentType(APPLICATION_JSON_UTF8)
				.content(con))
				.andExpect(status().is(201)).andReturn().getResponse().getContentAsString();
		//String expect = "{\"data\":null,\"messages\":[\"Name lenght is not in (1,100) range\"],\"status\":false}";
		//JSONAssert.assertEquals(expect,  actual, JSONCompareMode.LENIENT);
	}*/
	
	@Test
	void createPosition51CharLongName() throws Exception{
		String con = String.format(PositionBodyTemplate, "a".repeat(51), "position location", existingUuid);
		String actual = mockMvc.perform(  
				post("/position")
				.contentType(APPLICATION_JSON_UTF8)
				.content(con))
				.andExpect(status().is(400)).andReturn().getResponse().getContentAsString();
		String expect = "{\"data\":null,\"messages\":[\"name lenght is not in (0,50) range: "+"a".repeat(51)+"\"],\"status\":false}";
		JSONAssert.assertEquals(expect,  actual, JSONCompareMode.LENIENT);
	}

	@Test
	void searchPosition() throws Exception{
		mockMvc.perform(  
				get("/position/search")
				.param("keyword", "z")
				.param("location", "z")
				.param("uuid", existingUuid))
				.andExpect(status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[*].url").isNotEmpty());
	}
	
	@Test
	void searchPositionNoResults() throws Exception{
		mockMvc.perform(  
				get("/position/search")
				.param("keyword", "zasdfuhsdfsd")
				.param("location", "zASDFSDFSDF")
				.param("uuid", existingUuid))
				.andExpect(status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
	}
	
	@Test
	void getPosition() throws Exception{
		mockMvc.perform(  
				get("/position/{positionId}", "2"))
				.andExpect(status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[*].url").isNotEmpty());
	}
	
	@Test
	void getPositionInvalidPositionId() throws Exception{
		mockMvc.perform(  
				get("/position/{positionId}", "200"))
				.andExpect(status().is(400))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
	}
	


}
