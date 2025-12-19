package org.example.airbnbbackend.dtos;

import lombok.Data;

@Data
public class SearchRequestDto {
    private String city;
    private String startdate;
    private String enddate;
    private Integer roomsCount;
    private Integer page=0;
    private Integer size=10;
}
