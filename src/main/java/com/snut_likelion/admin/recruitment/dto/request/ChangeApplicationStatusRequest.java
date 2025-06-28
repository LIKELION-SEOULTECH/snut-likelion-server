package com.snut_likelion.admin.recruitment.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeApplicationStatusRequest {

    private List<Long> ids;

    public ChangeApplicationStatusRequest(List<Long> ids) {
        this.ids = ids;
    }
}
