package com.dnd.Exercise.domain.activityRing.repository;

import com.dnd.Exercise.domain.activityRing.entity.ActivityRing;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRingRepository
        extends JpaRepository<ActivityRing, Long>, ActivityRingRepositoryCustom {

    List<ActivityRing> findAllByDateAndUserIdIn(LocalDate date, Collection<Long> id);

    Optional<ActivityRing> findByDateAndUserId(LocalDate date, long userId);
}
