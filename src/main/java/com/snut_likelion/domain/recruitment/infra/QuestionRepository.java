package com.snut_likelion.domain.recruitment.infra;

import com.snut_likelion.domain.recruitment.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByRecruitmentId(Long recId);
}
