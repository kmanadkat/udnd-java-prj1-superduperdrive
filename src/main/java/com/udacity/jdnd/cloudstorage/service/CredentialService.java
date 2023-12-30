package com.udacity.jdnd.cloudstorage.service;

import com.udacity.jdnd.cloudstorage.mapper.CredentialMapper;
import com.udacity.jdnd.cloudstorage.mapper.UserMapper;
import com.udacity.jdnd.cloudstorage.model.Credential;
import com.udacity.jdnd.cloudstorage.model.CredentialForm;
import com.udacity.jdnd.cloudstorage.model.User;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;
    private final HashService hashService;

    public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper, HashService hashService) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
        this.hashService = hashService;
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
        String hashedPassword = hashService.getHashedValue(user.getPassword(), keys);
        Credential newCredential = new Credential(
                credentialForm.getCredentialId(),
                credentialForm.getUrl(),
                credentialForm.getUsername(),
                keys,
                hashedPassword,
                user.getUserId()
        );
        credentialMapper.insert(newCredential);
    }
}
