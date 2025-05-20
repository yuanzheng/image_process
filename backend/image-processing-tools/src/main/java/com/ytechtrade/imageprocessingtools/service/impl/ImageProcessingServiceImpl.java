package com.ytechtrade.imageprocessingtools.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ytechtrade.imageprocessingtools.dto.ImageToLatexResponse;
import com.ytechtrade.imageprocessingtools.service.ImageProcessingService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service
public class ImageProcessingServiceImpl implements ImageProcessingService {

    @Override
    public ImageToLatexResponse convertImageToLatex(MultipartFile imageFile) {
        try {
            // 保存文件到临时目录
            Path tempFile = Files.createTempFile("upload_", "_" + imageFile.getOriginalFilename());
            Files.copy(imageFile.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            // TODO: 实现实际的图片处理和LaTeX生成逻辑
            // 目前返回示例LaTeX代码
            return ImageToLatexResponse.builder()
                    .latexCode("\\begin{document}\nHello, World!\n\\end{document}")
                    .message("Successfully processed image")
                    .success(true)
                    .build();

        } catch (IOException e) {
            return ImageToLatexResponse.builder()
                    .message("Error processing image: " + e.getMessage())
                    .success(false)
                    .build();
        }
    }

    @Override
    public String getLatexFromImage(MultipartFile file) {
        // 占位符：硬编码返回示例 LaTeX 字符串
        return """
               \\documentclass{article}
               \\usepackage{graphicx}
               \\title{My Awesome Document from Image (Gradle Build)}
               \\author{AI Converter}
               \\date{\\today}
               \\begin{document}
               \\maketitle
               Hello, this is a sample LaTeX document (from Gradle project) for image: %s (size: %d bytes).
               %% \\includegraphics[width=0.8\\textwidth]{placeholder.png}
               \\end{document}
               """.formatted(file.getOriginalFilename(), file.getSize());
    }

    @Override
    public String saveTemporaryImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Cannot save an empty file.");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        Path tempDir = Files.createTempDirectory("image_uploads_gradle_"); // 稍微改个名以示区别
        Path tempFile = tempDir.resolve(UUID.randomUUID().toString() + extension);

        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        return tempFile.toAbsolutePath().toString();
    }
} 