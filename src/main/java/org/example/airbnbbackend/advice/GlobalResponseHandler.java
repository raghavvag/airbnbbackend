package org.example.airbnbbackend.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * Determines if this advice should be applied to the response
     */
    @Override
    public boolean supports(MethodParameter returnType,
                          Class<? extends HttpMessageConverter<?>> converterType) {
        // Apply to all responses except those already wrapped in ApiResponse
        return !returnType.getParameterType().equals(ApiResponse.class);
    }

    /**
     * Wraps the response body in ApiResponse before it's written
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                 MethodParameter returnType,
                                 MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpRequest request,
                                 ServerHttpResponse response) {

        // Skip wrapping for null responses (e.g., DELETE operations)
        if (body == null) {
            return null;
        }

        // Skip wrapping if already an ApiResponse
        if (body instanceof ApiResponse) {
            return body;
        }

        // Skip wrapping for error responses (handled by GlobalExceptionHandler)
        if (body instanceof ApiError) {
            return body;
        }

        log.debug("Wrapping response in ApiResponse for: {}", request.getURI());

        // Wrap the response in ApiResponse
        ApiResponse<Object> apiResponse = new ApiResponse<>(LocalDateTime.now());
        apiResponse.setData(body);

        return apiResponse;
    }
}

