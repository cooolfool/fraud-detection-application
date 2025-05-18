package com.application.fraud_detection.service;




import com.application.fraud_detection.entity.Transaction;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Service
@Slf4j
public class FraudDetectionService {

    public static final String url =  "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key}")
    private String openApiKey;

    public boolean isFraud(Transaction transaction){
        log.info("Request in isFraud");
        String prompt = "Check if this transaction is fraud: " +
                "Amount=" + transaction.getAmount() + ", " +
                "Merchant=" + transaction.getMerchant() + ", " +
                "Location=" + transaction.getLocation();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{ \"model\": \"gpt-4\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}] }");
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + openApiKey)
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            log.info("Exception is : {}",e.getLocalizedMessage());
            throw new RuntimeException(e);
        }


//        WebClient webClient = WebClient.builder()
//                .baseUrl("https://api.openai.com/v1/chat/completions")
//                .defaultHeader("Authorization", "Bearer YOUR_OPENAI_API_KEY")
//                .build();
//
//        String requestBody = "{ \"model\": \"gpt-4\", \"messages\": [{\"role\": \"user\", \"content\": \"Hello!\"}]}";
//
//        String response = webClient.post()
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();

        log.info("Response recieved from Open API");
         String responseBody = response.body().toString();
    log.info("Response body is : {}",response);
        return responseBody.contains("fraud");
    }

}