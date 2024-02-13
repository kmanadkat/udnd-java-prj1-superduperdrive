package com.udacity.jdnd.cloudstorage.service;

import com.udacity.jdnd.cloudstorage.mapper.FileMapper;
import com.udacity.jdnd.cloudstorage.mapper.UserMapper;
import com.udacity.jdnd.cloudstorage.model.File;
import com.udacity.jdnd.cloudstorage.model.User;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class FileService {
    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public List<File> getUserFiles(String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        if(user == null) {
            throw new InvalidArgumentException("User not found!");
        }
        return fileMapper.getFileByUser(user.getUserId());
    }

    public File getFileByNameId(Integer fileId, String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        if(user == null) {
            throw new InvalidArgumentException("User not found!");
        }
        // Validate File
        File oldFile = fileMapper.getFileById(fileId);
        if(oldFile == null || !Objects.equals(oldFile.getUserId(), user.getUserId())) {
            throw new InvalidArgumentException("Not Allowed!");
        }
        return oldFile;
    }

    public void addNewFile(MultipartFile file, String username) throws InvalidArgumentException, IOException {
        // Validate User
        User user = userMapper.getUser(username);
        if(user == null) {
            throw new InvalidArgumentException("User not found!");
        }

        // Validate Unique File Name
        String fileName = file.getOriginalFilename();
        File oldFile = fileMapper.getFileByNameId(fileName, user.getUserId());
        if(oldFile != null) {
            throw new InvalidArgumentException("File with same name already exists!");
        }
        byte[] fileData = file.getBytes();
        String contentType = file.getContentType();
        String fileSize = String.valueOf(file.getSize());
        fileMapper.insert(new File(null, fileName, contentType, fileSize, user.getUserId(), fileData));
    }

    public void deleteUserFile(Integer fileId, String username) throws InvalidArgumentException, IOException {
        // Validate User
        User user = userMapper.getUser(username);
        if(user == null) {
            throw new InvalidArgumentException("User not found!");
        }
        File oldFile = fileMapper.getFileById(fileId);
        if(oldFile == null || !Objects.equals(oldFile.getUserId(), user.getUserId())) {
            throw new InvalidArgumentException("Not Allowed!");
        }
        fileMapper.delete(oldFile.getFileId());
    }
}
