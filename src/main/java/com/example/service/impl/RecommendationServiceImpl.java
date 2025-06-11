package com.example.service.impl;

import com.example.dto.RecommendationRequestDTO;
import com.example.mapper.UsageHistoryMapper;
import com.example.service.RecommendationService;
import com.example.vo.RecommendedMaterialVO;
import jakarta.annotation.Resource;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Resource
    private UsageHistoryMapper usageHistoryMapper;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    @Override
    public List<RecommendedMaterialVO> getRecommendations(RecommendationRequestDTO requestDTO) {
        // In a real application, you would:
        // 1. Fetch historical data from usageHistoryMapper.
        // List<Map<String, Object>> history = usageHistoryMapper.getUsageHistory();
        // 2. Format a detailed prompt for the LLM.
        // String prompt = createPrompt(requestDTO, history);
        // 3. Call the LLM API.
        // String llmResponse = callOpenAI(prompt);
        // 4. Parse the response and format it as List<RecommendedMaterialVO>.
        // return parseResponse(llmResponse);

        // For this example, we return a mock response.
        RecommendedMaterialVO mockRecommendation = new RecommendedMaterialVO();
        mockRecommendation.setMaterialId(1);
        mockRecommendation.setMaterialName("工业相机");
        mockRecommendation.setRecommendReason("历史同类项目使用率90%");
        mockRecommendation.setAvgUsage(2.5);

        return Collections.singletonList(mockRecommendation);
    }

    private String callOpenAI(String prompt) {
        // This is a simplified example. A real implementation would be more robust.
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String jsonBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
        RequestBody body = RequestBody.create(jsonBody, mediaType);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + openaiApiKey)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return "{}";
            }
            return response.body().string();
        } catch (IOException e) {
            return "{}";
        }
    }
}