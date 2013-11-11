package controllerTest;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.teamsierra.csc191.api.controller.AvailabilityController;
import com.teamsierra.csc191.api.repository.AppointmentRepository;
import com.teamsierra.csc191.api.repository.StylistAvailabilityRepository;
import com.teamsierra.csc191.api.repository.UserRepository;

public class AvailabilityControllerTest 
{
	private UserRepository userRepo = mock(UserRepository.class);
	private StylistAvailabilityRepository sar = mock(StylistAvailabilityRepository.class);
	private AppointmentRepository apptRepo = mock(AppointmentRepository.class);
	
	private MockMvc mockMVC = standaloneSetup(new AvailabilityController(userRepo, sar, apptRepo)).build();
	
	@Before
	public void before()
	{
		
	}
	
	@Test
	public void getMonthTest() throws Exception
	{
		mockMVC.perform(get("/availability?month=10&year=2013"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getDayTest() throws Exception
	{
		mockMVC.perform(get("/availability?month=10&year=2013&day=20"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void putTest()
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
	}
}
