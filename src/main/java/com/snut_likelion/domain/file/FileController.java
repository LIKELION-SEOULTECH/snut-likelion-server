package com.snut_likelion.domain.file;

import com.snut_likelion.global.provider.FileProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileProvider fileProvider;

    // 파일 다운로드
    @GetMapping(value = "/download")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam("fileName") String fileName,
            HttpServletRequest request
    ) throws IOException {
        Resource file = fileProvider.getFile(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext()
                    .getMimeType(file.getFile().getAbsolutePath());
        } catch (IOException e) {
            // 타입 결정 실패 시 로그만 남기고 넘어감
            log.info("파일 타입을 결정할 수 없습니다: {}", fileName);
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(file.getFilename(), StandardCharsets.UTF_8)
                                .build()
                                .toString())
                .body(file);
    }
}
