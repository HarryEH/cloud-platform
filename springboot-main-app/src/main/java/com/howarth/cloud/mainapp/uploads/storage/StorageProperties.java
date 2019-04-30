package com.howarth.cloud.mainapp.uploads.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String tomcatRoot = "/var/lib/tomcat8/webapps/";

    private String location = "/var/lib/tomcat8/webapps/ROOT/upload-dir/";
//    private String location = "upload-dir/";


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
