package com.snut_likelion.domain.file;

import com.snut_likelion.global.provider.FileProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/images")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final FileProvider fileProvider;

    @GetMapping(produces = "image/jpeg")
    public Resource getProjectImage(
            @RequestParam("imageName") String imageName
    ) {
        return fileProvider.getFile(imageName);
    }

}
