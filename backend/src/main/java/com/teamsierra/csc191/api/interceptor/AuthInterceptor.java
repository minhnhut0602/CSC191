package com.teamsierra.csc191.api.interceptor;

import com.teamsierra.csc191.api.model.GenericModel;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: scott
 * Date: 10/16/13
 * Time: 11:52 AM
 */
public class AuthInterceptor implements HandlerInterceptor{
    private static final Log L = LogFactory.getLog(AuthInterceptor.class);

    @Autowired
    private UserRepository userRepository;

    private Properties p;
    private String AUTH_TOKEN;
    private String ID;
    private String AUTH_TYPE;

    public AuthInterceptor() {
        super();
        this.p = new Properties();
        try {
            this.p.load(this.getClass().getClassLoader().getResourceAsStream("system.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception{

        // TODO change appmode in system.properties before production
    	// WHEN REMOVED: go to UserControllerIntegrationTest.java and
    	// comment out the specified test method or the tests will fail.
        if (p.getProperty("appmode", "prod").equalsIgnoreCase("dev") &&
            request.getHeader("debug") != null)
        {
            request.setAttribute("authToken", request.getHeader("authToken"));
            request.setAttribute("id", request.getHeader("id"));
            request.setAttribute("authType", request.getHeader("authType"));
            return true;
        }

        // TODO remove??
        AUTH_TOKEN = request.getHeader(p.getProperty("headers.authToken"));
        ID = request.getHeader(p.getProperty("headers.id"));
        AUTH_TYPE = request.getHeader(p.getProperty("headers.authType"));
        L.info(AUTH_TYPE);

        User user = userRepository.findByOAuthId(ID);

        boolean returnValue = false;

        if (user != null) { //user exists
            L.info("user found");
            if (user.getToken().equals(AUTH_TOKEN)) { //access token is good
                returnValue = true;
            } else { //access token is bad
                switch (AUTH_TYPE) {
                    case "client":
                        //client
                        if (facebookChallenge(ID, AUTH_TOKEN, response)) {
                            //update user authToken
                            user.setToken(AUTH_TOKEN);
                            user.setOauthId(ID);
                            user.setActive(true);
                            userRepository.save(user);
                            request.setAttribute("id", user.getId());
                            returnValue = true;
                        } else {
                            returnValue = false;
                        }
                        break;
                    case "stylist":
                        //stylist
                        break;
                    case "admin":
                        //admin
                        break;
                }

            }

        } else { //user does not exist
            L.info("user not found");
            switch (AUTH_TYPE) {
                case "client":
                    //client
                    if (facebookChallenge(ID, AUTH_TOKEN, response)) {
                        //add user to database
                        L.info("adding user to the db");
                        User newUser = new User();
                        newUser.setOauthId(ID);
                        newUser.setToken(AUTH_TOKEN);
                        userRepository.insert(newUser);
                        L.info(newUser +" added");
                        returnValue = true;
                    } else {
                        L.info("fb challenge failed, returning 401");
                        returnValue = false;
                    }
                    break;
                case "stylist":
                    //stylist
                    break;
                case "admin":
                    //admin
                    break;
            }
        }

        if (returnValue) {
            L.info("filling in generic controller");

            //set the id and user type
            request.setAttribute("id", ID);
            switch (AUTH_TYPE) {
                case "client":
                    request.setAttribute("authType", GenericModel.UserType.CLIENT);
                    break;
                case "stylist":
                    request.setAttribute("authType", GenericModel.UserType.STYLIST);
                    break;
                case "admin":
                    request.setAttribute("authType", GenericModel.UserType.ADMIN);
                    break;
            }

            //set the auth_token
            request.setAttribute("authToken", AUTH_TOKEN);
        }

        return returnValue;
    }

    private boolean facebookChallenge(String id, String token, HttpServletResponse response) {

        RestTemplate restTemplate = new RestTemplate();


        String appAccessUrl = "https://graph.facebook.com/oauth/access_token?"+
                              "client_id={id}"+
                              "&client_secret={secret}"+
                              "&grant_type=client_credentials";
        Map<String, String> getAppAccessVars = new HashMap<>();
        getAppAccessVars.put("id", p.getProperty("facebookId"));
        getAppAccessVars.put("secret", p.getProperty("facebookSecret"));

        String appAccess = restTemplate.getForObject(appAccessUrl, String.class, getAppAccessVars);
        appAccess = appAccess.substring(appAccess.indexOf('=')+1);

        String apiUrl = "https://graph.facebook.com/debug_token?"+
                        "input_token={accessToken}"+
                        "&access_token={appToken}";
        Map<String, String> challengeVars = new HashMap<>();
        challengeVars.put("accessToken", token);
        challengeVars.put("appToken", appAccess);

        String fbChallenge = restTemplate.getForObject(apiUrl, String.class, challengeVars);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readValue(fbChallenge, JsonNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonNode data = root.get("data");
        if (data.get("is_valid").asBoolean() &&
            id.equals(data.get("user_id").asText())) {
            return true;
        } else {
            try {
                response.sendError(401);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }


    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {}

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception e) throws Exception {}


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public HashMap<String, String> handleException(Exception e)
    {
        HashMap<String, String> error = new HashMap<>();
        error.put("authError", e.getMessage());
        return error;
    }
}
