package com.example.service.impl;

import com.example.dto.RecommendationRequestDTO;
import com.example.mapper.UsageHistoryMapper;
import com.example.service.RecommendationService;
import com.example.vo.RecommendedMaterialVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    @Resource
    private UsageHistoryMapper usageHistoryMapper;

    @Resource
    private ObjectMapper objectMapper;

    @Value("${deepseek.api.key}")
    private String deepseekApiKey;

    @Value("${deepseek.api.url}")
    private String deepseekApiUrl;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    public List<RecommendedMaterialVO> getRecommendations(RecommendationRequestDTO requestDTO) {
        List<Map<String, Object>> history = usageHistoryMapper.getUsageHistory();
        String prompt = createPrompt(requestDTO, history);
        logger.info("向大模型生成的Prompt: {}", prompt);

        try {
            String llmResponseJson = callDeepSeekApi(prompt);
            logger.info("从DeepSeek API收到的原始响应: {}", llmResponseJson);
            return parseLlmResponse(llmResponseJson);
        } catch (IOException e) {
            logger.error("调用DeepSeek API或解析其响应时发生错误", e);
            return Collections.emptyList();
        }
    }

    /**
     * 创建发送给大模型的提示(Prompt)。
     * 在使用JSON mode时，必须在prompt中明确指示输出JSON。
     */
    private String createPrompt(RecommendationRequestDTO requestDTO, List<Map<String, Object>> history) {
        try {
            String historyJson = objectMapper.writeValueAsString(history);
            
            return String.format(
                "分析以下项目需求和历史物资使用记录，并以JSON格式返回推荐的物资清单。" +
                "项目需求: 项目类型是 '%s'，参与人数为 %d人。" +
                "历史使用记录 (JSON格式): %s。" +
                "你的输出必须是一个JSON对象，该对象有一个名为 'recommendations' 的键，其值是一个JSON数组。数组中的每个物资对象应包含 'materialId', 'materialName', 'recommendReason', 'avgUsage' 四个字段。" +
                "如果无法提供推荐，请让 'recommendations' 的值为空数组 `[]`。",
                requestDTO.getProjectType(),
                requestDTO.getParticipantCount(),
                historyJson
            );
        } catch (Exception e) {
            logger.error("创建Prompt时序列化历史数据失败", e);
            return String.format(
                "项目需求: 项目类型是 '%s'，参与人数为 %d人。历史数据无法加载，请根据你的知识库推荐通用物资，并严格按要求的JSON格式返回一个包含 'recommendations' 键的JSON对象。",
                requestDTO.getProjectType(),
                requestDTO.getParticipantCount()
            );
        }
    }

    /**
     * 使用OkHttp调用DeepSeek API，并启用JSON模式。
     */
    private String callDeepSeekApi(String prompt) throws IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        String systemPrompt = "你是一个智能物资推荐助手，你必须总是输出一个结构化的JSON对象。";

        
        Map<String, Object> requestBodyMap = Map.of(
            "model", "deepseek-chat",
            "messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", prompt)
            ),
            "response_format", Map.of("type", "json_object"), 
            "stream", false
        );
        String jsonBody = objectMapper.writeValueAsString(requestBodyMap);

        RequestBody body = RequestBody.create(jsonBody, mediaType);

        Request request = new Request.Builder()
                .url(deepseekApiUrl)
                .header("Authorization", "Bearer " + deepseekApiKey)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (!response.isSuccessful() || responseBody == null) {
                String errorBody = responseBody != null ? responseBody.string() : "No response body";
                logger.error("DeepSeek API调用失败，状态码 {}: {}", response.code(), errorBody);
                throw new IOException("API请求失败，状态码: " + response.code() + ", 响应体: " + errorBody);
            }
            return responseBody.string();
        }
    }

    /**
     * 解析在JSON模式下由大模型返回的响应。
     */
    private List<RecommendedMaterialVO> parseLlmResponse(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode choices = rootNode.path("choices");

            if (choices.isArray() && !choices.isEmpty()) {
                JsonNode firstChoice = choices.get(0);
                String contentJsonString = firstChoice.path("message").path("content").asText();

                
                if (contentJsonString != null && !contentJsonString.trim().isEmpty()) {
                    JsonNode contentNode = objectMapper.readTree(contentJsonString);
                    
                    JsonNode recommendationsNode = contentNode.path("recommendations");
                    
                    if (recommendationsNode.isArray()) {
                         
                        return objectMapper.convertValue(recommendationsNode, new TypeReference<List<RecommendedMaterialVO>>() {});
                    }
                }
            }
            logger.warn("大模型的响应中不包含有效的 'recommendations' 数组。响应: {}", jsonResponse);
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("解析大模型返回的JSON失败: {}", jsonResponse, e);
            return Collections.emptyList();
        }
    }
}