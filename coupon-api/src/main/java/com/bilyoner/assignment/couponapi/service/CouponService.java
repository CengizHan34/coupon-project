package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.entity.CouponEntity;
import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.exception.GenericException;
import com.bilyoner.assignment.couponapi.model.CouponCreateRequest;
import com.bilyoner.assignment.couponapi.model.CouponDTO;
import com.bilyoner.assignment.couponapi.model.CouponPlayRequest;
import com.bilyoner.assignment.couponapi.model.UpdateBalanceRequest;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.bilyoner.assignment.couponapi.model.enums.EventTypeEnum;
import com.bilyoner.assignment.couponapi.model.enums.TransactionType;
import com.bilyoner.assignment.couponapi.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final EventService eventService;
    private final BalanceService balanceService;


    public List<CouponDTO> getAllCouponsByCouponStatus(CouponStatusEnum couponStatus) {
        final List<CouponEntity> couponEntities = couponRepository.findByStatus(couponStatus);
        List<CouponDTO> couponDTOList = couponEntities.stream()
                .map(this::convertCouponEntityToDto).collect(Collectors.toList());

        return couponDTOList;
    }

    public CouponDTO createCoupon(CouponCreateRequest couponCreateRequest) {
        final List<EventEntity> mixedEventEntities = eventService.findByIds(couponCreateRequest.getEventIds());
        Map<EventTypeEnum, List<EventEntity>> eventMap = mixedEventEntities.stream().collect(groupingBy(EventEntity::getType));
        checkFootballAndTennisEvent(eventMap);

        EventEntity maxMbs = mixedEventEntities.stream().max(Comparator.comparing(EventEntity::getMbs)).orElseThrow(NoSuchElementException::new);

        if (maxMbs.getMbs() > mixedEventEntities.size()) {
            throw new GenericException(String.format("You must play at least %d matches ", maxMbs.getMbs()));
        }

        boolean checkEventDate = mixedEventEntities.stream()
                .anyMatch(eventEntity -> eventEntity.getEventDate().isBefore(LocalDateTime.now()));

        if (checkEventDate) {
            throw new GenericException(String.format("There are matches played!"));
        }

        EventEntity minDate = mixedEventEntities.stream().min(Comparator.comparing(EventEntity::getEventDate))
                .orElseThrow(NoSuchElementException::new);

        CouponEntity couponEntity = CouponEntity.builder()
                .cost(BigDecimal.valueOf(5))
                .status(CouponStatusEnum.CREATED)
                .playDate(minDate.getEventDate())
                .eventEntities(mixedEventEntities)
                .build();

        CouponEntity createdCouponEntity = couponRepository.save(couponEntity);

        CouponDTO couponDTO = CouponDTO.builder()
                .id(createdCouponEntity.getId())
                .cost(createdCouponEntity.getCost())
                .status(createdCouponEntity.getStatus())
                .eventIds(couponCreateRequest.getEventIds())
                .playDate(createdCouponEntity.getPlayDate())
                .build();

        return couponDTO;
    }

    private void checkFootballAndTennisEvent(Map<EventTypeEnum, List<EventEntity>> eventMap) {
        if (eventMap.containsKey(EventTypeEnum.FOOTBALL) && eventMap.containsKey(EventTypeEnum.TENNIS)) {
            throw new GenericException("Tennis and Football cannot be in the same coupon!");
        }
    }


    public List<CouponDTO> playCoupons(CouponPlayRequest couponPlayRequest) {
        final List<CouponEntity> couponEntities = couponPlayRequest.getCouponIds().stream()
                .map(id -> couponRepository.findById(id).get())
                .collect(Collectors.toList());


        List<CouponDTO> couponDTOList = couponEntities.stream().peek(couponEntity -> {

            if (!couponEntity.getStatus().equals(CouponStatusEnum.CANCELED)) {

                UpdateBalanceRequest updateBalanceRequest = UpdateBalanceRequest.builder()
                        .userId(couponPlayRequest.getUserId())
                        .amount(couponEntity.getCost())
                        .transactionId(UUID.randomUUID().toString())
                        .transactionType(TransactionType.BUY.name())
                        .build();


                balanceService.updateBalance(updateBalanceRequest);

                couponEntity.setUpdateDate(LocalDateTime.now());
                couponEntity.setStatus(CouponStatusEnum.PLAYED);
                couponEntity.setUserId(couponPlayRequest.getUserId());
                couponEntity.setTransactionId(updateBalanceRequest.getTransactionId());
                couponRepository.save(couponEntity);
            }
            else{
                couponEntity.setStatus(CouponStatusEnum.CANCELED);
            }

        }).map(this::convertCouponEntityToDto)
                .collect(Collectors.toList());


        return couponDTOList;
    }

    private CouponDTO convertCouponEntityToDto(CouponEntity couponEntity) {
        List<Long> eventList = couponEntity.getEventEntities().stream()
                .map(EventEntity::getId)
                .collect(Collectors.toList());

        CouponDTO couponDTO = CouponDTO.builder()
                .id(couponEntity.getId())
                .userId(couponEntity.getUserId())
                .eventIds(eventList)
                .status(couponEntity.getStatus())
                .playDate(couponEntity.getPlayDate())
                .build();

        return couponDTO;
    }

    public CouponDTO cancelCoupon(Long couponId) {
        final CouponEntity couponEntity = couponRepository.findById(couponId).get();
        couponEntity.setStatus(CouponStatusEnum.CANCELED);
        couponEntity.setUpdateDate(LocalDateTime.now());
        couponRepository.save(couponEntity);

        CouponDTO couponDTO = convertCouponEntityToDto(couponEntity);

        return couponDTO;
    }

    public List<CouponDTO> getPlayedCoupons(Long userId) {
        final List<CouponEntity> couponEntities = couponRepository.findByUserId(userId);
        List<CouponDTO> couponDTOList = couponEntities.stream().map(this::convertCouponEntityToDto)
                .collect(Collectors.toList());
        return couponDTOList;
    }
}
