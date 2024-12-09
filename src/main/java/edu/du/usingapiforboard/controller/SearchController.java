package edu.du.usingapiforboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SearchController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/pharmacy")
    public List<Map<String, String>> searchPharmacies(@RequestParam(value = "address1", required = false) String address1) throws JsonProcessingException {

        // 기본값 설정
        if (address1 == null) address1 = "서울특별시"; // 기본 주소 설정

        String apiUrl = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyListInfoInqire";
        String serviceKey = "hnjyrtEC2zqWHKYqgvkat+LGqvyU3KojH8MhJjrkY0g2qyFiiiIxHxunmkDa41ikRkLelxKiV25UadOO1iXGkQ==";
        String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

        // URL 생성 (주소만 사용)
        URI url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("ServiceKey", encodedServiceKey)
                .queryParam("Q0", URLEncoder.encode(address1, StandardCharsets.UTF_8)) // 주소만 사용
                .queryParam("pageNo", "1") // 페이지 번호
                .queryParam("numOfRows", "10") // 목록 건수
                .build(true)
                .toUri();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);


        // API 응답 처리
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode items = rootNode.path("body").path("items");

        // 결과를 리스트에 담기
        List<Map<String, String>> pharmacyList = new ArrayList<>();

        System.out.println(items);
        for (JsonNode item : items) {
            Map<String, String> pharmacyData = new HashMap<>();
            pharmacyData.put("address", item.path("addr").asText()); // 주소
            pharmacyData.put("dayOfWeek", item.path("wday").asText()); // 진료요일
            pharmacyData.put("pharmacyName", item.path("dutyName").asText()); // 기관명 (약국명)
            pharmacyList.add(pharmacyData);
        }

        // JSON 형태로 결과 반환
        return pharmacyList;
    }
}
