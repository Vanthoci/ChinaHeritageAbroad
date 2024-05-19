package com.ujcms.cms.ext.web.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EmotionHelper {
    private static final String API_KEY = "xBEwIkgOldScb8Xw3DdbHACk";
    private static final String SECRET_KEY = "DPNTVCqTgfu5Ja5rv7NkqcvXKgvxJvqo";

    public static void main(String[] args) {
        try {
            double positiveLevel = getPositiveLevel("还不不错给");
            System.out.println("Positive Probability: " + positiveLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getPositiveLevel(String text) throws IOException {
        String accessToken = getAccessToken();
        String response = sentimentClassify(text, accessToken);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        JsonNode items = rootNode.path("items");
        if (items.isArray() && items.has(0)) {
            JsonNode item = items.get(0);
            return item.path("positive_prob").asDouble();
        }
        return 0.0; // Return default value if the required data isn't found
    }

    private static String sentimentClassify(String text, String accessToken) throws IOException {
        URL url = new URL("https://aip.baidubce.com/rpc/2.0/nlp/v1/sentiment_classify?charset=UTF-8&access_token=" + accessToken);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.createObjectNode();
        ((ObjectNode) jsonNode).put("text", text);
        String requestBody = mapper.writeValueAsString(jsonNode);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    private static String getAccessToken() throws IOException {
        URL url = new URL("https://aip.baidubce.com/oauth/2.0/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        String data = "grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = data.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.toString());
            return node.path("access_token").asText();
        }
    }
}
