package com.dnd.Exercise.domain.teamworkRate.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.teamworkRate.entity.TeamworkRate;
import com.dnd.Exercise.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamworkRateRepository extends JpaRepository<TeamworkRate, Long> {

    Boolean existsByFieldAndSubmitUser(Field field, User submitUser);

    Boolean existsByField(Field field);

    @Query("select avg(tr.rate) " +
            "from TeamworkRate tr " +
            "where tr.field.id = :fieldId")
    Double getAvgRateOfField(Long fieldId);

    @Query("select tr.rate " +
            "from TeamworkRate tr " +
            "where tr.field.id = :fieldId")
    int getRateOfField(Long fieldId);
}
