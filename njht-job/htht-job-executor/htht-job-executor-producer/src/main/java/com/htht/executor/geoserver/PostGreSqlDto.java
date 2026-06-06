package com.htht.executor.geoserver;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "postgresql")
public class PostGreSqlDto {

    public String host;

    public String username;

    public String password;

    public String database;

    public int port;

    public String schema;
}
