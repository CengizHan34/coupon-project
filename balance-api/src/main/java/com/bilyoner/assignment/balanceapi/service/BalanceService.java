package com.bilyoner.assignment.balanceapi.service;

import com.bilyoner.assignment.balanceapi.model.UpdateBalanceRequest;
import com.bilyoner.assignment.balanceapi.persistence.entity.UserBalanceEntity;
import com.bilyoner.assignment.balanceapi.persistence.entity.UserBalanceHistoryEntity;
import com.bilyoner.assignment.balanceapi.persistence.repository.UserBalanceHistoryRepository;
import com.bilyoner.assignment.balanceapi.persistence.repository.UserBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final UserBalanceRepository userBalanceRepository;
    private final UserBalanceHistoryRepository userBalanceHistoryRepository;

    @Transactional
    public void updateBalance(UpdateBalanceRequest updateBalanceRequest) {
        final Optional<UserBalanceEntity> userBalanceOptional = userBalanceRepository.findById(updateBalanceRequest.getUserId());
        UserBalanceEntity userBalanceEntity = null;

        if (userBalanceOptional.isPresent()) {
            userBalanceEntity = userBalanceOptional.get();


            if (userBalanceEntity.getAmount().compareTo(updateBalanceRequest.getAmount()) == -1) {
                throw new RuntimeException("Bakiye yetersiz");
            }

            userBalanceEntity.setAmount(userBalanceEntity.getAmount().subtract(updateBalanceRequest.getAmount()));
                

            userBalanceRepository.save(userBalanceEntity);
            UserBalanceHistoryEntity userBalanceHistoryEntity = UserBalanceHistoryEntity.builder()
                    .userId(userBalanceEntity.getUserId())
                    .amount(updateBalanceRequest.getAmount())
                    .transactionId(updateBalanceRequest.getTransactionId())
                    .transactionType(updateBalanceRequest.getTransactionType())
                    .build();
            userBalanceHistoryRepository.save(userBalanceHistoryEntity);
        }
    }
}
