package br.com.sysmap.bootcamp.domain.service.integration;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WalletApi {

    private final RestTemplate restTemplate;

    public WalletApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void creditApiRequest(String token) {
        // Cria o header e adiciona o token de autorização
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        //headers.set("Authorization", "Bearer " + "Basic cmFmYWVsQGVtYWlsLmNvbTokMmEkMTAkNmNNTWEvOU9CaVhZampnTzU2cmxwT0hNVWl4VDIwQ29VYm1YelUuY2cyZzh3bWZFam03a0c");

        // Cria a entidade que inclui o header e o corpo da requisição
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        // Faz a chamada POST para a URL fornecida
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8081/api/wallet/credit",
                HttpMethod.POST,
                entity,
                String.class
        );

        // Retorna a resposta da API
        //response.getBody();
    }
}
