package org.example.airbnbbackend.advice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse <T>{
    private LocalDateTime timestamp;
    private  ApiError message;
    private T data;
    public ApiResponse(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }




    public ApiResponse(T data){

        this.data = data;

    }

    public ApiResponse(ApiError message) {
        this.message = message;
    }



}
