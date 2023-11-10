package tech.fublog.FuBlog.Utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.service.JwtService;

import java.util.List;

@Component
public class TokenChecker {
    private static JwtService jwtService = null;

    @Autowired
    public TokenChecker(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public static boolean checkToken(String token) {
        List<String> roles = jwtService.extractTokenToGetRoles(token.substring(7));
        if (roles != null) {
            if (!roles.isEmpty()) {
                return true;
            }
            throw new RuntimeException("Role is null!!");
        }
        throw new RuntimeException("token is wrong or role is not sp!!");
    }


    public static boolean checkRole(String token, boolean lectureIsAllowed) {
        List<String> roles = jwtService.extractTokenToGetRoles(token.substring(7));
        if (roles != null) {
            if (!roles.isEmpty()) {
                for (String role : roles) {
                    if (lectureIsAllowed) {
                        if (!role.toUpperCase().equals("USER")) {
                            return true;
                        }
                    } else {
                        if (!role.toUpperCase().equals("USER")
                                && !role.toUpperCase().equals("LECTURE")) {
                            return true;
                        }
                    }
                    throw new RuntimeException("Role is not sp!!");
                }
                throw new RuntimeException("Role is empty!!");
            }
            throw new RuntimeException("Role is null!!");
        }
        throw new RuntimeException("token is wrong or role is not sp!!");
    }

}
