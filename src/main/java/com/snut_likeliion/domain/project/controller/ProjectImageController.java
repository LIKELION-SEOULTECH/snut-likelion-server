package com.snut_likeliion.domain.project.controller;

import com.snut_likeliion.domain.project.service.ProjectImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProjectImageController {

    private final ProjectImageService projectImageService;

    @DeleteMapping("/api/v1/projects/{projectId}/images")
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectImage(
            @PathVariable("projectId") Long projectId,
            @RequestParam("imageName") String imageName
    ) {
        projectImageService.removeImage(projectId, imageName);
    }
}
