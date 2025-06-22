package com.snut_likelion.domain.recruitment.infra;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snut_likelion.domain.recruitment.dto.response.ApplicationResponse;
import com.snut_likelion.domain.user.entity.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.snut_likelion.domain.recruitment.entity.QApplication.application;

@Repository
@RequiredArgsConstructor
public class ApplicationQueryRepository {

    private final int PAGE_SIZE = 10; // 페이지당 항목 수

    private final JPAQueryFactory queryFactory;

    public List<ApplicationResponse> getApplicationsByRecruitmentId(Long recId, Part part, int page) {
        return queryFactory
                .select(Projections.constructor(ApplicationResponse.class,
                                application.id,
                                application.user.username,
                                application.part,
                                application.departmentType,
                                application.status,
                                application.submittedAt
                        )
                )
                .from(application)
                .where(
                        application.recruitment.id.eq(recId),
                        isPartMatch(part)
                )
                .offset((long) page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .fetch();
    }

    public BooleanExpression isPartMatch(Part part) {
        return part != null ? application.part.eq(part) : null;
    }

}
