package controllerTest;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
	}
}
