package com.ytechtrade.imageprocessingtools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageToLatexResponse {
    private String latexCode;
    private String message;
    private boolean success;
} 