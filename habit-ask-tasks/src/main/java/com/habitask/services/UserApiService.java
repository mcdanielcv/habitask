package com.habitask.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.habitask.Dto.*;

@Service
public class UserApiService {


    @Autowired
    private RestTemplate restTemplate;

    private static final String CLIENT_PERSON_URL = "http://user-authentication:8080";

    public com.habitask.Dto.UserDTO getAuthenticatedUser(String token) {
        // Crear los encabezados con el token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Crear la entidad HTTP con los encabezados
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Realizar la solicitud GET con RestTemplate
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                CLIENT_PERSON_URL,
                HttpMethod.GET,
                entity,
                UserDTO.class
        );

        return response.getBody();
    }

    public com.habitask.Dto.UserDTO getUserDtoById(Long id) {
        String url = CLIENT_PERSON_URL + "/api/users/"+id;
        return restTemplate.getForObject(url, UserDTO.class);
    }
}
