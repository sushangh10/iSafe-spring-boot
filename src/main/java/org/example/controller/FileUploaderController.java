package org.example.controller;

import org.example.beans.FileUploaderGetBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;


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
    private ResponseEntity fileUploadPost(HttpServletRequest request) throws IOException {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        String fileChunkName = request.getParameter("filename") + "-" + request.getParameter("chunkNumber");
        String fileDirectory = request.getParameter("destinationPath");
        String absDestinationPath = Paths.get(fileDirectory, fileChunkName).toString();
        File fileChunk = new File(absDestinationPath);
        FileOutputStream fileOutputStream = new FileOutputStream(fileChunk);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(file.getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        System.out.println(request.getParameter("chunkNumber"));
        System.out.println(request.getParameter("totalChunks"));
        if (request.getParameter("chunkNumber") == request.getParameter("totalChunks")) {
            System.out.println("Start Merge");
            this.mergeChunks(fileDirectory, request.getParameter("filename"), Integer.parseInt(request.getParameter("totalChunks")), Integer.parseInt(request.getParameter("chunkSize")));
        }
        return ResponseEntity.status(201).body("received and saved, return 201");
    }

    @RequestMapping(value = "/api/fileupload", method = RequestMethod.PUT)
    private ResponseEntity fileUploadPut(@RequestParam Map<String, Object> requestMap) throws IOException {
        System.out.println("Start Merge");
        boolean result = this.mergeChunks(
                requestMap.get("destinationPath").toString(),
                requestMap.get("filename").toString(),
                Integer.parseInt(requestMap.get("totalChunks").toString()),
                Integer.parseInt(requestMap.get("chunkSize").toString())
        );
        if (result) {
            return ResponseEntity.status(200).body("received and saved, return 200");
        } else {
            return ResponseEntity.status(400).body("received and failed to saved, return 400");
        }
    }

    private boolean mergeChunks(String dir, String filename, int totalChunks, int chunkSize) throws IOException {
        String fileAbsName = Paths.get(dir, filename).toString();
        File file = new File(fileAbsName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream((fileOutputStream));
        byte[] bytes = new byte[chunkSize];
        for (int i = 1; i < totalChunks+1; i++) {
            String chunkAbsName = Paths.get(fileAbsName + "-" + i).toString();
            System.out.println("writing file: " + chunkAbsName);
            File chunkFile = new File(chunkAbsName);
            FileInputStream fileInputStream = new FileInputStream(chunkFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            int count;
            while ( (count = bufferedInputStream.read(bytes)) > 0) {
                bufferedOutputStream.write(bytes, 0, count);
            }
            bufferedInputStream.close();
            chunkFile.delete();
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        return true;
    }

//    @RequestMapping(value = "/api/fileupload", method = RequestMethod.POST)
//    private ResponseEntity fileUploadPost(@RequestParam("file") MultipartFile file) {
//        try {
//            BufferedOutputStream out = new BufferedOutputStream(
//                    new FileOutputStream(new File(
//                            file.getOriginalFilename())));
//            System.out.println(file.getName());
//            out.write(file.getBytes());
//            out.flush();
//            out.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(403).body("received and saved forbidden, return 403");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(403).body("received and saved failed, return 400");
//        }
//        return ResponseEntity.status(201).body("received and saved, return 201");
//    }

//    @RequestMapping(value = "/api/fileupload", method = RequestMethod.POST)
//    private ResponseEntity fileUploadPost(@RequestParam Map<String, Object> requestMap) {
////        Iterator mapIterator = requestMap.keySet().iterator();
////        while (mapIterator.hasNext()) {
////            Object key = mapIterator.next();
////            System.out.println("[get] key: " + key + "; value: " + requestMap.get(key));
////        }
//        String sliceFileName = requestMap.get("filename").toString() + "-" + requestMap.get("chunkNumber").toString();
//        try {
//            BufferedOutputStream outputStream = new BufferedOutputStream(
//                    new FileOutputStream(new File(
//                            sliceFileName)));
//            outputStream.write(requestMap.get());
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(403).body("received and return 403 cause file no found");
//        }
//        return ResponseEntity.status(201).body("received and saved, return 201");
//    }

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
