package org.example.controller;

import org.example.beans.FileUploaderGetBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Map;


/**
 * @Author sushanghai
 * @Date 2021/3/8
 */
@RestController
public class FileUploaderController {
    public boolean skipUpload = true;
    public int[] existChunks = new int[5];

    @RequestMapping(value = "/api/fileupload", method = RequestMethod.GET)
    private ResponseEntity fileUploadGet(@RequestParam Map<String, Object> requestMap) {
        Iterator mapIterator = requestMap.keySet().iterator();
        while (mapIterator.hasNext()) {
            Object key = mapIterator.next();
            System.out.println("[get] key: " + key + "; value: " + requestMap.get(key));
        }
        FileUploaderGetBean fileUploaderGetBean = new FileUploaderGetBean();
        fileUploaderGetBean.setSkipUpload(skipUpload);
        fileUploaderGetBean.setExistChunks(existChunks);
        return ResponseEntity.status(201).body(fileUploaderGetBean);
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
}
