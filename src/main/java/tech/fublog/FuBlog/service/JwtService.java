package tech.fublog.FuBlog.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;
import tech.fublog.FuBlog.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String Secret_key = "123";

    public String generateToken(UserEntity user, Collection<SimpleGrantedAuthority> authorities) {
        Algorithm algorithm = Algorithm.HMAC256(Secret_key.getBytes());
        return JWT.create()
//                .withSubject(user.getUsername())
//                .withClaim("id", user.getId())
                .withClaim("user", user.getUsername())
//                .withClaim("fullname",user.getFullName())
//                .withClaim("email",user.getEmail())
//                .withClaim("picture", user.getPicture())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 24 * 30))
                .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String generateRefreshToken(UserEntity user, Collection<SimpleGrantedAuthority> authorities) {
        Algorithm algorithm = Algorithm.HMAC256(Secret_key.getBytes());
        return JWT.create()
//                .withSubject(user.getUsername())
//                .withClaim("id", user.getId())
                .withClaim("user", user.getUsername())
//                .withClaim("fullname",user.getFullName())
//                .withClaim("email",user.getEmail())
//                .withClaim("picture", user.getPicture())
                .withExpiresAt(new Date(System.currentTimeMillis() + 70 * 60 * 1000))
                .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String extractToken(String token) {
        String[] parts = token.split("\\.");
        JSONObject header = new JSONObject(decode(parts[0]));
        JSONObject payload = new JSONObject(decode(parts[1]));
//        System.out.println(header);
//        System.out.println(payload);
        String signature = decode(parts[2]);
//        System.out.println(signature);
        boolean exp = payload.getLong("exp") > (System.currentTimeMillis() / 1000);
        if (exp) {
            return payload.getString("user");
        }
        return null;
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }


}
