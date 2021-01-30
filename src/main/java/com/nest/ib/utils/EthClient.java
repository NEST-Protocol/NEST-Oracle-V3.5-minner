package com.nest.ib.utils;

import com.nest.ib.config.NestProperties;
import com.nest.ib.constant.Constant;
import com.nest.ib.constant.GasLimit;
import com.nest.ib.contract.*;
import com.nest.ib.model.Wallet;
import com.nest.ib.helper.Web3jHelper;
import com.nest.ib.state.Erc20State;
import com.nest.ib.state.GasPriceState;
import com.nest.ib.state.MinnerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author wll
 * @date 2020/7/16 13:25
 */
@Component
public class EthClient {
    private static final Logger log = LoggerFactory.getLogger(EthClient.class);

    @Autowired
    private Erc20State erc20State;
    @Autowired
    private MinnerState minnerState;
    @Autowired
    private GasPriceState gasPriceState;
    @Autowired
    private NestProperties nestProperties;

    /**
     * Check Transaction Status
     *
     * @param hash
     * @return
     */
    public EthGetTransactionReceipt ethGetTransactionReceipt(String hash) {
        EthGetTransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = Web3jHelper.getWeb3j().ethGetTransactionReceipt(hash).send();
        } catch (IOException e) {
            log.error("ethGetTransactionReceipt 异常：{}", e.getMessage());
        }
        return transactionReceipt;
    }

    /**
     * Check if the NToken issue has reached 5 million, and then start POST2 quotation
     *
     * @return
     */
    public boolean needOpenPost2() {
        BigInteger nTokentotalSupply = totalSupply(erc20State.nToken.getAddress());
        if (nTokentotalSupply == null) {
            throw new NullPointerException("NToken circulation acquisition failed. Post2 cannot be enabled");
        }
        if (nTokentotalSupply.compareTo(nestProperties.getnTokenMaxTotalSupply()) > 0) {
            log.warn("{} circulation is greater than 5 million. Post2 needs to be enabled", erc20State.nToken.getSymbol());
            return true;
        }
        return false;
    }

    /**
     * Check to see if one-time authorization has taken place, and if not, one-time authorization has taken place
     */
    public void approveToNestMinningContract(Wallet wallet) throws Exception {
        log.info("Authorization checking");
        BigInteger nonce = ethGetTransactionCount(wallet.getCredentials().getAddress());
        if (nonce == null) {
            log.error("Failed to obtain nonce：{}", nonce);
            throw new Exception("Failed to obtain nonce");
        }
        // Authorization of the token
        String tokenApproveHash = erc20Appprove(wallet, erc20State.token, nonce);
        nonce = StringUtils.isEmpty(tokenApproveHash) ? nonce : nonce.add(BigInteger.ONE);
        // Ntoken authorization
        String nTokenApproveHash = null;
        if (minnerState.isMustPost2()) {
            nTokenApproveHash = erc20Appprove(wallet, erc20State.nToken, nonce);
        }

        // If not quoted by USDT, NEST also needs to be authorized
        if (!erc20State.token.getSymbol().equalsIgnoreCase("USDT")) {
            nonce = StringUtils.isEmpty(nTokenApproveHash) ? nonce : nonce.add(BigInteger.ONE);
            Erc20State.Item nest = new Erc20State.Item();
            nest.setAddress(nestProperties.getNestTokenAddress());
            nest.setSymbol("NEST");
            String nestApproveHash = erc20Appprove(wallet, nest, nonce);
        }
    }

    public String erc20Appprove(Wallet wallet, Erc20State.Item token, BigInteger nonce) throws ExecutionException, InterruptedException {
        String transactionHash = null;
        BigInteger gasPrice = ethGasPrice(gasPriceState.approveType);
        if (gasPrice == null) throw new NullPointerException("Authorization check Failed to get GasPrice");

        BigInteger approveValue = allowance(wallet, token.getAddress());
        if (approveValue == null) {
            log.error("Authorization to obtain ApproveValue failed：{}", approveValue);
        }

        if (approveValue.compareTo(new BigInteger("100000000000000")) <= 0) {

            List<Type> typeList = Arrays.<Type>asList(
                    new Address(nestProperties.getNestPoolContractAddress()),
                    new Uint256(new BigInteger("999999999999999999999999999999999999999999"))
            );
            Function function = new Function("approve", typeList, Collections.<TypeReference<?>>emptyList());
            String encode = FunctionEncoder.encode(function);
            BigInteger payableEth = BigInteger.ZERO;

            RawTransaction tokenRawTransaction = RawTransaction.createTransaction(
                    nonce,
                    gasPrice,
                    GasLimit.APPROVE_GAS_LIMIT,
                    token.getAddress(),
                    payableEth,
                    encode);

            EthSendTransaction ethSendTransaction = ethSendRawTransaction(wallet.getCredentials(), tokenRawTransaction);
            if (ethSendTransaction.hasError()) {
                Response.Error error = ethSendTransaction.getError();
                log.error("Authorization transaction return failed：[msg = {}],[data = {}],[code = {}],[result = {}],[RawResponse = {}],",
                        error.getMessage(),
                        error.getData(),
                        error.getCode(),
                        ethSendTransaction.getResult(),
                        ethSendTransaction.getRawResponse());
            } else {
                transactionHash = ethSendTransaction.getTransactionHash();
                log.info("{} : {} One-time authorization hash： {} ", wallet.getCredentials().getAddress(), token.getSymbol(), transactionHash);
            }
        }
        return transactionHash;
    }

    public boolean checkTxStatus(String txHash, BigInteger nonce, String minnerAddress) {

        if (StringUtils.isEmpty(txHash)) {
            log.error("Transaction hash is empty, stop detecting transaction status!");
            return false;
        }

        if (!checkNonce(nonce, minnerAddress)) {
            log.error(String.format("Current transaction exception, hash：%s", txHash));
            return false;
        }

        log.info(String.format("Check the transaction status hash：%s", txHash));
        Optional<TransactionReceipt> transactionReceipt = ethGetTransactionReceipt(txHash).getTransactionReceipt();
        if (transactionReceipt == null) return false;
        if (!transactionReceipt.isPresent()) {
            log.error(String.format("The transaction has been overwritten, %s", txHash));
            return false;
        }

        int status = Integer.parseInt(transactionReceipt.get().getStatus().substring(2));
        return status == 1;
    }

    private boolean checkNonce(BigInteger nonce, String address) {
        BigInteger transactionCount = ethGetTransactionCount(address);
        if (nonce.compareTo(transactionCount) < 0) {
            log.info("Trading nonce has changed");
            return true;
        }

        log.info("The transaction is in progress. Repeat the test after 3 seconds !");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return checkNonce(nonce, address);
    }

    /**
     * Query token totalSupply
     *
     * @param erc20Address
     * @return
     */
    public BigInteger totalSupply(String erc20Address) {
        BigInteger totalSupply = null;
        try {
            totalSupply = ContractBuilder.erc20Readonly(erc20Address, Web3jHelper.getWeb3j()).totalSupply().send();
        } catch (Exception e) {
            log.error("Total number of substitutions on the chain failed:{}", e.getMessage());
        }

        return totalSupply;
    }

    /**
     * Get the latest quote block number
     *
     * @return
     */
    public BigInteger checkLatestMining() {
        BigInteger lastNum = null;
        try {
            // USDT quotation
            if (erc20State.token.getSymbol().equalsIgnoreCase("USDT")) {
                lastNum = ContractBuilder.nestMiningContract(Web3jHelper.getWeb3j()).latestMinedHeight().sendAsync().get();
            } else {
                lastNum = ContractBuilder.nTokenContract(erc20State.nToken.getAddress(), Web3jHelper.getWeb3j()).checkBlockInfo().sendAsync().get().component2();
            }
        } catch (Exception e) {
            log.error("checkLatestMining failure:{}", e.getMessage());
        }
        return lastNum;
    }

    public BigInteger ethGasPrice(GasPriceState.Type type) {
        BigInteger gasPrice = null;

        try {
            gasPrice = Web3jHelper.getWeb3j().ethGasPrice().send().getGasPrice();
        } catch (IOException e) {
            log.error("Failed to get GasPrice：{}", e.getMessage());
        }
        if (gasPrice == null) return gasPrice;
        gasPrice = MathUtils.toDecimal(gasPrice).multiply(type.getGasPriceMul()).toBigInteger();

        return gasPrice;
    }

    /**
     * View the authorized amount
     *
     * @param wallet
     * @return
     */
    public BigInteger allowance(Wallet wallet, String erc20TokenAddress) {
        BigInteger approveValue = null;
        try {
            approveValue = ContractBuilder.erc20Readonly(erc20TokenAddress, Web3jHelper.getWeb3j()).allowance(wallet.getCredentials().getAddress(), nestProperties.getNestPoolContractAddress()).sendAsync().get();
        } catch (InterruptedException e) {
            log.error("The query for authorization amount failed：{}", e.getMessage());
        } catch (ExecutionException e) {
            log.error("The query for authorization amount failed：{}", e.getMessage());
        }
        return approveValue;
    }

    /**
     * For the nonce value
     *
     * @param address
     * @return
     */
    public BigInteger ethGetTransactionCount(String address) {
        BigInteger transactionCount = null;

        try {
            transactionCount = Web3jHelper.getWeb3j().ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        } catch (Exception e) {
            log.error("Failed to get nonce:{}", e.getMessage());
        }
        return transactionCount;
    }

    /**
     * Get the latest block number for Ethereum
     *
     * @return
     */
    public BigInteger ethBlockNumber() {
        BigInteger latestBlockNumber = null;

        try {
            latestBlockNumber = Web3jHelper.getWeb3j().ethBlockNumber().send().getBlockNumber();
        } catch (IOException e) {
            log.error("Failed to get the latest block number：{}", e.getMessage());
        }

        return latestBlockNumber;
    }

    public BigInteger ethGetBalance(String address) {
        BigInteger balance = null;
        try {
            balance = Web3jHelper.getWeb3j().ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        } catch (IOException e) {
            log.error("Failed to query ETH balance：{}", e.getMessage());
        }
        return balance;
    }

    /**
     * Query defrosted ETH and TOKEN balances
     *
     * @param miner
     * @param token
     * @return
     */
    public Tuple2<BigInteger, BigInteger> getMinerEthAndToken(String miner, String token) {
        Tuple2<BigInteger, BigInteger> r = null;
        try {
            r = ContractBuilder.nestPoolContractReadonly(Web3jHelper.getWeb3j()).getMinerEthAndToken(miner, token).send();
        } catch (Exception e) {
            log.error("Failed to obtain unfrozen ETH and TOKEN balances within the contract：{}", e.getMessage());
        }
        return r;
    }

    /**
     * Query the defrosted TOKEN balance
     *
     * @param miner
     * @param token
     * @return
     */
    public BigInteger balanceOfTokenInPool(String miner, String token) {
        BigInteger r = null;
        try {
            r = ContractBuilder.nestPoolContractReadonly(Web3jHelper.getWeb3j()).balanceOfTokenInPool(miner, token).send();
        } catch (Exception e) {
            log.error("Failed to obtain the unfrozen TOKEN balance within the contract：{}", e.getMessage());
        }
        return r;
    }

    /**
     * Query the number of NEST defrosted and the number of NEST produced
     *
     * @param miner
     * @return
     */
    public BigInteger getMinerNest(String miner) {
        BigInteger r = null;
        try {
            r = ContractBuilder.nestPoolContractReadonly(Web3jHelper.getWeb3j()).getMinerNest(miner).send();
        } catch (Exception e) {
            log.error("Failed to acquire unfrozen Nest assets under contract：{}", e.getMessage());
        }
        return r;
    }

    /**
     * Gets the length of the quotation list for the specified quotation track
     *
     * @param tokenAddress
     * @return
     */
    public BigInteger lengthOfPriceSheets(String tokenAddress) {
        BigInteger r = null;
        try {
            r = ContractBuilder.nestMiningContract(Web3jHelper.getWeb3j()).lengthOfPriceSheets(tokenAddress).send();
        } catch (Exception e) {
            log.error("Failed to get length of quote list：{}", e.getMessage());
        }
        return r;
    }

    /**
     * Gets a frozen quotation at a specified address
     *
     * @param miner        Address of Quotation Miner
     * @param tokenAddress Quotation token address
     * @param maxFindNum   The maximum number of quotations traversed, which is also the number of arrays returned, is 0 if it does not meet the conditions
     * @return
     */
    public List<PriceSheet> unClosedSheetListOf(String miner, String tokenAddress, BigInteger maxFindNum) {
        List<PriceSheet> list = null;
        try {
            NestMiningContract nestMiningContract = ContractBuilder.nestMiningContract(Web3jHelper.getWeb3j());
            BigInteger fromIndex = nestMiningContract.lengthOfPriceSheets(tokenAddress).send().subtract(BigInteger.ONE);
            list = nestMiningContract.unClosedSheetListOf(miner, tokenAddress, fromIndex, maxFindNum).send();
        } catch (Exception e) {
            log.error("Failed to acquire frozen assets：{}", e.getMessage());
        }

        if (!CollectionUtils.isEmpty(list)) {
            List<PriceSheet> reulst = new ArrayList<>();
            for (PriceSheet priceSheet : list) {
                BigInteger height = priceSheet.getHeight();
                // Filter empty data
                if (height == null || height.compareTo(BigInteger.ZERO) == 0) continue;
                reulst.add(priceSheet);
            }
            list = reulst;
        }

        return list;
    }

    /**
     * Gets a collection of unthawed quotations at the specified address
     *
     * @return
     */
    public List<Uint32> canClosedSheetIndexs(List<PriceSheet> list) {
        if (CollectionUtils.isEmpty(list)) return null;
        BigInteger blockNumber = ethBlockNumber();
        if (blockNumber == null) {
            log.error("canClosedSheetIndexs : Failed to get the latest block number");
            return null;
        }

        int size = list.size();
        List<Uint32> closeIndexs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            PriceSheet priceSheet = list.get(i);
            BigInteger height = priceSheet.getHeight();
            if (priceSheet.state == BigInteger.ZERO) continue;
            if (blockNumber.subtract(height).compareTo(nestProperties.getPriceDurationBlock()) > 0) {
                closeIndexs.add(new Uint32(priceSheet.getIndex()));
            }
        }

        return closeIndexs;
    }

    /**
     * Gets the balance of the ERC20 token for the specified account
     *
     * @param address           The account address
     * @param erc20TokenAddress
     * @return
     */
    public BigInteger ethBalanceOfErc20(String address, String erc20TokenAddress) {
        BigInteger balance = null;
        try {
            balance = ContractBuilder.erc20Readonly(erc20TokenAddress, Web3jHelper.getWeb3j()).balanceOf(address).send();
        } catch (Exception e) {
            log.error("Query for ERC20 balance failed：{}", e.getMessage());
        }
        return balance;
    }

    /**
     * Query quotation information: number of remaining ETH and Token
     *
     * @param tokenAddress
     * @param index
     * @return
     */
    public PriceSheetPub priceSheet(String tokenAddress, BigInteger index) {
        PriceSheetPub priceSheetPub = null;
        try {
            priceSheetPub = ContractBuilder.nestMiningContract(Web3jHelper.getWeb3j()).priceSheet(tokenAddress, index).send();
        } catch (Exception e) {
            log.error("Failed to get quotation data according to index：{}", e.getMessage());
        }
        return priceSheetPub;
    }

    public boolean withdrawNest(BigInteger amount, Wallet wallet) {

        BigInteger gasPrice = ethGasPrice(gasPriceState.withdrawType);
        BigInteger nonce = ethGetTransactionCount(wallet.getCredentials().getAddress());
        if (gasPrice == null || nonce == null) {
            log.info("gasPrice || nonce fail to get");
            return false;
        }
        List<Type> typeList = Arrays.<Type>asList(
                new Uint256(amount));

        Function function = new Function("withdrawNest", typeList, Collections.<TypeReference<?>>emptyList());
        String encode = FunctionEncoder.encode(function);
        String transaction = null;
        try {
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce,
                    gasPrice,
                    GasLimit.DEFAULT_GAS_LIMIT,
                    nestProperties.getNestMiningAddress(),
                    BigInteger.ZERO,
                    encode);
            EthSendTransaction ethSendTransaction = ethSendRawTransaction(wallet.getCredentials(), rawTransaction);
            transaction = ethSendTransaction.getTransactionHash();
            if (ethSendTransaction.hasError()) {
                Response.Error error = ethSendTransaction.getError();
                log.error("Failed to retrieve Nest transaction：[msg = {}],[data = {}],[code = {}],[result = {}],[RawResponse = {}],",
                        error.getMessage(),
                        error.getData(),
                        error.getCode(),
                        ethSendTransaction.getResult(),
                        ethSendTransaction.getRawResponse());
            }
            log.info("WithdrawNest transaction hash:{}", transaction);
        } catch (Exception e) {
            log.error("Sending a withdrawing nest transaction fails：{}", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean withdrawEthAndToken(BigInteger ethAmount, String tokenAddress, BigInteger tokenAmount, Wallet wallet) {
        BigInteger gasPrice = ethGasPrice(gasPriceState.withdrawType);
        BigInteger nonce = ethGetTransactionCount(wallet.getCredentials().getAddress());
        if (gasPrice == null || nonce == null) {
            log.info("gasPrice || nonce fail to get");
            return false;
        }

        List<Type> typeList = Arrays.<Type>asList(
                new Uint256(ethAmount),
                new Address(tokenAddress),
                new Uint256(tokenAmount));

        Function function = new Function("withdrawEthAndToken", typeList, Collections.<TypeReference<?>>emptyList());
        String encode = FunctionEncoder.encode(function);
        String transaction = null;
        try {
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce,
                    gasPrice,
                    GasLimit.DEFAULT_GAS_LIMIT,
                    nestProperties.getNestMiningAddress(),
                    BigInteger.ZERO,
                    encode);
            EthSendTransaction ethSendTransaction = ethSendRawTransaction(wallet.getCredentials(), rawTransaction);
            transaction = ethSendTransaction.getTransactionHash();
            if (ethSendTransaction.hasError()) {
                Response.Error error = ethSendTransaction.getError();
                log.error("WithDrawAndToken transaction failed to return：[msg = {}],[data = {}],[code = {}],[result = {}],[RawResponse = {}],",
                        error.getMessage(),
                        error.getData(),
                        error.getCode(),
                        ethSendTransaction.getResult(),
                        ethSendTransaction.getRawResponse());
            }
            log.info("withdrawEthAndToken hash :{}", transaction);
        } catch (Exception e) {
            log.error("Send WithDraweThandToken transaction failed：{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Send quote transaction POST/POST2
     *
     * @param wallet
     * @param gasPrice
     * @param nonce
     * @param typeList
     * @param payableEth
     * @return
     */
    public String offer(String method, Wallet wallet, BigInteger gasPrice, BigInteger nonce, List typeList, BigInteger payableEth) {
        Function function = new Function(method, typeList, Collections.<TypeReference<?>>emptyList());
        String encode = FunctionEncoder.encode(function);
        String transaction = null;
        try {
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce,
                    gasPrice,
                    GasLimit.OFFER_GAS_LIMIT,
                    nestProperties.getNestMiningAddress(),
                    payableEth,
                    encode);
            EthSendTransaction ethSendTransaction = ethSendRawTransaction(wallet.getCredentials(), rawTransaction);
            transaction = ethSendTransaction.getTransactionHash();
            if (ethSendTransaction.hasError()) {
                Response.Error error = ethSendTransaction.getError();
                log.error("Quoted transaction return failed：[msg = {}],[data = {}],[code = {}],[result = {}],[RawResponse = {}],",
                        error.getMessage(),
                        error.getData(),
                        error.getCode(),
                        ethSendTransaction.getResult(),
                        ethSendTransaction.getRawResponse());
            }
        } catch (Exception e) {
            log.error("Send {} quote transaction failed：{}", method, e.getMessage());
        }
        return transaction;
    }

    /**
     * Bulk defrost quotation
     *
     * @param wallet
     * @param address
     * @param indices
     */
    public String closeList(Wallet wallet, BigInteger nonce, String address, List<Uint32> indices) {
        if (CollectionUtils.isEmpty(indices)) return null;
        Credentials credentials = wallet.getCredentials();
        if (nonce == null) {
            log.error("{} ：closeList failed to get nonce", credentials.getAddress());
            return null;
        }

        BigInteger gasPrice = ethGasPrice(gasPriceState.closeSheet);
        if (gasPrice == null) {
            log.error("closeList : failed to get GasPrice");
            return null;
        }

        List<Type> typeList = Arrays.<Type>asList(
                new Address(address),
                new DynamicArray(Uint32.class, indices));
        Function function = new Function("closeList", typeList, Collections.<TypeReference<?>>emptyList());
        String encode = FunctionEncoder.encode(function);

        BigInteger payableEth = BigInteger.ZERO;

        String transactionHash = null;
        try {

            // Batch defrosting due to the size is larger than the fixed, here estimated gas
            org.web3j.protocol.core.methods.request.Transaction transaction =
                    org.web3j.protocol.core.methods.request.Transaction.
                            createFunctionCallTransaction(
                                    wallet.getCredentials().getAddress(),
                                    nonce,
                                    gasPrice,
                                    null,
                                    nestProperties.getNestMiningAddress(),
                                    encode);
            BigInteger amountUsed = getTransactionGasLimit(transaction);
            if (amountUsed == null) {
                amountUsed = GasLimit.CLOSE_GAS_LIMIT;
            } else {
                amountUsed = amountUsed.add(Constant.BIG_INTEGER_200K);
            }

            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce,
                    gasPrice,
                    amountUsed,
                    nestProperties.getNestMiningAddress(),
                    payableEth,
                    encode);

            EthSendTransaction ethSendTransaction = ethSendRawTransaction(credentials, rawTransaction);
            transactionHash = ethSendTransaction.getTransactionHash();
            if (ethSendTransaction.hasError()) {
                Response.Error error = ethSendTransaction.getError();
                log.error("Closelist transaction return failed：[msg = {}],[data = {}],[code = {}],[result = {}],[RawResponse = {}],",
                        error.getMessage(),
                        error.getData(),
                        error.getCode(),
                        ethSendTransaction.getResult(),
                        ethSendTransaction.getRawResponse());
            }
        } catch (Exception e) {
            log.error("Failed to send Closelist transaction：{}", e.getMessage());
        }
        return transactionHash;
    }

    /**
     * Unfreeze individual quotation
     *
     * @param wallet
     * @param nonce
     * @param address
     * @param index
     * @return
     */
    public String close(Wallet wallet, BigInteger nonce, String address, Uint256 index) {
        BigInteger gasPrice = ethGasPrice(gasPriceState.closeSheet);
        if (gasPrice == null) {
            log.error("close : failed to get GasPrice");
        }

        List<Type> typeList = Arrays.<Type>asList(new Address(address), index);
        Function function = new Function("close", typeList, Collections.<TypeReference<?>>emptyList());
        String encode = FunctionEncoder.encode(function);

        BigInteger payableEth = BigInteger.ZERO;

        String transaction = null;
        try {
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce,
                    gasPrice,
                    GasLimit.CLOSE_GAS_LIMIT,
                    nestProperties.getNestMiningAddress(),
                    payableEth,
                    encode);
            EthSendTransaction ethSendTransaction = ethSendRawTransaction(wallet.getCredentials(), rawTransaction);
            transaction = ethSendTransaction.getTransactionHash();
            if (ethSendTransaction.hasError()) {
                Response.Error error = ethSendTransaction.getError();
                log.error("Close transaction failed to return：[msg = {}],[data = {}],[code = {}],[result = {}],[RawResponse = {}],",
                        error.getMessage(),
                        error.getData(),
                        error.getCode(),
                        ethSendTransaction.getResult(),
                        ethSendTransaction.getRawResponse());
            }
        } catch (Exception e) {
            log.error("Send close transaction failed：{}", e.getMessage());
        }
        return transaction;
    }

    public BigInteger getTransactionGasLimit(org.web3j.protocol.core.methods.request.Transaction transaction) {
        BigInteger amountUsed = null;
        try {
            EthEstimateGas ethEstimateGas = Web3jHelper.getWeb3j().ethEstimateGas(transaction).send();
            if (ethEstimateGas.hasError()) {
                log.error("Estimate GasLimit exceptions：{}", ethEstimateGas.getError().getMessage());
                return amountUsed;
            }
            amountUsed = ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            log.error("Estimate GasLimit exceptions：{}", e.getMessage());
        }
        return amountUsed;
    }

    /**
     * Cancel the transaction (use the same nonce as the quoted price, set the GasPrice higher than the quoted price and make a transfer to yourself to override the quoted transaction)
     */
    public String cancelTransaction(Wallet wallet, BigInteger nonce, BigInteger gasPrice) {
        BigInteger payableEth = BigInteger.ZERO;
        String transactionHash = null;
        try {
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                    nonce,
                    gasPrice,
                    GasLimit.CANCEL_GAS_LIMIT,
                    wallet.getCredentials().getAddress(),
                    payableEth);
            EthSendTransaction ethSendTransaction = ethSendRawTransaction(wallet.getCredentials(), rawTransaction);

            transactionHash = ethSendTransaction.getTransactionHash();
            if (ethSendTransaction.hasError()) {
                Response.Error error = ethSendTransaction.getError();
                log.error("Cancel transaction returns failure：[msg = {}],[data = {}],[code = {}],[result = {}],[RawResponse = {}],",
                        error.getMessage(),
                        error.getData(),
                        error.getCode(),
                        ethSendTransaction.getResult(),
                        ethSendTransaction.getRawResponse());
            }
        } catch (Exception e) {
            log.error("Cancellation transaction failed to send:{}", e.getMessage());
        }
        return transactionHash;
    }

    public EthSendTransaction ethSendRawTransaction(Credentials credentials, RawTransaction rawTransaction) throws ExecutionException, InterruptedException {

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        Web3j web3j = Web3jHelper.getWeb3j();
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        return ethSendTransaction;
    }

}
