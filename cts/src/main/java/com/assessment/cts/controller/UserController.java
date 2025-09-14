package com.assessment.cts.controller;

import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.ResponseListDTO;
import com.assessment.cts.model.TradeResponseDTO;
import com.assessment.cts.model.WalletBalanceResponseDTO;
import com.assessment.cts.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    /**
     * I would imagine that the uuid is something frontend would have upon user login.
     * In this case, this is a very super temporary method to get UUID for the only user initalised at startup.
     * Then use the UUID to access the wallet balance and transactions
     * */
    @GetMapping("/uuid")
    String getJohnDoeUserUUID() {
        return this.userService.getJohnDoeUserUUID();
    }

    @GetMapping("/{uuid}/wallet/{currency}")
    ResponseDTO<WalletBalanceResponseDTO> getUserWalletBalance(@PathVariable String uuid, @PathVariable String currency) {
        return this.userService.getUserWallet(uuid, currency);
    }

    @GetMapping("/{uuid}/wallets")
    ResponseListDTO<WalletBalanceResponseDTO> getWalletBalance(@PathVariable String uuid){
        return this.userService.getUserWallets(uuid);
    }

    @GetMapping("/{uuid}/trades")
    ResponseListDTO<TradeResponseDTO> getTradeHistory(@PathVariable String uuid){
        return this.userService.getTradeHistory(uuid);
    }
}
