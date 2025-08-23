package com.optimi.painel.controller.model.dto;


import java.util.List;

public record SliceResponse<T>(
        List<T> content,
        int page,
        int size,
        boolean hasPrevious,
        boolean hasNext
) {}

