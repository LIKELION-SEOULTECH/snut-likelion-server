package com.snut_likelion.domain.recruitment.infra;

import com.snut_likelion.domain.recruitment.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
public interface QuestionRepository extends JpaRepository<Question, Long> {
=======
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByRecruitmentId(Long recId);
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
}
