package com.assessment.cts.service.impl;

import com.assessment.cts.entity.*;
import com.assessment.cts.enums.ResponseStatus;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.ResponseListDTO;
import com.assessment.cts.model.TradeResponseDTO;
import com.assessment.cts.model.WalletBalanceResponseDTO;
import com.assessment.cts.repository.*;
import com.assessment.cts.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WalletBalanceRepository walletBalanceRepository;
    private final TradeRepository tradeRepository;

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
    public ResponseDTO<WalletBalanceResponseDTO> getUserWallet(String uuid, String currency) {
        try {
            Optional<User> userOptional = this.userRepository.findByUuid(uuid);
            if (userOptional.isEmpty()) {
                ResponseDTO<WalletBalanceResponseDTO> responseDTO = new ResponseDTO<>();
                responseDTO.setResponseStatus(ResponseStatus.INVALID);
                responseDTO.setMessage("User not found. Not able to retrieve wallet.");
                return responseDTO;
            } else {
                Optional<WalletBalance> walletBalanceOptional = this.walletBalanceRepository.findByWalletUserUuidAndCurrencyCode(uuid, currency);
                if (walletBalanceOptional.isEmpty()) {
                    ResponseDTO<WalletBalanceResponseDTO> responseDTO = new ResponseDTO<>();
                    responseDTO.setResponseStatus(ResponseStatus.INVALID);
                    responseDTO.setMessage("Wallet of that currency is not found.");
                    return responseDTO;
                } else {
                    ResponseDTO<WalletBalanceResponseDTO> responseDTO = new ResponseDTO<>();
                    WalletBalanceResponseDTO walletBalance = mapToBalanceResponseDTO(walletBalanceOptional.get());
                    responseDTO.setResponse(walletBalance);
                    responseDTO.setResponseStatus(ResponseStatus.SUCCESS);
                    return responseDTO;
                }
            }
        } catch (Exception e) {
            ResponseDTO<WalletBalanceResponseDTO> responseDTO = new ResponseDTO<>();
            responseDTO.setResponseStatus(ResponseStatus.ERROR);
            responseDTO.setMessage("Something went wrong. Please try again later.");
            return responseDTO;
        }
    }

    @Override
    public ResponseListDTO<WalletBalanceResponseDTO> getUserWallets(String uuid) {
        try {
            Optional<User> userOptional = this.userRepository.findByUuid(uuid);
            if (userOptional.isEmpty()) {
                ResponseListDTO<WalletBalanceResponseDTO> responseDTO = new ResponseListDTO<>();
                responseDTO.setResponseStatus(ResponseStatus.INVALID);
                responseDTO.setMessage("User not found. Not able to retrieve wallets.");
                return responseDTO;
            } else  {
                List<WalletBalance> walletBalances = this.walletBalanceRepository.findByWalletUserUuid(uuid);
                if (walletBalances.isEmpty()) {
                    ResponseListDTO<WalletBalanceResponseDTO> responseDTO = new ResponseListDTO<>();
                    responseDTO.setResponseStatus(ResponseStatus.INVALID);
                    responseDTO.setMessage("User have no wallets.");
                    return responseDTO;
                } else {
                    ResponseListDTO<WalletBalanceResponseDTO> responseDTO = new ResponseListDTO<>();
                    List<WalletBalanceResponseDTO> userAllWalletBalance = walletBalances.stream()
                            .map(this::mapToBalanceResponseDTO)
                            .toList();
                    responseDTO.setList(userAllWalletBalance);
                    responseDTO.setResponseStatus(ResponseStatus.SUCCESS);
                    return responseDTO;
                }
            }
        } catch (Exception e) {
            ResponseListDTO<WalletBalanceResponseDTO> responseDTO = new ResponseListDTO<>();
            responseDTO.setResponseStatus(ResponseStatus.ERROR);
            responseDTO.setMessage("Something went wrong. Please try again later.");
            return responseDTO;
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
            Optional<User> userOptional = this.userRepository.findByUuid(uuid);
            if (userOptional.isEmpty()) {
                ResponseListDTO<TradeResponseDTO> responseListDTO = new ResponseListDTO<>();
                responseListDTO.setResponseStatus(ResponseStatus.INVALID);
                responseListDTO.setMessage("User is empty. Not able to retrieve uuid");
                return responseListDTO;
            } else {
                List<Trade> trades = this.tradeRepository.findByUser(userOptional.get());
                if (trades.isEmpty()) {
                    ResponseListDTO<TradeResponseDTO> responseListDTO = new ResponseListDTO<>();
                    responseListDTO.setResponseStatus(ResponseStatus.INVALID);
                    responseListDTO.setMessage("User have not made any trades.");
                    return responseListDTO;
                } else {
                    ResponseListDTO<TradeResponseDTO> responseListDTO = new ResponseListDTO<>();
                    List<TradeResponseDTO> tradesResponseDTO = trades.stream()
                            .map(this::mapToTradeResponseDTO)
                            .toList();
                    responseListDTO.setList(tradesResponseDTO);
                    responseListDTO.setResponseStatus(ResponseStatus.SUCCESS);
                    return responseListDTO;
                }
            }
        } catch (Exception e) {
            ResponseListDTO<TradeResponseDTO> responseListDTO = new ResponseListDTO<>();
            responseListDTO.setResponseStatus(ResponseStatus.ERROR);
            responseListDTO.setMessage("Something went wrong. Please try again later");
            return responseListDTO;
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
