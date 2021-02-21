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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CouponServiceTest {
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private EventService eventService;
    @Mock
    private BalanceService balanceService;

    private CouponService couponService;

    private EventEntity eventEntity;
    private CouponEntity couponEntity;
    private List<EventEntity> eventEntities = new ArrayList<>();



    @BeforeEach
    void setUp(){
        couponService = new CouponService(couponRepository,eventService,balanceService);
    }

    @BeforeEach
    void initialize(){
         eventEntity = EventEntity.builder().id(1l)
                 .eventDate(LocalDateTime.of(2022, 1, 8,12,00,00))
                 .name("Galatasaray - Fenerbahçe")
                 .mbs(3).type(EventTypeEnum.FOOTBALL).build();

         couponEntity = CouponEntity.builder().eventEntities(Arrays.asList(eventEntity)).cost(new BigDecimal(5))
                 .status(CouponStatusEnum.CREATED).userId(Long.valueOf(1l)).playDate(LocalDateTime.now()).build();

        eventEntities.add(eventEntity);
    }

    @Test
    public void getAllCouponsByCouponStatus_IfGetAllCouponsByCouponStatusIsCalled_couponDtoListMustReturn(){
        CouponStatusEnum couponStatusEnum = CouponStatusEnum.CREATED;

        List<CouponEntity> couponEntities = Arrays.asList(couponEntity);

        when(couponRepository.findByStatus(couponStatusEnum)).thenReturn(couponEntities);
        List<CouponDTO> couponDTOList =  couponService.getAllCouponsByCouponStatus(couponStatusEnum);

        Assertions.assertNotNull(couponDTOList);
        Assertions.assertTrue(couponDTOList.get(0) instanceof  CouponDTO);
        Assertions.assertTrue(couponDTOList.get(0).getEventIds().get(0) instanceof  Long);
    }

    @Test
    public void createCoupon_IfFootballAndTennisAreInSameCoup_mustThrowGenericException(){
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        couponCreateRequest.setEventIds(Arrays.asList(1l,2l,3l));
        EventEntity eventEntity1 = EventEntity.builder().id(2l).eventDate(LocalDateTime.now()).name("Beşiktaş - Fenerbahçe")
                .mbs(2).type(EventTypeEnum.FOOTBALL).build();
        EventEntity eventEntity2 = EventEntity.builder().id(3l).eventDate(LocalDateTime.now()).name("Martina Navratilova -  Serena Williams")
                .mbs(2).type(EventTypeEnum.TENNIS).build();

        eventEntities.add(eventEntity1);
        eventEntities.add(eventEntity2);

        when(eventService.findByIds(Arrays.asList(1l,2l,3l))).thenReturn(eventEntities);

        Assertions.assertThrows(GenericException.class, () -> {
            couponService.createCoupon(couponCreateRequest);
        });


    }

    @Test
    public void createCoupon_IfMbsConditionIsNotMet_mustThrowGenericException(){

        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        couponCreateRequest.setEventIds(Arrays.asList(1l,2l));
        EventEntity eventEntity1 = EventEntity.builder().id(2l).eventDate(LocalDateTime.now()).name("Beşiktaş - Fenerbahçe")
                .mbs(2).type(EventTypeEnum.FOOTBALL).build();

        eventEntities.add(eventEntity1);

        when(eventService.findByIds(Arrays.asList(1l,2l))).thenReturn(eventEntities);

        Assertions.assertThrows(GenericException.class, () -> {
            couponService.createCoupon(couponCreateRequest);
        });

    }

    @Test
    public void createCoupon_ifThereIsGamePlayed_mustThrowGenericException(){
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        couponCreateRequest.setEventIds(Arrays.asList(1l,2l,3l));
        EventEntity eventEntity1 = EventEntity.builder().id(2l).eventDate(LocalDateTime.now()).name("Beşiktaş - Fenerbahçe")
                .mbs(2).type(EventTypeEnum.FOOTBALL).eventDate(LocalDateTime.now()).build();
        EventEntity eventEntity2 = EventEntity.builder().id(3l)
                .eventDate(LocalDateTime.of(2020, 1, 8,12,00,00))
                .name("Beşiktaş - Fenerbahçe")
                .mbs(2).type(EventTypeEnum.FOOTBALL).build();

        eventEntities.add(eventEntity1);
        eventEntities.add(eventEntity2);
        couponEntity.setId(1l);

        when(eventService.findByIds(Arrays.asList(1l,2l,3l))).thenReturn(eventEntities);
        when(couponRepository.save(any(CouponEntity.class))).thenReturn(couponEntity);

        Assertions.assertThrows(GenericException.class, () -> {
            couponService.createCoupon(couponCreateRequest);
        });
    }

    @Test
    public void createCoupon_IfCreateCouponValidationsPass_shouldReturnCouponDTO(){
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        couponCreateRequest.setEventIds(Arrays.asList(1l,2l,3l));
        EventEntity eventEntity1 = EventEntity.builder().id(2l)
                .eventDate(LocalDateTime.of(2022, 1, 8,12,00,00)).name("Beşiktaş - Fenerbahçe")
                .mbs(2).type(EventTypeEnum.FOOTBALL).build();
        EventEntity eventEntity2 = EventEntity.builder().id(3l)
                .eventDate(LocalDateTime.of(2022, 1, 8,12,00,00))
                .name("Beşiktaş - Fenerbahçe")
                .mbs(2).type(EventTypeEnum.FOOTBALL).build();

        eventEntities.add(eventEntity1);
        eventEntities.add(eventEntity2);

        CouponEntity couponEntity2 = CouponEntity.builder().id(1l).eventEntities(Arrays.asList(eventEntity))
                .status(CouponStatusEnum.CREATED).build();

        when(eventService.findByIds(Arrays.asList(1l,2l,3l))).thenReturn(eventEntities);
        when(couponRepository.save(any(CouponEntity.class))).thenReturn(couponEntity2);



        CouponDTO couponDTO =  couponService.createCoupon(couponCreateRequest);

        Assertions.assertNotNull(couponDTO);
        Assertions.assertEquals(1l,couponDTO.getId());
        Assertions.assertEquals(CouponStatusEnum.CREATED,couponDTO.getStatus());
    }

    @Test
    public void playCoupons_ifCouponIsNotCanceled_shouldReturnCouponDTOs(){
        CouponPlayRequest couponPlayRequest = new CouponPlayRequest();
        couponPlayRequest.setUserId(1l);
        couponPlayRequest.setCouponIds(Arrays.asList(1l));
        Optional<CouponEntity> couponEntity1 = Optional.of(couponEntity);

        UpdateBalanceRequest updateBalanceRequest = UpdateBalanceRequest.builder()
                .userId(couponPlayRequest.getUserId())
                .amount(couponEntity.getCost())
                .transactionId(UUID.randomUUID().toString())
                .transactionType(TransactionType.BUY.name())
                .build();

        couponEntity.setId(1l);

        when(couponRepository.findById(any())).thenReturn(couponEntity1);
        doNothing().when(balanceService).updateBalance(updateBalanceRequest);
        when(couponRepository.save(any(CouponEntity.class))).thenReturn(couponEntity);

        List<CouponDTO> couponDTOList = couponService.playCoupons(couponPlayRequest);

        Assertions.assertNotNull(couponDTOList);


    }

    @Test
    public void playCoupons_ifCouponIsCanceled_shouldReturnCancellationInformation(){
        CouponPlayRequest couponPlayRequest = new CouponPlayRequest();
        couponPlayRequest.setUserId(1l);
        couponPlayRequest.setCouponIds(Arrays.asList(1l));

        CouponEntity couponEntityCancelled = CouponEntity.builder().eventEntities(Arrays.asList(eventEntity)).cost(new BigDecimal(5))
                .status(CouponStatusEnum.CANCELED).userId(Long.valueOf(1l)).build();

        Optional<CouponEntity> couponEntityOptional = Optional.of(couponEntityCancelled);

        UpdateBalanceRequest updateBalanceRequest = UpdateBalanceRequest.builder()
                .userId(couponPlayRequest.getUserId())
                .amount(couponEntity.getCost())
                .transactionId(UUID.randomUUID().toString())
                .transactionType(TransactionType.BUY.name())
                .build();

        couponEntity.setId(1l);

        when(couponRepository.findById(any())).thenReturn(couponEntityOptional);
        doNothing().when(balanceService).updateBalance(updateBalanceRequest);
        when(couponRepository.save(any(CouponEntity.class))).thenReturn(couponEntity);

        List<CouponDTO> couponDTOList = couponService.playCoupons(couponPlayRequest);

        Assertions.assertNotNull(couponDTOList);
        Assertions.assertEquals(couponDTOList.get(0).getStatus(),CouponStatusEnum.CANCELED);

    }

    @Test
    public void cancelCoupon_IfCancelCouponIsCalled_mustCancelTheActiveCoupon(){
        Optional<CouponEntity> couponEntityOptional = Optional.of(couponEntity);
        when(couponRepository.findById(1l)).thenReturn(couponEntityOptional);
        when(couponRepository.save(any(CouponEntity.class))).thenReturn(couponEntity);
        CouponDTO couponDTO = couponService.cancelCoupon(1l);
        Assertions.assertNotNull(couponDTO);
        Assertions.assertEquals(couponDTO.getStatus(),CouponStatusEnum.CANCELED);
    }

    @Test
    public void getPlayedCoupons_IfGetPlayedCouponsIsCalled_shouldReturnCouponsTheCustomerHasPlayed(){
        List<CouponEntity> couponEntities = Arrays.asList(couponEntity);
        when(couponRepository.findByUserId(1l)).thenReturn(couponEntities);
        List<CouponDTO> couponDTOList = couponService.getPlayedCoupons(1l);
        Assertions.assertNotNull(couponDTOList);
    }
}
