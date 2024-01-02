package com.komponente.servis2.security;

import com.komponente.servis2.security.CheckSecurity;
import com.komponente.servis2.security.CustomUserDetails;
import com.komponente.servis2.security.service.TokenService;
import com.komponente.servis2.security.strategy.SecurityCheckStrategy;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

@Aspect
@Configuration
public class SecurityAspect {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    private TokenService tokenService;
    private Map<String, SecurityCheckStrategy> checkStrategies;

    public SecurityAspect(TokenService tokenService, Map<String, SecurityCheckStrategy> checkStrategies) {
        this.tokenService = tokenService;
        this.checkStrategies = checkStrategies;
    }

    @Around("@annotation(com.komponente.servis2.security.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //Get method signature
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //Check for authorization parameter
        String token = null;
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if (methodSignature.getParameterNames()[i].equals("authorization")) {
                //Check bearer schema
                if (joinPoint.getArgs()[i].toString().startsWith("Bearer")) {
                    //Get token
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
        }
        System.out.println(1);
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        System.out.println(2);
        Claims claims = tokenService.parseToken(token);
        if (claims == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);
        boolean hasRolePermission = Arrays.asList(checkSecurity.roles()).contains(claims.get("role"));
        boolean hasOwnerPermission = false;

        if (checkSecurity.owners().length > 0) {
            for (String strategyName : checkSecurity.owners()) {
                SecurityCheckStrategy strategy = checkStrategies.get(strategyName);
                if (strategy != null) {
                    Long resourceID = (Long) joinPoint.getArgs()[0]; // Assuming resource ID is the first argument
                    CustomUserDetails userDetails = new CustomUserDetails(new Long(claims.get("id").toString()), claims.get("role").toString());
                    if (strategy.check(userDetails, resourceID)) {
                        hasOwnerPermission = true;
                        break;
                    }
                }
            }
        }

        // Proceed if the user has either role permission or owner permission
        if (hasRolePermission && hasOwnerPermission) {
            return joinPoint.proceed();
        }
        if (claims.get("role").equals("ROLE_ADMIN")) {
            return joinPoint.proceed();
        }

        // If the user neither has the role nor is the owner, return FORBIDDEN
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}

