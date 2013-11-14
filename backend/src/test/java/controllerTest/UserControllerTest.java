package controllerTest;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.teamsierra.csc191.api.controller.UserController;
import com.teamsierra.csc191.api.repository.StylistAvailabilityRepository;
import com.teamsierra.csc191.api.repository.UserRepository;

public class UserControllerTest 
{	
	private UserRepository userRepo = mock(UserRepository.class);
	private StylistAvailabilityRepository sar = mock(StylistAvailabilityRepository.class);
	
	private MockMvc mockMVC = standaloneSetup(new UserController(userRepo, sar)).build();
	
	@Before
	public void before()
	{
		
	}
	
	@Test
	public void postTest() throws Exception
	{
		//CLIENT
		RequestPostProcessor requestPostProcessor = new RequestPostProcessor(){
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.setAttribute("authType", "CLIENT");
				mockRequest.setAttribute("authToken", "123456");
				mockRequest.setAttribute("id", " ");
				
				return mockRequest;
			}
		};
		
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.with(requestPostProcessor)
				.accept(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isForbidden());
		
		// STYLIST
		requestPostProcessor = new RequestPostProcessor(){
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.setAttribute("authType", "STYLIST");
				mockRequest.setAttribute("authToken", "123456");
				mockRequest.setAttribute("id", " ");
				
				return mockRequest;
			}
		};
		
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.with(requestPostProcessor)
				.accept(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isForbidden());
		
		//ADMIN
		requestPostProcessor = new RequestPostProcessor(){
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.setAttribute("authType", "ADMIN");
				mockRequest.setAttribute("authToken", "123456");
				mockRequest.setAttribute("id", " ");
				
				return mockRequest;
			}
		};
		
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.with(requestPostProcessor)
				.accept(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest());
		
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.with(requestPostProcessor)
				.accept(MediaType.APPLICATION_JSON).content("{"
						+ " \"type\": \"CLIENT\","
						+ " \"firstName\": \"Kyle\","
						+ " \"lastName\": \"Matz\","
						+ " \"email\": \"kmatz4b@gmail.com\","
						+ " \"password\": \"password\""
						+ "}"))
				.andExpect(status().isBadRequest());
		
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.with(requestPostProcessor)
				.accept(MediaType.APPLICATION_JSON).content("{"
						+ " \"type\": \"STYLIST\","
						+ " \"firstName\": \"Kyle\","
						+ " \"lastName\": \"Matz\","
						+ " \"email\": \"kmatz4b@gmail.com\","
						+ " \"password\": \"password\","
						+ " \"phone\": \"91622dfaf44\""
						+ "}"))
				.andExpect(status().isBadRequest());
		
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.with(requestPostProcessor)
				.accept(MediaType.APPLICATION_JSON).content("{"
						+ " \"type\": \"ADMIN\","
						+ " \"firstName\": \"Kyle\","
						+ " \"lastName\": \"Matz\","
						+ " \"email\": \"kmatz4b@gmail.com\","
						+ " \"password\": \"password\""
						+ "}"))
				.andExpect(status().isCreated());
		
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.with(requestPostProcessor)
				.accept(MediaType.APPLICATION_JSON).content("{"
						+ " \"type\": \"STYLIST\","
						+ " \"firstName\": \"Kyle\","
						+ " \"lastName\": \"Matz\","
						+ " \"email\": \"kmatz4b@gmail.com\","
						+ " \"password\": \"password\","
						+ " \"phone\": \"9162224444\""
						+ "}"))
				.andExpect(status().isCreated());
		
		
		//TODO fongo? mock the db, or something like that
		/*requestPostProcessor = new RequestPostProcessor(){
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.setAttribute("authType", "ADMIN");
				mockRequest.setAttribute("authToken", "123456");
				mockRequest.setAttribute("id", " ");
				
				return mockRequest;
			}
		};
		
		mockMVC.perform(get("/users")
				.with(requestPostProcessor))
				.andExpect(status().isOk());*/
	}
}
