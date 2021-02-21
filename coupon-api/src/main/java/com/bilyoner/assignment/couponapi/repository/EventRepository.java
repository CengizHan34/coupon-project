package com.bilyoner.assignment.couponapi.repository;

import com.bilyoner.assignment.couponapi.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    //List<EventEntity> findByIdInOrderByMbsDesc(List<Long> ids);
    List<EventEntity> findByIdIn(List<Long> ids);
}
