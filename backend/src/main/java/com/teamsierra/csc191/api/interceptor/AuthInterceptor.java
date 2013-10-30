package com.teamsierra.csc191.api.interceptor;

import com.teamsierra.csc191.api.controller.GenericController;
import com.teamsierra.csc191.api.model.GenericModel;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
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

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        //TODO implement authentication

        if (handler instanceof GenericController)
        {
            GenericController controller = (GenericController)handler;
            String cookieToken = "";
            Cookie[] cookies = request.getCookies();

            controller.setId("1");
            controller.setAuthType(GenericModel.UserType.CLIENT);

            for (Cookie cookie: cookies)
            {
                controller.setAuthToken(cookie.getValue());
            }

            controller.setAuthToken("1234567890abcdef");
            controller.setUserRepository(userRepository);
        }
        return true;

        /*
        final String accessToken = "accessToken";

        String id = request.getHeader("fbUserId");
        User user = userRepository.findByOAuthId(id);


        if (user != null) { //user exists
            if (user.getToken().equals(request.getHeader("fbAccessToken"))) { //access token is good
                return true;
            } else { //access token is bad
                //TODO validate the id returned is user.oauthId
                facebookChallenge(request.getHeader("fbUserId"), request.getHeader("fbAccessToken"));
            }
        } else { //user does not exist
            //TODO challenge header.fbAccessToken with facebook
            facebookChallenge(request.getHeader("fbUserId"), request.getHeader("fbAccessToken"));
            //TODO create user???
        }

        return true;
        */
    }

    public boolean facebookChallenge(String id, String token) {
        Properties p = new Properties();
        try {
            p.load(this.getClass().getClassLoader().getResourceAsStream("system.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RestTemplate restTemplate = new RestTemplate();


        String appAccessUrl = "https://graph.facebook.com/oauth/access_token?"+
                              "client_id={id}"+
                              "&client_secret={secret}"+
                              "&grant_type=client_credentials";
        Map<String, String> getAppAccessVars = new HashMap<>();
        getAppAccessVars.put("id", "197300770451342");
        getAppAccessVars.put("secret", p.getProperty("facebookSecret"));

        String appAccess = restTemplate.getForObject(appAccessUrl, String.class, getAppAccessVars);
        L.info(appAccess);
        appAccess = appAccess.substring(appAccess.indexOf('=')+1);

        String apiUrl = "https://graph.facebook.com/debug_token?"+
                        "input_token={accessToken}"+
                        "&access_token={appToken}";
        Map<String, String> challengeVars = new HashMap<>();
        challengeVars.put("accessToken", token);
        challengeVars.put("appToken", appAccess);

        String fbChallenge = restTemplate.getForObject(apiUrl, String.class, challengeVars);
        L.info(fbChallenge);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView)
    { }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception e) throws Exception
    {}
}
