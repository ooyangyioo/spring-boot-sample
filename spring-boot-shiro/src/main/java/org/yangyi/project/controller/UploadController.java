package org.yangyi.project.controller;

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
        return ResponseEntity.ok(null);
    }

}
