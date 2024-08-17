package com.yangyi.project.other.service.impl;

import com.yangyi.project.other.service.SftpService;
import org.springframework.integration.file.remote.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class SftpServiceImpl implements SftpService {


    @Override
    public void uploadFile(File file) {

    }

    @Override
    public void uploadFile(byte[] bytes, String name) {

    }

    @Override
    public void upload(byte[] bytes, String filename, String path) {

    }

    @Override
    public String[] listFile(String path) {
        return new String[0];
    }

    @Override
    public List<FileInfo> listALLFile(String path) {
        return List.of();
    }

    @Override
    public File downloadFile(String fileName, String savePath) {
        return null;
    }

    @Override
    public InputStream readFile(String fileName) {
        return null;
    }

    @Override
    public boolean existFile(String filePath) {
        return false;
    }

    @Override
    public boolean mkdir(String dirName) {
        return false;
    }

    @Override
    public boolean deleteFile(String fileName) {
        return false;
    }

    @Override
    public void uploadFiles(List<MultipartFile> files, boolean deleteSource) throws IOException {

    }

    @Override
    public void uploadFiles(List<MultipartFile> files) throws IOException {

    }

    @Override
    public void uploadFile(MultipartFile multipartFile) throws IOException {

    }

    @Override
    public String listFileNames(String dir) {
        return "";
    }

    @Override
    public File getFile(String dir) {
        return null;
    }

    @Override
    public List<File> mgetFile(String dir) {
        return List.of();
    }

    @Override
    public boolean rmFile(String file) {
        return false;
    }

    @Override
    public boolean mv(String sourceFile, String targetFile) {
        return false;
    }

    @Override
    public File putFile(String dir) {
        return null;
    }

    @Override
    public List<File> mputFile(String dir) {
        return List.of();
    }

    @Override
    public String nlstFile(String dir) {
        return "";
    }
}
