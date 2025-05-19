package com.snut_likeliion.domain.project.dto.response;

import com.snut_likeliion.domain.user.entity.Part;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RetrospectionResponse {

    private Long id;
    private String content;
    private Writer writer;

    public class Writer {
        private Long id;
        private String name;
        private Part part;
    }
}
