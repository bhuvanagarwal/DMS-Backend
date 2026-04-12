package com.dms.filter;

import java.io.IOException;
import java.net.InetAddress;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.dms.user.entity.ApiAuditLog;
import com.dms.user.repository.ApiAuditLogRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.util.ContentCachingRequestWrapper;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Run this filter first
public class ApiAuditLogFilter extends OncePerRequestFilter {

    private final ApiAuditLogRepository auditLogRepository;

    public ApiAuditLogFilter(ApiAuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String txnId = UUID.randomUUID().toString().substring(0, 12); // short unique ID

        // Wrap request and response to cache body
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        String requestBody = "";
        String responseBody = "";
        int statusCode = 0;
        String errorMsg = null;

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);

            // Read bodies after controller execution
            byte[] requestBytes = wrappedRequest.getContentAsByteArray();
            requestBody = requestBytes.length > 0 ? new String(requestBytes) : "";

            byte[] responseBytes = wrappedResponse.getContentAsByteArray();
            responseBody = responseBytes.length > 0 ? new String(responseBytes) : "";

            statusCode = wrappedResponse.getStatus();

        } catch (Exception ex) {
            errorMsg = ex.getMessage();
            statusCode = 500;
            throw ex; // rethrow so error handlers can still work
        } finally {
            // Copy response body back to client
            wrappedResponse.copyBodyToResponse();

            // Save audit log (can be made @Async later for better performance)
            saveAuditLog(txnId, wrappedRequest, requestBody, responseBody,
                    statusCode, (int) (System.currentTimeMillis() - startTime), errorMsg);
        }
    }

    private void saveAuditLog(String txnId,
            HttpServletRequest request,
            String requestBody,
            String responseBody,
            int statusCode,
            int executionTimeMs,
            String errorMessage) {

        // Truncate large bodies to prevent DB bloat
        String safeRequestBody = truncateBody(requestBody, 10000); // max 10KB
        String safeResponseBody = truncateBody(responseBody, 10000);

        String serviceName = determineServiceName(request.getRequestURI().toString());
        ApiAuditLog log = ApiAuditLog.builder()
                .txnId(txnId)
                .endpoint(request.getRequestURI())
                .method(request.getMethod())
                .requestBody(safeRequestBody)
                .responseBody(safeResponseBody)
                .statusCode(statusCode)
                .clientIp(getClientIp(request))
                .systemIp(getSystemIp())
                .serviceName(serviceName) 
                .userId(getCurrentUserId())
                .executionTimeMs(executionTimeMs)
                .errorMessage(errorMessage)
                .createdAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }

    private String determineServiceName(String endPoint) {
       String serviceName = "";
       switch(endPoint){
        case "/api/users/create":
            serviceName = "USER_CREATE_SERVICE";
            break;

        case "/api/users/login":
            serviceName = "USER_LOGIN_SERVICE";
            break;
        default:
            serviceName = "NA";
       }
        return serviceName;
    }

    private String truncateBody(String body, int maxLength) {
        if (body == null || body.isEmpty())
            return null;
        return body.length() > maxLength
                ? body.substring(0, maxLength) + " ...[TRUNCATED]"
                : body;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String getSystemIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "unknown";
        }
    }

    // Optional: Skip logging for certain paths (health checks, static files, etc.)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator/") ||
                path.startsWith("/swagger") ||
                path.equals("/health") ||
                path.equals("/favicon.ico");
    }

    // TODO: Replace with actual logic from Spring Security / JWT / Session
    private String getCurrentUserId() {
        // Example: SecurityContextHolder.getContext().getAuthentication().getName();
        return null; // or "anonymous"
    }
}