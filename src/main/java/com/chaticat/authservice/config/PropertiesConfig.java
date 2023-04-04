package com.chaticat.authservice.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        PropertiesConfig.JwtProperties.class,
})
public class PropertiesConfig {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @ConfigurationProperties(prefix = "jwt")
    public static class JwtProperties {

        /**
         * Use this variable for set expiration time of access token
         */
        Long expirationAccessToken;

        /**
         * Use this variable for set expiration time of refresh token
         */
        Long expirationRefreshToken;

        /**
         * Use this variable for set key that allow to validate the JWT
         */
        String secretKey;
    }
}
