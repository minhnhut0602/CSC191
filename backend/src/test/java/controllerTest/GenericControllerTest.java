package controllerTest;

import com.teamsierra.csc191.api.model.GenericModel;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.junit.BeforeClass;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @Author: Alex Chernyak
 * @Date: 11/11/13
 * @Project: salon-scheduler-api
 * @Package: controllerTest
 * @Description: place short description here
 */
public abstract class GenericControllerTest
{
    protected static RequestPostProcessor requestClient, requestStylist, requestAdmin;
    //protected final AppointmentRepository aRepo = mock(AppointmentRepository.class);
    protected final UserRepository uRepo = mock(UserRepository.class);
    protected final String adminType = GenericModel.UserType.ADMIN.toString();
    protected final String clientType = GenericModel.UserType.CLIENT.toString();
    protected static User admin, client, stylist;

    protected static List<User> users;

    protected MockHttpServletRequest setRequest(String authType, String authToken, String id)
    {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setAttribute("authType", authType);
        mockRequest.setAttribute("authToken", authToken);
        mockRequest.setAttribute("id", id);

        return mockRequest;
    }

    /**
     * This method sets test environment that will be used by mock objects
     */
    @BeforeClass
    public static void populateUserRepo()
    {
        admin = new User();
        client = new User();
        stylist = new User();
        users = new ArrayList<>();

        // Set admin info
        admin.setActive(true);
        admin.setId("adminid");
        admin.setType(GenericModel.UserType.ADMIN);
        admin.setToken("admintoken");
        admin.setFirstName("Admin");
        admin.setLastName("Test");
        users.add(admin);

        // Set client info
        client.setActive(true);
        client.setId("clientid");
        client.setType(GenericModel.UserType.CLIENT);
        client.setToken("clienttoken");
        client.setFirstName("Client");
        client.setLastName("Test");
        users.add(client);

        // Set stylist info
        stylist.setActive(true);
        stylist.setId("stylistid");
        stylist.setType(GenericModel.UserType.STYLIST);
        stylist.setToken("stylisttoken");
        stylist.setFirstName("Stylist");
        stylist.setLastName("Test");
        users.add(stylist);

        // Set requests
        requestClient = new RequestPostProcessor()
        {
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
            {
                mockRequest.setAttribute("authType", "CLIENT");
                mockRequest.setAttribute("authToken", client.getToken());
                mockRequest.setAttribute("id", client.getId());

                return mockRequest;
            }
        };

        requestStylist = new RequestPostProcessor()
        {
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
            {
                mockRequest.setAttribute("authType", "STYLIST");
                mockRequest.setAttribute("authToken", stylist.getToken());
                mockRequest.setAttribute("id", stylist.getId());

                return mockRequest;
            }
        };

        requestAdmin = new RequestPostProcessor()
        {
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockRequest)
            {
                mockRequest.setAttribute("authType", "ADMIN");
                mockRequest.setAttribute("authToken", admin.getToken());
                mockRequest.setAttribute("id", admin.getId());

                return mockRequest;
            }
        };
    }
}
