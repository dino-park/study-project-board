package com.greyson.projectboard.domain;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditingFields {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;    // 생성일시

    @CreatedBy
    @Column(name = "createdBy", nullable = false, updatable = false, length = 100)
    protected String createdBy;           // 생성자

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(name = "modifiedAt", nullable = false)
    protected LocalDateTime modifiedAt;   // 수정일시

    @LastModifiedBy
    @Column(name = "modifiedBy", nullable = false, length = 100)
    protected String modifiedBy;  // 수정자
}
