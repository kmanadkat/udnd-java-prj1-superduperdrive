package com.udacity.jdnd.cloudstorage.service;

import com.udacity.jdnd.cloudstorage.mapper.CredentialMapper;
import com.udacity.jdnd.cloudstorage.mapper.UserMapper;
import com.udacity.jdnd.cloudstorage.model.Credential;
import com.udacity.jdnd.cloudstorage.model.CredentialForm;
import com.udacity.jdnd.cloudstorage.model.User;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;
    private final HashService hashService;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper, HashService hashService, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
        this.hashService = hashService;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getUserCredentials(String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        if(user == null) {
            throw new InvalidArgumentException("User not found!");
        }
        return credentialMapper.getUserCredentials(user.getUserId());
    }

    public void addNewCredential(CredentialForm credentialForm, String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        if(user == null) {
            throw new InvalidArgumentException("User not found!");
        }
        String keys = hashService.getEncodedSalt();
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), keys);
        Credential newCredential = new Credential(
                credentialForm.getCredentialId(),
                credentialForm.getUrl(),
                credentialForm.getUsername(),
                keys,
                encryptedPassword,
                user.getUserId()
        );
        credentialMapper.insert(newCredential);
    }

    public String getUserCredentialDecoded(Integer credentialId, String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        if(user == null) {
            throw new InvalidArgumentException("User not found!");
        }
        Credential credential = credentialMapper.getCredential(credentialId);
        if(credential == null) {
            throw new InvalidArgumentException("Credential Not Found");
        }
        // Validate Credential User
        if(!Objects.equals(credential.getUserId(), user.getUserId())) {
            throw new InvalidArgumentException("Invalid User Access");
        }
        return encryptionService.decryptValue(credential.getPassword(), credential.getKeys());
    }
}
