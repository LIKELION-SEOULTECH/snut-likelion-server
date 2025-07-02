package com.snut_likelion.infra.summary;

import com.snut_likelion.domain.notice.repository.SummaryApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Component
public class SummaryApiClientImpl implements SummaryApiClient {

    public final RestClient restClient;

    public SummaryApiClientImpl(
            @Value("${ai-server.url}") String aiServierUrl
    ) {
        this.restClient = RestClient.create(aiServierUrl);
    }

    @Override
    public String summarize(String content) {
        try {
            SummaryResponse response = restClient.post()
                    .uri("/summarize")
                    .body(new SummaryRequest(content))
                    .retrieve()
                    .body(SummaryResponse.class);
            return response.getSummary();
        } catch (RestClientResponseException ex) {
            // 4xx, 5xx HTTP 에러
            log.warn("요약 API 에러: status={} responseBody={}",
                    ex.getRawStatusCode(), ex.getResponseBodyAsString(), ex);
        } catch (RestClientException ex) {
            log.warn("요약 API 네트워크/직렬화 에러", ex);
        }

        return "요약이 제공되지 않았습니다.";
    }
}
