package com.study.Utils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.study.common.CommonConstants;
import com.study.entity.User;
import com.study.entity.UserToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * <p>
 * JWT token 生成工具类
 * </p>
 *
 * @author Caratacus
 */
    public class JwtUtils {
        public static final String START = "Bearer ";
        public static String generateToken(User user, int expire) throws Exception {
            UserToken userToken = new UserToken();
            userToken.setUserId(user.getId());
            userToken.setUsername(user.getUserName());
            String token = Jwts.builder()
                    .setSubject(userToken.getUsername())
                    .claim(CommonConstants.CONTEXT_USER_ID, userToken.getUserId())
                    .claim(CommonConstants.RENEWAL_TIME,new Date(System.currentTimeMillis()+expire))
                    .setExpiration(new Date(System.currentTimeMillis()+expire))
                    .signWith(SignatureAlgorithm.HS256, CommonConstants.JWT_PRIVATE_KEY)
                    .compact();
            token = START+token;
            return token;
        }

    /**
     * token解析
     */
        public static UserToken getInfoFromToken(String token) throws Exception {
            Claims claims = Jwts.parser()
                    .setSigningKey(CommonConstants.JWT_PRIVATE_KEY).parseClaimsJws(token)
                    .getBody();
            UserToken userToken = new UserToken();
            userToken.setUsername(claims.getSubject());
            userToken.setUserId((Integer) claims.get(CommonConstants.CONTEXT_USER_ID));
            return userToken;
        }
}
