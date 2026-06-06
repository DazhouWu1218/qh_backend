package com.njht.webyun.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.model.SysRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.management.relation.Role;
import java.util.*;

/**
 * @Author chengqh
 * @Date 2020/12/3
 */
public class TokenUtil {

    public static final String TOKEN="token";

    public static final String USERID="userid";

    public static final String USERNAME="username";
    public static final String ORGID="orgid";

    public static final  String ROLE = "role";

    public static final String AUTHORITIES = "authorities";
    /**
     * 签名用的密钥
     */
    private static final String SIGNING_KEY = "AOTOSIGNINGKEYTOKEN";

//    @Autowired
//    TokenConfig tokenConfig;


    /**
     * 后生成Token
     * 使用Hs256算法
     *
     * @param claims 保存在Payload（有效载荷）中的内容
     * @return token字符串
     */
    public static String genToken( Map<String, Object> claims,int expirationTime) {
        //指定签名的时候使用的签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.SECOND, expirationTime);

        //创建一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //保存在Payload（有效载荷）中的内容
                .setClaims(claims)
                //iat: jwt的签发时间
                .setIssuedAt(new Date())
                //设置过期时间
                .setExpiration(c.getTime())
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, SIGNING_KEY);

        return builder.compact();
    }


    public static String genToken(CurrentUser user, int expirationTime) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(USERID,user.getUserId());
        claims.put(USERNAME,user.getUsername());
        claims.put(ORGID,user.getOrgId());
        claims.put(ROLE,JSON.toJSONString(user.getRoles()));
        claims.put(AUTHORITIES, JSON.toJSONString(user.getAuthorities()));
        return genToken(claims,expirationTime);
    }

    /**
     * 解析token，获取到Payload（有效载荷）中的内容，包括验证签名，判断是否过期
     *
     * @param token
     * @return
     */
    public static Claims parseToken(String token) {
        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(SIGNING_KEY)
                //设置需要解析的token
                .parseClaimsJws(token).getBody();


        return claims;
    }


    public static CurrentUser parseTokenToUser(String token) {
        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(SIGNING_KEY)
                //设置需要解析的token
                .parseClaimsJws(token).getBody();
        CurrentUser user = new CurrentUser();
        user.setUserId((Integer) claims.get(USERID));
        user.setOrgId((Integer)claims.get(ORGID));
        List<SysRole> sysRoles = new ArrayList<>();
        List<JSONObject> sysRolesStr = JSON.parseObject(claims.get(ROLE).toString(),List.class);
        for (JSONObject obj : sysRolesStr) {
            SysRole sysRole = JSON.parseObject(String.valueOf(obj), SysRole.class);
            sysRoles.add(sysRole);
        }
        user.setRoles(sysRoles);

        String authority = claims.get(AUTHORITIES).toString();
        Set<GrantedAuthority> authorities = new HashSet<>();
        if(!StringUtils.isEmpty(authority)){
            List<Map<String,String>> authorityMap = JSONObject.parseObject(authority, List.class);
            for(Map<String,String> role : authorityMap){
                if(!StringUtils.isEmpty(role)) {
                    authorities.add(new SimpleGrantedAuthority(role.get("authority")));
                }
            }
            user.setAuthorities(authorities);
        }
        return user;
    }

    public static String updateToken(Claims claims,int expirationTime) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put(USERID,claims.get(USERID));
        map.put(USERNAME,claims.get(USERNAME));
        map.put(ORGID,claims.get(ORGID));
        map.put(ROLE,claims.get(ROLE));
        map.put(AUTHORITIES,claims.get(AUTHORITIES));
        return genToken(map,expirationTime);

    }
}
