package controllerTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.teamsierra.csc191.api.controller.UserController;
import com.teamsierra.csc191.api.model.GenericModel.UserType;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.StylistAvailabilityRepository;
import com.teamsierra.csc191.api.repository.UserRepository;

public class UserControllerTest 
{	
	private static RequestPostProcessor requestPostProcessorClient,
										requestPostProcessorStylist,
										requestPostProcessorAdmin;
	private static User userClient, userStylist, userAdmin;
	private static List<User> userList;
	private static String token = "testToken";
	private static String id = "testID";
	
	private UserRepository userRepo = mock(UserRepository.class);
	private StylistAvailabilityRepository sar = mock(StylistAvailabilityRepository.class);
	
	private MockMvc mockMVC = standaloneSetup(new UserController(userRepo, sar)).build();
	
	@BeforeClass
	public static void beforeClass()
	{
		requestPostProcessorClient = new RequestPostProcessor()
		{
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.setAttribute("authType", "CLIENT");
				mockRequest.setAttribute("authToken", token);
				mockRequest.setAttribute("id", id);
				
				return mockRequest;
			}
		};
		
		requestPostProcessorStylist = new RequestPostProcessor()
		{
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.setAttribute("authType", "STYLIST");
				mockRequest.setAttribute("authToken", token);
				mockRequest.setAttribute("id", id);
				
				return mockRequest;
			}
		};
		
		requestPostProcessorAdmin = new RequestPostProcessor()
		{
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
			{
				mockRequest.setAttribute("authType", "ADMIN");
				mockRequest.setAttribute("authToken", token);
				mockRequest.setAttribute("id", id);
				
				return mockRequest;
			}
		};
		
		userClient = new User();
		userStylist = new User();
		userAdmin = new User();
		
		userList = new ArrayList<User>();
		userList.add(userClient);
		userList.add(userStylist);
		userList.add(userAdmin);
	}
	
	@Test
	public void clientGetTest() throws Exception
	{
		when(userRepo.findByToken(token)).thenReturn(userClient);
		
		mockMVC.perform(get("/users")
				.with(requestPostProcessorClient))
				.andExpect(status().isOk());
	}
	
	@Test
	public void stylistGetTest() throws Exception
	{		
		when(userRepo.findAllActive()).thenReturn(userList);
		
		mockMVC.perform(get("/users")
				.with(requestPostProcessorStylist))
				.andExpect(status().isOk());
	}
	
	@Test
	public void adminGetTest() throws Exception
	{		
		when(userRepo.findAll()).thenReturn(userList);
		
		mockMVC.perform(get("/users")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isOk());
	}
	
	@Test
	public void failGetTest() throws Exception
	{
		// no users returned from repo
		when(userRepo.findAll()).thenReturn(new ArrayList<User>());
		
		mockMVC.perform(get("/users")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void clientPostTest() throws Exception
	{		
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content("{}")
				.with(requestPostProcessorClient))
				.andExpect(status().isForbidden());
	}
	
	@Test
	public void stylistPostTest() throws Exception
	{		
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content("{}")
				.with(requestPostProcessorStylist))
				.andExpect(status().isForbidden());
	}
	
	@Test
	public void adminPostEmptyUserTest() throws Exception
	{
		// empty user
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content("{}")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void adminPostCreateClientTest() throws Exception
	{
		// try to create a client user
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(
						"{"
						+ " \"type\": \"CLIENT\","
						+ " \"firstName\": \"Kyle\","
						+ " \"lastName\": \"Matz\","
						+ " \"email\": \"kmatz4b@gmail.com\","
						+ " \"password\": \"password\""
						+ "}")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void adminPostInvalidFieldTest() throws Exception
	{
		// invalid phone
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(
						"{"
						+ " \"type\": \"STYLIST\","
						+ " \"firstName\": \"Kyle\","
						+ " \"lastName\": \"Matz\","
						+ " \"email\": \"kmatz4b@gmail.com\","
						+ " \"password\": \"password\","
						+ " \"phone\": \"91622dfaf44\""
						+ "}")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void adminPostMissingReqFieldTest() throws Exception
	{
		// missing required field (email)
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(
						"{"
						+ " \"type\": \"STYLIST\","
						+ " \"firstName\": \"Kyle\","
						+ " \"lastName\": \"Matz\","
						+ " \"password\": \"password\","
						+ " \"phone\": \"9166668888\""
						+ "}")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void adminPostMinimalFieldsValidTest() throws Exception
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
	}
	
	@Test
	public void adminPostOptionalFieldsValidTest() throws Exception
	{
		// create user with optional fields
		mockMVC.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(
						"{"
						+ " \"type\": \"STYLIST\","
						+ " \"firstName\": \"Kyle\","
						+ " \"lastName\": \"Matz\","
						+ " \"email\": \"kmatz4b@gmail.com\","
						+ " \"password\": \"password\","
						+ " \"phone\": \"9162224444\""
						+ "}")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void clientGetUserTest() throws Exception
	{
		userClient.setId(id);
		when(userRepo.findByToken(token)).thenReturn(userClient);
		
		mockMVC.perform(get("/users/" + id)
				.with(requestPostProcessorClient))
				.andExpect(status().isOk());
	}
	
	@Test
	public void clientGetDifferentUserTest() throws Exception
	{
		userClient.setId(id);
		when(userRepo.findByToken(token)).thenReturn(userClient);
		
		mockMVC.perform(get("/users/differntID")
				.with(requestPostProcessorClient))
				.andExpect(status().isForbidden());
	}
	
	@Test
	public void stylistGetUserTest() throws Exception
	{
		userStylist.setActive(true);
		when(userRepo.findById(id)).thenReturn(userStylist);
		
		mockMVC.perform(get("/users/" + id)
				.with(requestPostProcessorStylist))
				.andExpect(status().isOk());
	}
	
	@Test
	public void stylistGetInactiveUserTest() throws Exception
	{
		userStylist.setActive(false);
		when(userRepo.findById(id)).thenReturn(userStylist);
		
		mockMVC.perform(get("/users/" + id)
				.with(requestPostProcessorStylist))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void adminGetUserTest() throws Exception
	{
		when(userRepo.findById(id)).thenReturn(userAdmin);
		
		mockMVC.perform(get("/users/" + id)
				.with(requestPostProcessorAdmin))
				.andExpect(status().isOk());
	}
	
	@Test
	public void adminGetInvalidUserTest() throws Exception
	{
		mockMVC.perform(get("/users/differentID")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void clientPutUserTest() throws Exception
	{
		userClient.setId(id);
		when(userRepo.findByToken(token)).thenReturn(userClient);
		
		mockMVC.perform(put("/users/" + id).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content("{}")
				.with(requestPostProcessorClient))
				.andExpect(status().isAccepted());
	}
	
	@Test
	public void stylistPutUserTest() throws Exception
	{
		userStylist.setId(id);
		when(userRepo.findByToken(token)).thenReturn(userStylist);
		
		mockMVC.perform(put("/users/" + id).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content("{}")
				.with(requestPostProcessorClient))
				.andExpect(status().isAccepted());
	}
	
	@Test
	public void stylistPutDifferentUserTest() throws Exception
	{
		userStylist.setId(id);
		when(userRepo.findByToken(token)).thenReturn(userStylist);
		
		mockMVC.perform(put("/users/differentID").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content("{}")
				.with(requestPostProcessorClient))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void adminPutUserTest() throws Exception
	{
		userAdmin.setType(UserType.ADMIN);
		when(userRepo.findById(id)).thenReturn(userAdmin);
		
		mockMVC.perform(put("/users/" + id).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content("{}")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isAccepted());
	}
}
