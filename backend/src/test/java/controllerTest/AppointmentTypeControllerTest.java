package controllerTest;

import com.teamsierra.csc191.api.controller.AppointmentTypeController;
import com.teamsierra.csc191.api.model.AppointmentType;
import com.teamsierra.csc191.api.repository.AppointmentTypeRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @Author: Alex Chernyak
 * @Date: 11/11/13
 * @Project: salon-scheduler-api
 * @Package: controllerTest
 * @Description: place short description here
 */
public class AppointmentTypeControllerTest extends GenericControllerTest
{
    private final AppointmentTypeRepository atRepo = mock(AppointmentTypeRepository.class);
    private final MockMvc mockMVC = standaloneSetup(new AppointmentTypeController(this.atRepo, this.uRepo)).build();
    private AppointmentType apType, apType2;
    private List<AppointmentType> apTypes;

    @Before
    public void before()
    {
        apType = new AppointmentType();
        apType2 = new AppointmentType();
        apTypes = new ArrayList<>();

        // Set appointment type
        apType.setAppointmentType("testType1");
        apType.setId("testid1");
        apTypes.add(apType);

        apType2.setAppointmentType("testType2");
        apType2.setId("testid2");
        apTypes.add(apType2);
    }

    //@Test
    public void addType() throws Exception
    {
        AppointmentType newType = new AppointmentType();
        ObjectMapper jsonMapper = new ObjectMapper();
        when(atRepo.findByCriteria(new AppointmentType())).thenReturn(apTypes);
        when(atRepo.insert(newType)).thenReturn(newType);

        // new type
        newType.setAppointmentType("testTypeNew");

        // Validate authorization routine
        mockMVC.perform(post("/appointmentTypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(apType))
                        .with(requestStylist))
                .andExpect(status().isUnauthorized());

        // Validate duplicate appointment type check
        mockMVC.perform(post("/appointmentTypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(apType))
                        .with(requestAdmin))
                .andExpect(status().isConflict());
    }
}
