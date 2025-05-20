package com.ytechtrade.imageprocessingtools.controller;

import com.ytechtrade.imageprocessingtools.dto.ImageToLatexResponse;
import com.ytechtrade.imageprocessingtools.service.ImageProcessingService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/convert")
public class ImageProcessingController {

    private final ImageProcessingService imageProcessingService;

    @Autowired
    public ImageProcessingController(ImageProcessingService imageProcessingService) {
        this.imageProcessingService = imageProcessingService;
    }

    @PostMapping("/image-to-latex")
    public ResponseEntity<String> imageToLatex(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }
        try {
            String tempFilePath = imageProcessingService.saveTemporaryImage(file);
            System.out.println("File saved temporarily to: " + tempFilePath); // 日志

            String latexContent = imageProcessingService.getLatexFromImage(file);
            return ResponseEntity.ok(latexContent);
        } catch (IOException e) {
            // 实际项目中应使用更完善的日志 e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process image: " + e.getMessage());
        }
    }
} 