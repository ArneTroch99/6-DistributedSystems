package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HTTPClient {

    private final RestTemplate restTemplate;

    public HTTPClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void putHTTP(String ip, String data) {
        String url = "http://" + ip + ":8081/" + data;
        restTemplate.put(url, String.class);
    }
}
