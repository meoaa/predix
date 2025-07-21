package project.predix.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties (String secret, long accessTtl, long refreshTtl){
}
