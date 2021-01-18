package com.softarex.microform.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDto<T> {
    private List<T> body;
    private int currentPage;
    private int totalPages;

    public PageDto(List<T> body, int currentPage, int totalPages) {
        this.body = body;
        this.currentPage = currentPage + 1;
        this.totalPages = totalPages;
    }
}
