package com.snut_likelion.domain.project.infra;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snut_likelion.domain.project.entity.Project;
import com.snut_likelion.domain.project.entity.ProjectCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.snut_likelion.domain.project.entity.QProject.project;

@Repository
@RequiredArgsConstructor
public class ProjectQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Project> findAllByGenerationAndCategory(Integer generation, ProjectCategory category) {
        return queryFactory
                .selectFrom(project)
                .where(isCorrectGeneration(generation), isCorrectCategory(category))
                .fetch();
    }

    public BooleanExpression isCorrectGeneration(Integer generation) {
        return generation == null ? null : project.generation.eq(generation);
    }

    public BooleanExpression isCorrectCategory(ProjectCategory category) {
        return category == null ? null : project.category.eq(category);
    }
}
