package com.assessment.cts.service;

import com.assessment.cts.entity.User;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.ResponseListDTO;
import com.assessment.cts.model.TradeResponseDTO;
import com.assessment.cts.model.WalletBalanceResponseDTO;

public interface UserService {
    // getJohnDoeUserUUID is a temporary method to retrieve uuid
    String getJohnDoeUserUUID();

    ResponseDTO<WalletBalanceResponseDTO> getUserWallet(String uuid, String currency);
    ResponseListDTO<WalletBalanceResponseDTO> getUserWallets(String uuid);
    ResponseListDTO<TradeResponseDTO> getTradeHistory(String uuid);

    ResponseDTO<User> getUserByUuid(String uuid);
}
