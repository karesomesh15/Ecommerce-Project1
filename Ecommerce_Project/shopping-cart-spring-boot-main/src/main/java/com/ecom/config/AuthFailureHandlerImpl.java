package com.ecom.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ecom.dao.UserDao;
import com.ecom.model.UserDtls;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthFailureHandlerImpl.class);

    @Autowired
    private UserDao userRepository;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String email = request.getParameter("username");
        UserDtls userDtls = userRepository.findByEmail(email);

        if (userDtls != null) {
            if (userDtls.getIsEnable()) {
                if (userDtls.getAccountNonLocked()) {
                    if (userDtls.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
                        userService.increaseFailedAttempt(userDtls);
                        logger.warn("User '{}' failed to log in. Failed attempt #{}", email, userDtls.getFailedAttempt());
                    } else {
                        userService.userAccountLock(userDtls);
                        logger.warn("User '{}' account is locked due to multiple failed login attempts.", email);
                        exception = new LockedException("Your account is locked! Failed attempt 3");
                    }
                } else {
                    logger.warn("User '{}' attempted to log in but the account is locked.", email);
                }
            } else {
                logger.warn("Inactive user '{}' attempted to log in.", email);
            }
        } else {
            logger.warn("Failed login attempt with invalid credentials for '{}'.", email);
        }

        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
