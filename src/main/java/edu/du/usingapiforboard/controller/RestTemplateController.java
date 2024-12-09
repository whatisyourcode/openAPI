package edu.du.usingapiforboard.controller;


import com.ctc.wstx.shaded.msv_core.util.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;

@RestController
public class RestTemplateController {

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/")
    public String test() throws URISyntaxException, UnsupportedEncodingException, MalformedURLException {
        String apiUrl = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyListInfoInqire";

        // 공공 API 서비스 키 (공공 데이터 포털에서 발급받은 서비스 키로 변경해야 함)
        String serviceKey = "hnjyrtEC2zqWHKYqgvkat+LGqvyU3KojH8MhJjrkY0g2qyFiiiIxHxunmkDa41ikRkLelxKiV25UadOO1iXGkQ==";


        String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

        // 요청할 주소와 약국명 등 쿼리 파라미터 설정
        // 요청 파라미터
        String address1 = URLEncoder.encode("서울특별시", StandardCharsets.UTF_8);
        String address2 = URLEncoder.encode("강남구", StandardCharsets.UTF_8);
        String pharmacyName = URLEncoder.encode("삼성약국", StandardCharsets.UTF_8);

        // UriComponentsBuilder를 사용하여 URL 생성
        URI url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("ServiceKey", encodedServiceKey) // 인코딩된 서비스 키 추가
                .queryParam("Q0", address1) // 주소(시도)
//                .queryParam("Q1", address2) // 주소(시군구)
//                .queryParam("QN", pharmacyName) // 기관명 (약국 이름)
//                .queryParam("QT", "1") // 월~일요일
//                .queryParam("ORD", "NAME") // 이름 순서 정렬
                .queryParam("pageNo", "1") // 페이지 번호
                .queryParam("numOfRows", "10") // 목록 건수
                .build(true) // 인코딩 방지
                .toUri();



        // 헤더 추가를 위한 HttpHeaders 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json"); // JSON 형식 요청

        // HttpEntity 객체에 헤더 설정
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate 요청
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);


        return response.getBody();

    }

}
