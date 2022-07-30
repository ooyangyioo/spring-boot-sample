package org.yangyi.project.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yangyi.project.bo.UploadBO;

@RestController
@RequestMapping("/system/upload")
@Validated
public class UploadController {

    @PostMapping(value = {"/image"})
    public ResponseEntity image(@RequestPart("info") UploadBO info,
                                @RequestPart("file") MultipartFile file) {

        System.err.println(JSON.toJSONString(info));
        System.err.println(file.getName());

        return ResponseEntity.ok(null);
    }

}
