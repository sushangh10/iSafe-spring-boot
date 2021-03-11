package org.example.controller;

import org.example.beans.FileUploaderGetBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import java.io.File;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author sushanghai
 * @Date 2021/3/8
 */
@RestController
public class FileUploaderController {

    public ArrayList<Integer> existChunks = new ArrayList<>();

    Logger logger = LoggerFactory.getLogger(FileUploaderController.class); // log
    /**
     * k=chunkNumber, v=1
     * k=chunkSize, v=10485760
     * k=currentChunkSize, v=0
     * k=totalSize, v=0
     * k=identifier, v=0-testtxt
     * k=filename, v=test.txt
     * k=relativePath, v=test.txt
     * k=totalChunks, v=1
     * k=destinationPath, v=D:\FileReveiver
     */
    @RequestMapping(value = "/api/fileupload", method = RequestMethod.GET)
    private ResponseEntity fileUploadGet(@RequestParam Map<String, Object> requestMap) {
        int responseStatus = 0;
        String fileDirectory = requestMap.get("destinationPath").toString();
        String fileName = requestMap.get("filename").toString();
        Long fileTotalSize = Long.valueOf(requestMap.get("totalSize").toString());
        String absDestinationPath = Paths.get(fileDirectory, fileName).toString();
        int fileStatus = isFileExist(absDestinationPath, fileTotalSize);
        logger.info("File status is： " + fileStatus);
        FileUploaderGetBean fileUploaderGetBean = new FileUploaderGetBean();
        if (fileStatus == 0) {
            fileUploaderGetBean.setSkipUpload(true);
            fileUploaderGetBean.setExistChunks(existChunks);
            responseStatus = 200; // 存在且大小匹配
        } else if (fileStatus == 1) {
            fileUploaderGetBean.setSkipUpload(false);
            fileUploaderGetBean.setExistChunks(existChunks);
            responseStatus = 409; // 存在但大小不匹配
        } else {
            fileUploaderGetBean.setSkipUpload(false);
            fileUploaderGetBean.setExistChunks(this.getExistChunks(fileDirectory, fileName));
            responseStatus = 200; // 存在但大小不匹配
        }
        return ResponseEntity.status(responseStatus).body(fileUploaderGetBean);
    }

    @RequestMapping(value = "/api/fileupload", method = RequestMethod.POST)
    private ResponseEntity fileUploadPost(@RequestParam Map<String, Object> requestMap) {
        Iterator mapIterator = requestMap.keySet().iterator();
        while (mapIterator.hasNext()) {
            Object key = mapIterator.next();
            System.out.println("[get] key: " + key + "; value: " + requestMap.get(key));
        }
        return ResponseEntity.status(201).body("received and return 201");
    }

    /**
     * check whether file is exist
     *
     * return 0 if file exist and fileSize correct
     * return 1 if file exist but fileSize incorrect
     * return -1 if file non-exist
     */
    private int isFileExist(String fileAbsPath, long fileSize) {
        File file = new File(fileAbsPath);
        if (file.exists()) {
            logger.info("File: " + fileAbsPath + " already exist");
            long fileActuallySize = file.length();
            logger.info("File: " + fileAbsPath + " size is: " + fileActuallySize);
            logger.info("File: " + fileAbsPath + " size should be: " + fileSize);
            return fileSize == fileActuallySize ? 0 : 1;
        } else {
            logger.info("File: " + fileAbsPath + " non-exist");
            return -1;
        }
    }

    private ArrayList<Integer> getExistChunks(String directory, String filename) {
        ArrayList<Integer> existChunks = new ArrayList<>();
        String fileChunkNamePattern = filename + "-\\d+";
        Pattern pattern = Pattern.compile(fileChunkNamePattern);
        Matcher matcher;
        File file = new File(directory);
        File[] allFiles = file.listFiles();
        for (int i = 0; i < allFiles.length; i++) {
            matcher = pattern.matcher(allFiles[i].getName());
            if (matcher.find()) {
                String chunkNUm = matcher.group(0).substring(filename.length() + 1);
                existChunks.add(new Integer(chunkNUm));
            }
        }
        return existChunks;
    }
}
