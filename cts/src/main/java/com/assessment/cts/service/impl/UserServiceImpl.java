package com.assessment.cts.service.impl;

import com.assessment.cts.entity.*;
import com.assessment.cts.enums.ResponseStatus;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.ResponseListDTO;
import com.assessment.cts.model.TradeResponseDTO;
import com.assessment.cts.model.WalletBalanceResponseDTO;
import com.assessment.cts.repository.*;
import com.assessment.cts.service.HelperUtility;
import com.assessment.cts.service.UserService;
import com.assessment.cts.service.WalletBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final WalletBalanceService walletBalanceService;
    private final TradeRepository tradeRepository; // Next time, will name the other TradeService differently
    private final UserRepository userRepository;
    private final HelperUtility helper;

    @Override
    public String getJohnDoeUserUUID() {
        Optional<User> userOptional = Optional.ofNullable(this.userRepository.findByEmail("johndoe@cts.com"));
        if (userOptional.isEmpty()) {
            return "User not found. Not able to retrieve uuid";
        } else {
            return userOptional.get().getUuid();
        }
    }

    @Override
    public ResponseDTO<User> getUserByUuid(String uuid) {
        try {
            Optional<User> userOptional = this.userRepository.findByUuid(uuid);
            if (userOptional.isEmpty()) {
                return this.helper.transformToResponseDTO(null, ResponseStatus.INVALID, "No user in this system.");
            }
            return this.helper.transformToResponseDTO(userOptional.get(), ResponseStatus.SUCCESS, "Retrieved user successfully by uuid");
        } catch (Exception e) {
            return this.helper.transformToResponseDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later.");
        }
    }

    @Override
    public ResponseDTO<WalletBalanceResponseDTO> getUserWallet(String uuid, String currency) {
        try {
            ResponseDTO<User> user = getUserByUuid(uuid);
            if (user.getResponse() == null) {
                return this.helper.transformToResponseDTO(null, user.getResponseStatus(), user.getMessage());
            }
            ResponseDTO<WalletBalance> walletBalance = this.walletBalanceService.findByWalletUserUuidAndCurrencyCode(uuid, currency);
            if (walletBalance.getResponse() == null) {
                return this.helper.transformToResponseDTO(null, walletBalance.getResponseStatus(), walletBalance.getMessage());
            } else {
                return this.helper.transformToResponseDTO(mapToBalanceResponseDTO(walletBalance.getResponse()), walletBalance.getResponseStatus(), walletBalance.getMessage());
            }
        } catch (Exception e) {
            return this.helper.transformToResponseDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later.");
        }
    }

    @Override
    public ResponseListDTO<WalletBalanceResponseDTO> getUserWallets(String uuid) {
        try {
            ResponseDTO<User> user = getUserByUuid(uuid);
            if (user.getResponse() == null) {
                return this.helper.transformToResponseListDTO(null, user.getResponseStatus(), user.getMessage());
            }

            ResponseListDTO<WalletBalance> walletBalances = this.walletBalanceService.findByWalletUserUuid(uuid);
            if (walletBalances.getList().isEmpty()) {
                return this.helper.transformToResponseListDTO(null, walletBalances.getResponseStatus(), walletBalances.getMessage());
            }
            List<WalletBalanceResponseDTO> userAllWalletBalance = walletBalances.getList().stream()
                    .map(this::mapToBalanceResponseDTO)
                    .toList();
            return this.helper.transformToResponseListDTO(userAllWalletBalance, ResponseStatus.SUCCESS, "Retrieved wallets successfully by uuid.");
        } catch (Exception e) {
            return this.helper.transformToResponseListDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later.");
        }
    }

    private WalletBalanceResponseDTO mapToBalanceResponseDTO(WalletBalance walletBalance) {
        WalletBalanceResponseDTO walletBalanceResponseDTO = new WalletBalanceResponseDTO();
        walletBalanceResponseDTO.setOwner(walletBalance.getWallet().getUser().getEmail());
        walletBalanceResponseDTO.setWalletType(walletBalance.getWallet().getWalletType());
        walletBalanceResponseDTO.setCurrency(walletBalance.getCurrency().getCode());
        walletBalanceResponseDTO.setAvailableBalance(walletBalance.getAvailableBalance());
        walletBalanceResponseDTO.setStatus(walletBalance.getWallet().getStatus());
        return walletBalanceResponseDTO;
    }

    @Override
    public ResponseListDTO<TradeResponseDTO> getTradeHistory(String uuid) {
        try {
            ResponseDTO<User> user = getUserByUuid(uuid);
            if (user.getResponse() == null) {
                return this.helper.transformToResponseListDTO(null, user.getResponseStatus(), user.getMessage());
            }

            List<Trade> trades = this.tradeRepository.findByUserOrderByCreatedAt(user.getResponse());
            if (trades.isEmpty()) {
                return this.helper.transformToResponseListDTO(null, ResponseStatus.INVALID, "User have not made any trades.");
            } else {
                List<TradeResponseDTO> tradesResponseDTO = trades.stream()
                        .map(this::mapToTradeResponseDTO)
                        .toList();
                return this.helper.transformToResponseListDTO(tradesResponseDTO, ResponseStatus.SUCCESS, "Retrieved trades by user.");
            }
        } catch (Exception e) {
            return this.helper.transformToResponseListDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later.");
        }
    }

    private TradeResponseDTO mapToTradeResponseDTO(Trade trade) {
        TradeResponseDTO tradeResponseDTO = new TradeResponseDTO();
        tradeResponseDTO.setSymbol(trade.getProduct().getSymbol());
        tradeResponseDTO.setNotionalAmount(trade.getNotionalAmount());
        tradeResponseDTO.setRateAmount(trade.getRateAmount());
        tradeResponseDTO.setFinalAmount(trade.getFinalAmount());
        tradeResponseDTO.setTradeStatus(trade.getStatus());
        tradeResponseDTO.setTradeDirection(trade.getDirection());
        tradeResponseDTO.setTradeDateTime(trade.getCreatedAt());
        return tradeResponseDTO;
    }
}
