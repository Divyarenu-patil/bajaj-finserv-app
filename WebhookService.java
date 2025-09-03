package com.bajajfinserv.app.service;

import com.bajajfinserv.app.dto.WebhookRequestDTO;
import com.bajajfinserv.app.dto.WebhookResponseDTO;
import com.bajajfinserv.app.dto.SolutionRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService implements CommandLineRunner {

    @Autowired
    private RestTemplate restTemplate;

    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String SUBMIT_SOLUTION_BASE_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

    // The final SQL query solution
    private static final String SQL_SOLUTION = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, COUNT(e2.EMP_ID) as YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e2.DOB > e1.DOB GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME ORDER BY e1.EMP_ID DESC";

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Bajaj Finserv Health Application Started ===");
        System.out.println("Step 1: Generating webhook...");

        // Step 1: Generate webhook
        WebhookResponseDTO webhookResponse = generateWebhook();

        if (webhookResponse != null && webhookResponse.getWebhook() != null && webhookResponse.getAccessToken() != null) {
            System.out.println("Webhook generated successfully!");
            System.out.println("Webhook URL: " + webhookResponse.getWebhook());
            System.out.println("Access Token: " + webhookResponse.getAccessToken().substring(0, 20) + "...");

            // Step 2: Submit solution
            System.out.println("Step 2: Submitting SQL solution...");
            boolean success = submitSolution(webhookResponse.getWebhook(), webhookResponse.getAccessToken());

            if (success) {
                System.out.println("Solution submitted successfully!");
            } else {
                System.out.println("Failed to submit solution.");
            }
        } else {
            System.out.println("Failed to generate webhook.");
        }

        System.out.println("=== Application process completed ===");
    }

    private WebhookResponseDTO generateWebhook() {
        try {
            // Create request body
            WebhookRequestDTO request = new WebhookRequestDTO("John Doe", "REG12347", "john@example.com");

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create HTTP entity
            HttpEntity<WebhookRequestDTO> entity = new HttpEntity<>(request, headers);

            // Make POST request
            ResponseEntity<WebhookResponseDTO> response = restTemplate.postForEntity(
                GENERATE_WEBHOOK_URL, entity, WebhookResponseDTO.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                System.out.println("Generate webhook failed with status: " + response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error generating webhook: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private boolean submitSolution(String webhookUrl, String accessToken) {
        try {
            // Create solution request
            SolutionRequestDTO solutionRequest = new SolutionRequestDTO(SQL_SOLUTION);

            // Set headers with JWT token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            // Create HTTP entity
            HttpEntity<SolutionRequestDTO> entity = new HttpEntity<>(solutionRequest, headers);

            // Make POST request to webhook URL
            ResponseEntity<String> response = restTemplate.postForEntity(
                SUBMIT_SOLUTION_BASE_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Solution submission response: " + response.getBody());
                return true;
            } else {
                System.out.println("Solution submission failed with status: " + response.getStatusCode());
                System.out.println("Response body: " + response.getBody());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error submitting solution: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}