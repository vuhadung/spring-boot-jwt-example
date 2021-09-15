package com.fortna.hackathon.config;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")
public class FileStorageConfiguration {

    private String gameDir;

    public String getGameDir() {
        return gameDir;
    }

    public void setGameDir(String gameDir) {
        this.gameDir = gameDir;
    }

    public String getOfficialDir() {
        return new StringBuilder().append(this.gameDir).append(File.separator).append("official").toString();
    }
    
    public String getMapDir() {
        return new StringBuilder().append(this.gameDir).append(File.separator).append("map").toString();
    }
    
    public String getPlayerDir() {
        return new StringBuilder().append(this.gameDir).append(File.separator).append("player").toString();
    }
    
    public String getResultDir() {
        return new StringBuilder().append(this.gameDir).append(File.separator).append("result").toString();
    }

}
