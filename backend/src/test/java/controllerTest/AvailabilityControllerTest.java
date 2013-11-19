package controllerTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.teamsierra.csc191.api.controller.AvailabilityController;
import com.teamsierra.csc191.api.model.StylistAvailability;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.AppointmentRepository;
import com.teamsierra.csc191.api.repository.StylistAvailabilityRepository;
import com.teamsierra.csc191.api.repository.UserRepository;

public class AvailabilityControllerTest 
{
	private static RequestPostProcessor requestPostProcessorClient,
										requestPostProcessorStylist,
										requestPostProcessorAdmin;
	private static StylistAvailability availStylist;
	private static User userStylist;
	private static String token = "testToken";
	private static String id = "testID";
	
	private UserRepository userRepo = mock(UserRepository.class);
	private StylistAvailabilityRepository sar = mock(StylistAvailabilityRepository.class);
	private AppointmentRepository apptRepo = mock(AppointmentRepository.class);
	
	private MockMvc mockMVC = standaloneSetup(new AvailabilityController(userRepo, sar, apptRepo)).build();
	
	private static final String USER_LINK = "http://localhost/users/" + id;
	private static final String AVAILABILITY_LINK = "http://localhost/availability/" + id;
	
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
		
		availStylist = new StylistAvailability();
		availStylist.setStylistID(id);
		userStylist = new User();
	}
	
	/**
	 * Test call to GET month availability. 
	 * 
	 * testing for:
	 * -status is ok.
	 * 
	 * @throws Exception
	 */
	@Test
	public void monthGetTest() throws Exception
	{
		mockMVC.perform(get("/availability?month=10&year=2013"))
				.andExpect(status().isOk());
	}
	
	/**
	 * Test call to GET month availability, missing month param.
	 * (or is it a call to GET day availability with two missing
	 * params...?)
	 * 
	 * testing for:
	 * -status is bad request.
	 * 
	 * @throws Exception
	 */
	@Test
	public void monthMissingGetTest() throws Exception
	{
		mockMVC.perform(get("/availability?year=2013"))
				.andExpect(status().isBadRequest());
	}
	
	/**
	 * Test call to GET day availability. 
	 * 
	 * testing for:
	 * -status is ok.
	 * 
	 * @throws Exception
	 */
	@Test
	public void dayGetTest() throws Exception
	{
		mockMVC.perform(get("/availability?month=10&year=2013&day=20"))
				.andExpect(status().isOk());
	}
	
	/**
	 * Test call to GET user availability as a Client.
	 * 
	 * testing for:
	 * -status is ok.
	 * 
	 * @throws Exception
	 */
	@Test
	public void clientGetAvailabilityTest() throws Exception
	{
		when(sar.findByStylistID(id)).thenReturn(availStylist);
		
		mockMVC.perform(get("/availability/" + id)
				.with(requestPostProcessorClient))
				.andExpect(status().isOk());
	}
	
	/**
	 * Test call to GET user availability as a Stylist.
	 * 
	 * testing for:
	 * -proper repo methods called.
	 * -the return value has links to the stylist's availability and 
	 * 	the stylist user.
	 * -status is ok.
	 * 
	 * @throws Exception
	 */
	@Test
	public void stylistGetAvailabilityTest() throws Exception
	{
		when(sar.findByStylistID(id)).thenReturn(availStylist);
		
		mockMVC.perform(get("/availability/" + id)
				.with(requestPostProcessorStylist))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.links[?(@.rel==stylist)].href[0]")
						.value(USER_LINK))
				.andExpect(jsonPath("$.links[?(@.rel==availability)].href[0]")
						.value(AVAILABILITY_LINK));
	}
	
	/**
	 * Test call to GET user availability as a Stylist, different user.
	 * 
	 * testing for:
	 * -proper repo methods called.
	 * -status is ok.
	 * 
	 * @throws Exception
	 */
	@Test
	public void stylistGetDifferentAvailabilityTest() throws Exception
	{
		when(sar.findByStylistID("differentID")).thenReturn(availStylist);
		
		mockMVC.perform(get("/availability/differentID")
				.with(requestPostProcessorStylist))
				.andExpect(status().isOk());
	}
	
	/**
	 * Test call to GET user availability as an admin.
	 * 
	 * testing for:
	 * -proper repo methods called.
	 * -status is ok.
	 * 
	 * @throws Exception
	 */
	@Test
	public void adminGetAvailbilityTest() throws Exception
	{
		when(sar.findByStylistID(id)).thenReturn(availStylist);
		
		mockMVC.perform(get("/availability/" + id)
				.with(requestPostProcessorAdmin))
				.andExpect(status().isOk());
	}
	
	/**
	 * Test call to GET user availability as an admin, different user.
	 * 
	 * testing for:
	 * -status is ok.
	 * 
	 * @throws Exception
	 */
	@Test
	public void adminGetDifferentAvailabilityTest() throws Exception
	{
		when(sar.findByStylistID("differentID")).thenReturn(availStylist);
		
		mockMVC.perform(get("/availability/differentID")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isOk());
	}
	
	/**
	 * Test call to GET user availability as an admin, missing user.
	 * 
	 * testing for:
	 * -status is not found.
	 * 
	 * @throws Exception
	 */
	@Test
	public void adminGetMissingAvailabilityTest() throws Exception
	{
		when(sar.findByStylistID("differentID")).thenReturn(null);
		
		mockMVC.perform(get("/availability/differentID")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isNotFound());
	}
	
	/**
	 * Test call to PUT availability as client.
	 * 
	 * testing for:
	 * -status is bad request.
	 * 
	 */
	@Test
	public void clientPutTest() throws Exception
	{
		mockMVC.perform(put("/availability/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"availability\":[]}")
				.with(requestPostProcessorClient))
				.andExpect(status().isBadRequest());
	}
	
	/**
	 * Test call to PUT availability as stylist.
	 * 
	 * testing for:
	 * -proper repo methods called.
	 * -status is accepted.
	 * 
	 * @throws Exception
	 */
	@Test
	public void stylistPutTest() throws Exception
	{
		userStylist.setId(id);
		when(userRepo.findByToken(token)).thenReturn(userStylist);
		when(sar.findByStylistID(id)).thenReturn(availStylist);
		
		mockMVC.perform(put("/availability/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"availability\":[]}")
				.with(requestPostProcessorStylist))
				.andExpect(status().isAccepted());
	}
	
	/**
	 * Test call to PUT availability as stylist, different user.
	 * 
	 * testing for:
	 * -proper repo methods called.
	 * -status is forbidden.
	 * 
	 * @throws Exception
	 */
	@Test
	public void stylistPutDifferentUserTest() throws Exception
	{
		userStylist.setId(id);
		when(userRepo.findByToken(token)).thenReturn(userStylist);
		
		mockMVC.perform(put("/availability/differentID")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"availability\":[]}")
				.with(requestPostProcessorStylist))
				.andExpect(status().isForbidden());
	}
	
	/**
	 * Test call to PUT availability as stylist, null availability.
	 * 
	 * testing for:
	 * -status is bad request.
	 * 
	 * @throws Exception
	 */
	@Test
	public void stylistMissingAvailabilityPutTest() throws Exception
	{
		userStylist.setId(id);
		when(userRepo.findByToken(token)).thenReturn(userStylist);
		when(sar.findByStylistID(id)).thenReturn(availStylist);
		
		mockMVC.perform(put("/availability/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"availability\":}")
				.with(requestPostProcessorStylist))
				.andExpect(status().isBadRequest());
	}
	
	/**
	 * Test call to PUT availability as admin.
	 * 
	 * testing for:
	 * -proper repo methods called.
	 * -status is accepted.
	 * 
	 * @throws Exception
	 */
	@Test
	public void adminPutTest() throws Exception
	{
		when(sar.findByStylistID(id)).thenReturn(availStylist);
		
		mockMVC.perform(put("/availability/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"availability\":[]}")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isAccepted());
	}
	
	/**
	 * Test call to PUT availability as admin, different user.
	 * 
	 * testing for:
	 * -status is accepted.
	 * 	
	 * @throws Exception
	 */
	@Test
	public void adminPutDifferntUserTest() throws Exception
	{
		when(sar.findByStylistID("differentID")).thenReturn(availStylist);
		
		mockMVC.perform(put("/availability/differentID")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"availability\":[]}")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isAccepted());
	}
	
	/**
	 * Test call to PUT availability as admin, missing user.
	 * 
	 * testing for:
	 * -status is not found.
	 * 	
	 * @throws Exception
	 */
	@Test
	public void adminPutMissingUserTest() throws Exception
	{
		when(sar.findByStylistID("differentID")).thenReturn(null);
		
		mockMVC.perform(put("/availability/differentID")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"availability\":[]}")
				.with(requestPostProcessorAdmin))
				.andExpect(status().isNotFound());
	}
}
