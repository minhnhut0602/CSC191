package controllerTest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.model.GenericModel.UserType;
import com.teamsierra.csc191.api.repository.UserRepository;

import static com.jayway.jsonassert.JsonAssert.collectionWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml", "file:src/main/webapp/WEB-INF/spring/salon-scheduler-api/servlet-context-test.xml"})
public class UserControllerIntegrationTest 
{
	@Autowired
	private volatile WebApplicationContext webApplicationContext;
	@Autowired
	private UserRepository userRepo;
	
	private volatile MockMvc mockMVC;
	
	private static RequestPostProcessor requestPostProcessorAdmin,
										requestPostProcessorStylist;
	private static String tokenAdmin = "tokenAdmin",
						  tokenStylist = "tokenStylist",
						  tokenClient = "tokenClient";
	
	@BeforeClass
	public static void beforeClass()
	{
		requestPostProcessorAdmin = new RequestPostProcessor()
		{
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.addHeader("authToken", tokenAdmin);
				
				return mockRequest;
			}
		};
		
		requestPostProcessorStylist = new RequestPostProcessor()
		{
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.addHeader("authToken", tokenStylist);
				
				return mockRequest;
			}
		};
	}
	
	@Before
	public void before()
	{
		mockMVC = webAppContextSetup(this.webApplicationContext).build();
		userRepo.deleteAll();
	}
	
	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdmin() throws Exception
	{
		// create users
		User user = new User();
		user.setActive(true);
		user.setToken(tokenAdmin);
		user.setType(UserType.ADMIN);
		userRepo.insert(user);
		String adminID = user.getId();
		
		user = new User();
		user.setActive(true);
		user.setToken(tokenStylist);
		user.setType(UserType.STYLIST);
		userRepo.insert(user);
		
		user = new User();
		user.setActive(false);
		user.setToken(tokenClient);
		user.setType(UserType.CLIENT);
		userRepo.insert(user);
		String clientID = user.getId();
		
		assertTrue(adminID != clientID);

		// get all users
		mockMVC.perform(get("/users")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$")
						.value(collectionWithSize(equalTo(3))));
		// get client
		mockMVC.perform(get("/users/" + clientID)
				.with(requestPostProcessorAdmin))
				.andExpect(status().isOk());
		
		mockMVC.perform(get("/users/me")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isOk());
		
		mockMVC.perform(get("/users/stylists")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$")
						.value(collectionWithSize(equalTo(2))));
	}
	
	@Test
	public void testStylist() throws Exception
	{
		User user = new User();
		user.setActive(true);
		user.setToken(tokenStylist);
		user.setType(UserType.STYLIST);
		userRepo.insert(user);
		
		user = new User();
		user.setActive(false);
		user.setToken(tokenClient);
		user.setType(UserType.CLIENT);
		userRepo.insert(user);
		String clientID = user.getId();
		
		// get client
		mockMVC.perform(get("/users/" + clientID)
				.with(requestPostProcessorStylist))
				.andExpect(status().isNotFound());
		
		mockMVC.perform(get("/users/me")
				.with(requestPostProcessorStylist))
				.andExpect(status().isOk());
		
		mockMVC.perform(get("/users/stylists")
				.with(requestPostProcessorStylist))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$")
						.value(collectionWithSize(equalTo(1))));
	}
}
