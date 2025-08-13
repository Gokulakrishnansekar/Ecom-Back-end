package com.gokul.ecom_website.utils;

import com.gokul.ecom_website.Entity.RolesEntity;
import com.gokul.ecom_website.Entity.UsersModel;
import com.google.api.client.http.javanet.NetHttpTransport;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson2.JacksonFactory;
@Slf4j
@Component
public class JwtUtils {
    private final String secretKey="mysecretkeymysecretkeymysecretkey";
    private final Key key= Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));


    private static final String CLIENT_ID="852423727825-e1ir64huir0ov8dnb9krobrblg1k2qhe.apps.googleusercontent.com" ;
    private static final JacksonFactory jsonFactory = new JacksonFactory();
    private static final NetHttpTransport transport = new NetHttpTransport();

    public String generateToken(UsersModel user) {
        long expiration = 1000 * 60*60*15 ;
        log.info("generate token");


        log.info(user.toString());
            if(user.getRoles() !=null)
        {
            log.info(user.getRoles().toString());
        }

        List<String> userRoles=user.getRoles().stream().map(RolesEntity::getRole_name).toList();
        Map<String,Object> claims=new HashMap<>();
        claims.put("user_id",user.getId());
        claims.put("roles",userRoles);
        return  Jwts.builder().subject(user.getUsername()).claims(claims)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis()+ expiration)).signWith(key)
        .signWith(key, SignatureAlgorithm.HS256)
                .compact();

               // return "null";
    }

    public String extractName(String token)
    {
        return Jwts.parser()

                .setSigningKey((SecretKey) key)
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .getSubject();
    }

    public Integer extractId(String token){
        return (Integer) Jwts.parser() .setSigningKey((SecretKey) key)
                .build()
                .parseClaimsJws(token).getBody().get("id");

    }

    public Boolean validateToken(String token){
        log.warn("insider validation token");
        try{
            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
            return true;
        }
        catch (JwtException e)
        {
            return false;
        }
    }

 public String verifyGoogleToken(String idTokenString) throws GeneralSecurityException, IOException {
        log.info("id:"+CLIENT_ID);
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)

                .setAudience(Collections.singletonList(CLIENT_ID))

                .build();



        GoogleIdToken idToken = verifier.verify(idTokenString);


            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();


            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            log.info(email);
            return email;


    }
}


