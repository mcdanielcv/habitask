package com.habitask.services;


import com.habitask.Dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserApiService {


    @Autowired
    private RestTemplate restTemplate;

    private static final String CLIENT_PERSON_URL = "http://user-authentication:8080";

    public UserDTO getAuthenticatedUser() {
        String url = CLIENT_PERSON_URL + "/auth/autenticateUser";
        return restTemplate.getForObject(url, UserDTO.class);
    }

    public UserDTO getUserDtoById(Long id) {
        String url = CLIENT_PERSON_URL + "/api/users/"+id;
        return restTemplate.getForObject(url, UserDTO.class);
    }
}
