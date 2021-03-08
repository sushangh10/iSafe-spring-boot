package org.example.beans;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author sushanghai
 * @Date 2021/3/8
 */
public class FIleForm {
    /**
     * api/fileupload?
     *  chunkNumber=1&
     *  chunkSize=1048576&
     *  currentChunkSize=1261&
     *  totalSize=1261&
     *  identifier=1261-Codeexe-lnk&filename=Code.exe%20-%20%E5%BF%AB%E6%8D%B7%E6%96%B9%E5%BC%8F.lnk&
     *  relativePath=Code.exe%20-%20%E5%BF%AB%E6%8D%B7%E6%96%B9%E5%BC%8F.lnk&
     *  totalChunks=1
     */

    private int chunkNumber;
    private int chunkSize;
    private int currentChunkSize;
    private int totalSize;
    private String identifier;
    private String filename;
    private String relativePath;
    private int totalChunks;

}
