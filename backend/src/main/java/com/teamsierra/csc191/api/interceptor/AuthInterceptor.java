package com.teamsierra.csc191.api.interceptor;

import com.teamsierra.csc191.api.model.Appointment;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
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

        RestTemplate restTemplate = new RestTemplate();
        Properties p = new Properties();
        try {
            p.load(this.getClass().getClassLoader().getResourceAsStream("system.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String id = request.getHeader("fbUserId");
        User user = userRepository.findByOAuthId(id);


        if (user != null) { //user exists
            if (user.getToken().equals(request.getHeader("fbAccessToken"))) { //access token is good
                return true;
            } else { //access token is bad
                Map<String, String> challengeVars = new HashMap<String, String>();
                challengeVars.put("accessToken", request.getHeader("fbAccessToken"));
                challengeVars.put("appSecret", p.getProperty("facebookSecret"));
                //TODO challenge header.fbAccessToken with facebook
                String apiUrl = "https://graph.facebook.com/debug_token?input_token={accessToken}&access_token={appSecret}";
                String fbChallenge = restTemplate.getForObject(apiUrl, String.class, challengeVars);
                L.info(fbChallenge);
                //TODO validate the id returned is user.oauthId
            }
        } else { //user does not exist
            //TODO challenge header.fbAccessToken with facebook
            //TODO create user???
        }

        return true;
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
}
