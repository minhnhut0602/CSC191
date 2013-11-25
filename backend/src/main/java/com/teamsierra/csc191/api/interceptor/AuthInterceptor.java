package com.teamsierra.csc191.api.interceptor;

import com.teamsierra.csc191.api.exception.GenericException;
import com.teamsierra.csc191.api.model.GenericModel;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
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

    /**
     * Validate the id and auth token passed to the API as credentials when making calls. This method is bypassed
     * if the request is attempting to get to the AuthenticationController class which is used at logon for staff
     * to generate a new auth token to be passed to the REST API.
     *
     * @param request HTTP request object
     * @param response HTTP response object
     * @param handler I have no idea what this does.
     * @return If true, continue through the api, else handle the response and block further execution.
     * @throws Exception If credentials do not validate, throw an exception killing the execution and returning an
     * error to the caller
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception{

        L.info("getRequestURI() "+ request.getRequestURI());
        if (request.getRequestURI().matches(".*/authorize\\?.*")) {
            return true;
        }

        /*
         * TODO change appmode in system.properties before production
    	 * WHEN REMOVED: go to UserControllerIntegrationTest.java and
    	 * comment out the specified test method or the tests will fail.
    	 */
        if (p.getProperty("appmode", "prod").equalsIgnoreCase("dev") &&
            request.getHeader("debug") != null) {

            request.setAttribute("authToken", request.getHeader("authToken"));
            request.setAttribute("id", request.getHeader("id"));
            request.setAttribute("authType", request.getHeader("authType"));
            return true;
        }

        String AUTH_TOKEN = request.getHeader(p.getProperty("headers.authToken"));

        /*
         * Find the user who matches the given auth token.
         */
        L.info("looking for user with auth token: "+ AUTH_TOKEN);
        User user = null;
        user = userRepository.findByToken(AUTH_TOKEN);

        /*
         * Check to see if the supplied auth token belongs to a user.
         */
        if (user != null) {
            /*
             * User was found for supplied auth token, credentials verified, fill attributes and continue execution.
             */
            L.info("user found: "+ user);
            L.info("setting attributes and returning true");

            request.setAttribute("id", user.getId());
            request.setAttribute("authToken", user.getToken());
            request.setAttribute("authType", user.getType());
            return true;

        } else {
            /*
             * No user found for supplied auth token.
             */
            L.info("no user found");

            /*
             * Ask facebook if the auth token passed in is valid for a facebook session.
             */
            L.info("asking facebook if auth token is valid for a facebook session");
            String ID = null;
            if ((ID = facebookChallenge(AUTH_TOKEN)) != null) {
                /*
                 * Facebook session is valid, either auth token changed or user is new client.
                 */
                L.info("auth token is valid for facebook session");

                /*
                 * Check for user in database that matches facebook ID.
                 */
                L.info("looking for user in database that matches facebook id: "+ ID);
                user = userRepository.findByOAuthId(ID);
                if (user != null) {
                    /*
                     * User was found for this facebook session. update their auth token and continue execution.
                     */
                    L.info("user was found "+ user);

                    L.info("updating auth token in database");
                    user.setToken(AUTH_TOKEN);
                    userRepository.save(user);

                    L.info("setting attributes and returning true");
                    request.setAttribute("id", user.getId());
                    request.setAttribute("authToken", user.getToken());
                    request.setAttribute("authType", user.getType());
                    return true;
                } else {
                    /*
                     * Facebook session is valid but no user found. Create new user and throw new user exception.
                     */
                    L.info("user not found in database");

                    L.info("adding new user to the db");
                    user = new User();
                    user.setOauthId(ID);
                    user.setType(GenericModel.UserType.CLIENT);
                    user.setToken(AUTH_TOKEN);
                    user.setActive(true);

                    userRepository.insert(user);

                    L.info("new user added to database "+ user);

                    L.info("setting attributes and returning true");
                    request.setAttribute("id", user.getId());
                    request.setAttribute("authToken", user.getToken());
                    request.setAttribute("authType", user.getType());
                    return true;
                }
            } else {
                /*
                 * No facebook session found, auth token is not valid, throw exception and halt execution.
                 */
                throw new GenericException("invalid auth token", HttpStatus.UNAUTHORIZED, L);
            }

        }
    }

    private String facebookChallenge(String token) throws Exception{

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
        if (data.get("is_valid").asBoolean()) {
            return data.get("user_id").asText();
        } else {
            return null;
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


    @ExceptionHandler(GenericException.class)
    @ResponseBody
    public ResponseEntity<HashMap<String, String>> handleException(Exception e)
    {
        HashMap<String, String> error = new HashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        error.put("authError", e.getMessage());
        if (e instanceof GenericException)
        {
            status = ((GenericException)e).getStatus();
        }

        return new ResponseEntity<>(error, status);
    }
}
