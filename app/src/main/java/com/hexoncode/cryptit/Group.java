package com.hexoncode.cryptit;

import java.io.File;

public class Group {

    private String heading;
    private File[] files;

    public Group(String heading, File[] files) {
        this.heading = heading;
        this.files = files;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public File[] getFiles() {
        return files;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }
}
