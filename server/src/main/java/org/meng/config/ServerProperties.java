package org.meng.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:server.properties")
@ConfigurationProperties(prefix = "org.meng.server")
@SpringBootConfiguration
@Data
public class ServerProperties {
    private String ip;
    private int port;
    private int maxConnects;
    private int customThread;
}
