package org.example.beans;

/**
 * @Author sushanghai
 * @Date 2021/3/8
 */
public class FileUploaderGetBean {

    private boolean skipUpload;
    private int[] existChunks;

    public boolean isSkipUpload() {
        return skipUpload;
    }

    public void setSkipUpload(boolean skipUpload) {
        this.skipUpload = skipUpload;
    }

    public int[] getExistChunks() {
        return existChunks;
    }

    public void setExistChunks(int[] existChunks) {
        this.existChunks = existChunks;
    }
}
