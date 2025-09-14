package com.assessment.cts.service.impl;

import com.assessment.cts.entity.*;
import com.assessment.cts.enums.*;
import com.assessment.cts.model.PriceDTO;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.TradeRequestDTO;
import com.assessment.cts.model.TradeResponseDTO;
import com.assessment.cts.repository.ProductRepository;
import com.assessment.cts.repository.TradeRepository;
import com.assessment.cts.repository.UserRepository;
import com.assessment.cts.repository.WalletBalanceRepository;
import com.assessment.cts.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * In the future: Should handle invalid product type properly
 * */
@AllArgsConstructor
@Service
public class SpotTradeServiceImpl implements TradeService {

    private final PriceService priceService;
    private final ProductService productService;
    private final TradeRepository tradeRepository;
    private final UserService userService;
    private final WalletBalanceService walletBalanceService;
    private final WalletService walletService;
    private final CurrencyService currencyService;

    private HelperUtility helperUtility;

    /**
     * BUY: ETHUSDT or BTCUSDT
     * Convert USDT (Quote) to BaseCurrency
     * finalAmount (ETH or BTC) = Quote (USDT) / Rate (Ask Price)
     * */
    @Override
    @Transactional
    public ResponseDTO<TradeResponseDTO> buy(TradeRequestDTO tradeRequestDTO) {
        try {
            if (isValidTrade(tradeRequestDTO)) {
                // Ensure that the user exists
                User user = this.userService.getUserByUuid(tradeRequestDTO.getUuid());
                if (user == null) {
                    return this.helperUtility.transformToResponseDTO(null, ResponseStatus.ERROR, "No user in this system.");
                }
                // Ensure that the currency pair is tradable
                String currencyPair = getCurrencyPair(tradeRequestDTO);
                Product product = this.productService.getProductBySymbol(currencyPair);
                if (product == null) {
                    return this.helperUtility.transformToResponseDTO(null, ResponseStatus.INVALID, "The product is not tradable in this system.");
                }
                // Ensure that there are prices for the product
                PriceDTO latestPrice = this.priceService.getLatestPrice(currencyPair);
                if (latestPrice == null) {
                    return this.helperUtility.transformToResponseDTO(null, ResponseStatus.INVALID, "There is no price for this product.");
                }

                // Calculate the final amount from this BUY order
                BigDecimal notionalAmount = tradeRequestDTO.getAmount(); // This is the amount of QUOTE (USDT)
                BigDecimal rateAmount = latestPrice.getAskPrice();
                BigDecimal finalAmount = notionalAmount.divide(rateAmount, 8, RoundingMode.HALF_UP);

                // Ensure that user have the wallet for this product type otherwise create it
                // In theory, the ProductType would be valid because of TradeRequestDTO but any invalid ProductType exception handling is not done yet
                Wallet wallet = this.walletService.getOrCreateWallet(user, tradeRequestDTO.getProductType());
                // Ensure that user's wallet have the currencies and balances for such trade
                Currency baseCurrency = this.currencyService.getCurrencyByCode(tradeRequestDTO.getBaseCurrency());
                Currency quoteCurrency = this.currencyService.getCurrencyByCode(tradeRequestDTO.getQuoteCurrency());
                WalletBalance baseWalletBalance = this.walletBalanceService.getOrCreateWalletBalance(wallet, baseCurrency);
                WalletBalance quoteWalletBalance = this.walletBalanceService.getOrCreateWalletBalance(wallet, quoteCurrency);

                // Since it's BUY, so should deduct notionalAmount from quote and add finalAmount to base
                if (quoteWalletBalance.getAvailableBalance().compareTo(notionalAmount) < 0) {
                    return this.helperUtility.transformToResponseDTO(null, ResponseStatus.INVALID, "User's balance is less than notional amount.");
                }

                // Deduct notionalAmount from quoteWallet (USDT)
                quoteWalletBalance.setAvailableBalance(quoteWalletBalance.getAvailableBalance().subtract(notionalAmount));
                // Add finalAmount into baseWallet (ETH or BTC)
                baseWalletBalance.setAvailableBalance(baseWalletBalance.getAvailableBalance().add(finalAmount));
                // If something went wrong during updating of balances
                ResponseDTO<TradeResponseDTO> updateAllWalletBalancesResult = this.walletBalanceService.updateAllWalletBalances(List.of(baseWalletBalance, quoteWalletBalance));
                if (ResponseStatus.ERROR.equals(updateAllWalletBalancesResult.getResponseStatus())) {
                    return updateAllWalletBalancesResult;
                }

                // Add it into the trade
                Trade trade = transformToTrade(tradeRequestDTO, product, user);
                trade.setDirection(TradeDirection.BUY);
                trade.setRateAmount(rateAmount);
                trade.setFinalAmount(finalAmount);
                trade.setStatus(TradeStatus.COMPLETED);
                this.tradeRepository.save(trade);
                return this.helperUtility.transformToResponseDTO(transformToTradeResponseDTO(trade), ResponseStatus.SUCCESS, "Buy Trade Order was executed successfully.");
            } else {
                return this.helperUtility.transformToResponseDTO(null, ResponseStatus.INVALID, "Trade request is invalid. Please ensure that the fields are correct.");
            }
        } catch (Exception e) {
            return this.helperUtility.transformToResponseDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later");
        }
    }

    /**
     * SELL: ETHUSDT or BTCUSDT
     * Convert ETH (Base) to USDT (Quote)
     * finalAmount (USDT) = Base (ETH or BTC) x Rate (Bid Price)
     * */
    @Override
    @Transactional
    public ResponseDTO<TradeResponseDTO> sell(TradeRequestDTO tradeRequestDTO) {
        try {
            if (isValidTrade(tradeRequestDTO)) {
                // Ensure that the user exists
                User user = this.userService.getUserByUuid(tradeRequestDTO.getUuid());
                if (user == null) {
                    return this.helperUtility.transformToResponseDTO(null, ResponseStatus.ERROR, "No user in this system.");
                }
                // Ensure that the currency pair is tradable
                String currencyPair = getCurrencyPair(tradeRequestDTO);
                Product product = this.productService.getProductBySymbol(currencyPair);
                if (product == null) {
                    return this.helperUtility.transformToResponseDTO(null, ResponseStatus.INVALID, "The product is not tradable in this system.");
                }
                // Ensure that there are prices for the product
                PriceDTO latestPrice = this.priceService.getLatestPrice(currencyPair);
                if (latestPrice == null) {
                    return this.helperUtility.transformToResponseDTO(null, ResponseStatus.INVALID, "There is no price for this product.");
                }

                // Calculate the final amount from this SELL order
                BigDecimal notionalAmount = tradeRequestDTO.getAmount(); // This is the amount of BASE (ETH or BTC)
                BigDecimal rateAmount = latestPrice.getBidPrice();
                BigDecimal finalAmount = notionalAmount.multiply(rateAmount).setScale(8, RoundingMode.HALF_UP);

                // Ensure that user have the wallet for this product type otherwise create it
                // In theory, the ProductType would be valid because of TradeRequestDTO but any invalid ProductType exception handling is not done yet
                Wallet wallet = this.walletService.getOrCreateWallet(user, tradeRequestDTO.getProductType());
                // Ensure that user's wallet have the currencies and balances for such trade
                Currency baseCurrency = this.currencyService.getCurrencyByCode(tradeRequestDTO.getBaseCurrency());
                Currency quoteCurrency = this.currencyService.getCurrencyByCode(tradeRequestDTO.getQuoteCurrency());
                WalletBalance baseWalletBalance = this.walletBalanceService.getOrCreateWalletBalance(wallet, baseCurrency);
                WalletBalance quoteWalletBalance = this.walletBalanceService.getOrCreateWalletBalance(wallet, quoteCurrency);

                // Since it's SELL, so should deduct notionalAmount from base and add finalAmount to quote
                if (baseWalletBalance.getAvailableBalance().compareTo(notionalAmount) < 0) {
                    return this.helperUtility.transformToResponseDTO(null, ResponseStatus.INVALID, "User's balance is less than notional amount.");
                }

                // Deduct notionalAmount from baseWallet (ETH or BTC)
                baseWalletBalance.setAvailableBalance(baseWalletBalance.getAvailableBalance().subtract(notionalAmount));
                // Add finalAmount into quoteWallet (USDT)
                quoteWalletBalance.setAvailableBalance(quoteWalletBalance.getAvailableBalance().add(finalAmount));
                // If something went wrong during updating of balances
                ResponseDTO<TradeResponseDTO> updateAllWalletBalancesResult = this.walletBalanceService.updateAllWalletBalances(List.of(baseWalletBalance, quoteWalletBalance));
                if (ResponseStatus.ERROR.equals(updateAllWalletBalancesResult.getResponseStatus())) {
                    return updateAllWalletBalancesResult;
                }

                // Add it into the trade
                Trade trade = transformToTrade(tradeRequestDTO, product, user);
                trade.setDirection(TradeDirection.SELL);
                trade.setRateAmount(rateAmount);
                trade.setFinalAmount(finalAmount);
                trade.setStatus(TradeStatus.COMPLETED);
                this.tradeRepository.save(trade);
                return this.helperUtility.transformToResponseDTO(transformToTradeResponseDTO(trade), ResponseStatus.SUCCESS, "Sell Trade Order was executed successfully.");
            } else {
                return this.helperUtility.transformToResponseDTO(null, ResponseStatus.INVALID, "Trade request is invalid. Please ensure that the fields are correct.");
            }
        } catch (Exception e) {
            return this.helperUtility.transformToResponseDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later");
        }
    }

    private String getCurrencyPair(TradeRequestDTO tradeRequestDTO) {
        return tradeRequestDTO.getBaseCurrency().toUpperCase() + tradeRequestDTO.getQuoteCurrency().toUpperCase();
    }

    private Boolean isValidTrade(TradeRequestDTO tradeRequestDTO) {
        return tradeRequestDTO != null &&
                tradeRequestDTO.getAmount().compareTo(BigDecimal.ZERO) > 0 &&
                !tradeRequestDTO.getBaseCurrency().isEmpty() &&
                !tradeRequestDTO.getQuoteCurrency().isEmpty();
    }

    private Trade transformToTrade(TradeRequestDTO tradeRequestDTO, Product product, User user) {
        Trade trade = new Trade();
        trade.setUser(user);
        trade.setProduct(product);
        trade.setBaseCurrency(product.getBaseCurrency());
        trade.setQuoteCurrency(product.getQuoteCurrency());
        trade.setNotionalAmount(tradeRequestDTO.getAmount());
        return trade;
    }

    private TradeResponseDTO transformToTradeResponseDTO(Trade trade) {
        TradeResponseDTO tradeResponseDTO = new TradeResponseDTO();
        tradeResponseDTO.setSymbol(trade.getProduct().getSymbol());
        tradeResponseDTO.setNotionalAmount(trade.getNotionalAmount());
        tradeResponseDTO.setRateAmount(trade.getRateAmount());
        tradeResponseDTO.setFinalAmount(trade.getFinalAmount());
        tradeResponseDTO.setTradeDirection(trade.getDirection());
        tradeResponseDTO.setTradeDateTime(trade.getCreatedAt());
        return tradeResponseDTO;
    }
}
