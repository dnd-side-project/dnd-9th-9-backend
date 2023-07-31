package com.dnd.Exercise.global.common;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity{

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성 날짜

    @LastModifiedDate
    private LocalDateTime lastModifiedAt; // 수정 날짜
}
