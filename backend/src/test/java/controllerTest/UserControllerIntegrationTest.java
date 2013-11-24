package controllerTest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml", "file:src/main/webapp/WEB-INF/spring/salon-scheduler-api/servlet-context-test.xml"})
public class UserControllerIntegrationTest 
{
	@Autowired
	private volatile WebApplicationContext webApplicationContext;
	private volatile MockMvc mockMVC;
	
	private static RequestPostProcessor requestPostProcessorAdmin,
										requestPostProcessorStylist,
										requestPostProcessorClient;
	private static String token = "testToken";
	private static String id = "testID";
	
	@BeforeClass
	public static void beforeClass()
	{				
		requestPostProcessorClient = new RequestPostProcessor()
		{
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.addHeader("authType", "CLIENT");
				mockRequest.addHeader("authToken", token);
				mockRequest.addHeader("id", id);
				mockRequest.addHeader("debug", "true");
				
				return mockRequest;
			}
		};
		
		requestPostProcessorStylist = new RequestPostProcessor()
		{
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.addHeader("authType", "STYLIST");
				mockRequest.addHeader("authToken", token);
				mockRequest.addHeader("id", id);
				mockRequest.addHeader("debug", "true");
				
				return mockRequest;
			}
		};
		
		requestPostProcessorAdmin = new RequestPostProcessor()
		{
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.addHeader("authType", "ADMIN");
				mockRequest.addHeader("authToken", token);
				mockRequest.addHeader("id", id);
				mockRequest.addHeader("debug", "true");
				
				return mockRequest;
			}
		};
	}
	
	@Before
	public void before()
	{
		mockMVC = webAppContextSetup(this.webApplicationContext).build();
	}
	
	/**
	 * IF DEBUG IS REMOVED FROM INTERCEPTOR COMMENT OUT THIS TEST.
	 * IT WILL FAIL DUE TO MADE UP CREDENTIALS.
	 * 
	 * Don't delete without talking to Kyle.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFongo() throws Exception
	{
		// create user
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(
						"{"
						+ " \"type\": \"ADMIN\","
						+ " \"firstName\": \"Kyle\","
						+ " \"lastName\": \"Matz\","
						+ " \"email\": \"kmatz4b@gmail.com\","
						+ " \"password\": \"password\""
						+ "}")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isCreated());

		// get all users
		mockMVC.perform(get("/users")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isOk());
	}
}
