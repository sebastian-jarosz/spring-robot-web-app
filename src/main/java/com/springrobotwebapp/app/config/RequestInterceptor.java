package com.springrobotwebapp.app.config;

import com.springrobotwebapp.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Autowired
    User user;

    private ArrayList<String> excludedItemsList = new ArrayList<>(Arrays.asList("css", "js", "png", "welcome", "logout", "error"));

    private boolean isStringContainsTextFromArray(String string, ArrayList<String> textList) {
        return textList.stream().anyMatch(string::contains);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        if (user.getUsername() != null || request.getMethod().equals(RequestMethod.POST) || isStringContainsTextFromArray(request.getRequestURL().toString(), excludedItemsList)){
            return true;
        } else {
            System.out.println(request.getRequestURL().toString());
            response.sendRedirect("/welcome");
            return false;
        }
    }

}
