package com.snut_likeliion.domain.user.entity;

import com.snut_likeliion.global.support.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Generation extends BaseEntity {

    private Long userId;
    private int generation;

    public Generation(Long userId, int generation) {
        this.userId = userId;
        this.generation = generation;
    }

    public static Generation of(Long userId, int generation) {
        return new Generation(userId, generation);
    }
}
