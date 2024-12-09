package edu.du.usingapiforboard;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@SpringBootTest
class UsingApiForBoardApplicationTests {


    @Test
    void man() throws IOException {
        String serviceKey = "hnjyrtEC2zqWHKYqgvkat%2BLGqvyU3KojH8MhJjrkY0g2qyFiiiIxHxunmkDa41ikRkLelxKiV25UadOO1iXGkQ%3D%3D";
        String baseUrl = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyListInfoInqire";


        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("ServiceKey", serviceKey)
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", "10");

        ResponseEntity<String> response = restTemplate.getForEntity(uriBuilder.toUriString(),String.class);

        System.out.println(response.getBody());
    }

}
