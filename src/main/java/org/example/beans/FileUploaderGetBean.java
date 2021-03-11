package org.example.beans;

import java.util.ArrayList;

/**
 * @Author sushanghai
 * @Date 2021/3/8
 */
public class FileUploaderGetBean {

    private boolean skipUpload;
    private ArrayList<Integer> existChunks;

    public boolean isSkipUpload() {
        return skipUpload;
    }

    public void setSkipUpload(boolean skipUpload) {
        this.skipUpload = skipUpload;
    }

    public ArrayList<Integer> getExistChunks() {
        return existChunks;
    }

    public void setExistChunks(ArrayList<Integer> existChunks) {
        this.existChunks = existChunks;
    }
}
