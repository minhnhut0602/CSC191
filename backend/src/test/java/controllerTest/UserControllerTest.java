package controllerTest;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.springframework.test.web.servlet.MockMvc;

import com.teamsierra.csc191.api.controller.UserController;
import com.teamsierra.csc191.api.repository.UserRepository;

public class UserControllerTest 
{	
	private UserRepository userRepo = mock(UserRepository.class);
	private MockMvc mockMVC = standaloneSetup(new UserController()).build();
	
	@Before
	public void before()
	{
		
	}
}
