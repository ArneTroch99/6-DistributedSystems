package be.uantwerpen.fti.ei.distributed.project.reallifesaveicons.NamingServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class HTTPClient {

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(NamingServerServiceImpl.class);
    private final RestTemplate restTemplate;

    public HTTPClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void putHTTP(String ip, String data){
        String url = "http://" + ip + ":8081/" + data;
        logger.info(url);
        restTemplate.put(url, String.class);
    }

    public ResponseEntity<String> getHTTP(String ip, String data){
        String url = "http://" + ip + ":8081/" + data;
        return restTemplate.getForEntity(url, String.class);
    }
}
