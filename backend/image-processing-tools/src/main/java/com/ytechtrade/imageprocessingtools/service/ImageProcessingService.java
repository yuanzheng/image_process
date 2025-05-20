package com.ytechtrade.imageprocessingtools.service;

import org.springframework.web.multipart.MultipartFile;
import com.ytechtrade.imageprocessingtools.dto.ImageToLatexResponse;
import java.io.IOException;


public interface ImageProcessingService {
    ImageToLatexResponse convertImageToLatex(MultipartFile imageFile);
    String getLatexFromImage(MultipartFile file);
    String saveTemporaryImage(MultipartFile file) throws IOException;
} 