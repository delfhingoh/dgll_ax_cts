package com.assessment.cts.initialiser;

import com.assessment.cts.entity.*;
import com.assessment.cts.model.initialise.ProductInit;
import com.assessment.cts.model.initialise.WalletBalanceInit;
import com.assessment.cts.model.initialise.WalletInit;
import com.assessment.cts.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DataInitialiser implements ApplicationRunner {
    @Value("classpath:user-file.json")
    private Resource userResource;
    @Value("classpath:currency-file.json")
    private Resource currencyResource;
    @Value("classpath:product-file.json")
    private Resource productResource;
    @Value("classpath:wallet-file.json")
    private Resource walletResource;
    @Value("classpath:wallet-balance-file.json")
    private Resource walletBalanceResource;

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final ProductRepository productRepository;
    private final WalletRepository walletRepository;
    private final WalletBalanceRepository walletBalanceRepository;
    private final ObjectMapper objectMapper;

    public DataInitialiser(
        UserRepository userRepository,
        CurrencyRepository currencyRepository,
        ProductRepository productRepository,
        WalletRepository walletRepository,
        WalletBalanceRepository walletBalanceRepository,
        ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
        this.productRepository = productRepository;
        this.walletRepository = walletRepository;
        this.walletBalanceRepository = walletBalanceRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        populateUserData(userResource);
        populateCurrencyData(currencyResource);
        populateProductData(productResource);
        populateWalletData(walletResource);
        populateWalletBalanceData(walletBalanceResource);
    }

    private void populateUserData(Resource resource) throws IOException {
        if (resource != null) {
            log.info("Loading users from {}", userResource);
            InputStream userInputStream = resource.getInputStream();
            List<User> users = objectMapper.readValue(userInputStream, new TypeReference<>() {});
            this.userRepository.saveAll(users);
            log.info("Users loaded");
        } else {
            log.info("Users resource not found. Please ensure that path is correct.");
        }
    }

    private void populateCurrencyData(Resource resource) throws IOException {
        if (resource != null) {
            log.info("Loading currencies from {}", currencyResource);
            InputStream currencyInputStream = resource.getInputStream();
            List<Currency> currencies = objectMapper.readValue(currencyInputStream, new TypeReference<>() {});
            this.currencyRepository.saveAll(currencies);
            log.info("Currencies loaded");
        } else  {
            log.info("Currency resource not found. Please ensure that path is correct.");
        }
    }

    private void populateProductData(Resource resource) throws IOException {
        if (resource != null) {
            log.info("Loading products from {}", productResource);
            InputStream productInputStream = resource.getInputStream();
            List<ProductInit> initProducts = objectMapper.readValue(productInputStream, new TypeReference<>() {});
            List<Product> products = initProducts.stream()
                    .map(this::mapToProductEntity)
                    .toList();
            this.productRepository.saveAll(products);
            log.info("Products loaded");
        } else {
            log.info("Product resource not found. Please ensure that path is correct.");
        }
    }

    private void populateWalletData(Resource resource) throws IOException {
        if (resource != null) {
            log.info("Loading wallets from {}", walletResource);
            InputStream walletInputStream = resource.getInputStream();
            List<WalletInit> initWallets = objectMapper.readValue(walletInputStream, new TypeReference<>() {});
            List<Wallet> wallets = initWallets.stream()
                    .map(this::mapToWalletInitEntity)
                    .toList();
            this.walletRepository.saveAll(wallets);
            log.info("Wallets loaded");
        } else {
            log.info("Wallet resource not found. Please ensure that path is correct.");
        }
    }

    private void populateWalletBalanceData(Resource resource) throws IOException {
        if (resource != null) {
            log.info("Loading wallet balances from {}", walletBalanceResource);
            InputStream walletBalanceInputStream = resource.getInputStream();
            List<WalletBalanceInit> initWallets = objectMapper.readValue(walletBalanceInputStream, new TypeReference<>() {});
            List<WalletBalance> balances = initWallets.stream()
                    .map(this::mapToWalletBalanceEntity)
                    .toList();
            this.walletBalanceRepository.saveAll(balances);
            log.info("Wallet balances loaded");
        } else {
            log.info("Wallet balance resource not found. Please ensure that path is correct.");
        }
    }

    private Product mapToProductEntity(ProductInit productInit) {
        Product product = new Product();
        product.setSymbol(productInit.getSymbol());
        product.setStatus(productInit.getStatus());
        Currency base = this.currencyRepository.findByCode(productInit.getBaseCurrencyCode());
        Currency quote = this.currencyRepository.findByCode(productInit.getQuoteCurrencyCode());
        product.setBaseCurrency(base);
        product.setQuoteCurrency(quote);
        return product;
    }

    private Wallet mapToWalletInitEntity(WalletInit walletInit) {
        Wallet wallet = new Wallet();
        User user = this.userRepository.findByEmail(walletInit.getUserEmail());
        wallet.setUser(user);
        wallet.setWalletType(walletInit.getWalletType());
        wallet.setStatus(walletInit.getStatus());
        return wallet;
    }

    private WalletBalance mapToWalletBalanceEntity(WalletBalanceInit walletBalanceInit) {
        WalletBalance walletBalance = new WalletBalance();
        User user = this.userRepository.findByEmail(walletBalanceInit.getUserEmail());
        Wallet wallet = this.walletRepository.findByUserAndWalletType(user, walletBalanceInit.getWalletType());
        Currency currency = this.currencyRepository.findByCode(walletBalanceInit.getCurrencyCode());
        walletBalance.setWallet(wallet);
        walletBalance.setCurrency(currency);
        walletBalance.setAvailableBalance(walletBalanceInit.getAvailableBalance());
        return walletBalance;
    }
}
