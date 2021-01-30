package com.nest.ib.contract;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class NestMiningContract extends Contract {

    protected NestMiningContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected NestMiningContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    /**
     * Get the latest USDTETH mining quote block number
     *
     * @return
     */
    public RemoteCall<BigInteger> latestMinedHeight() {
        final Function function = new Function("latestMinedHeight",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Params> parameters() {
        final Function function = new Function("parameters",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Params>() {
                }));
        return executeRemoteCallSingleValueReturn(function,Params.class);
    }

    /**
     * Gets a frozen quotation at the specified address
     *
     * @param miner     Address of Quotation Miner
     * @param token     Quotation token address
     * @param fromIndex Index value, reverse order from the quotation to find their own quotation
     * @param num       Number of returns
     * @return
     */
    public RemoteFunctionCall<List> unClosedSheetListOf(String miner, String token, BigInteger fromIndex, BigInteger num) {
        final Function function = new Function("unClosedSheetListOf",
                Arrays.<Type>asList(new Address(miner),new Address(token),new Uint256(fromIndex),new Uint256(num)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<PriceSheet>>() {
                }));
        return executeRemoteCallSingleValueReturn(function,List.class);
    }

    public RemoteFunctionCall<PriceSheetPub> priceSheet(String tokenAddress, BigInteger index) {
        final Function function = new Function("priceSheet",
                Arrays.<Type>asList(new Address(tokenAddress),new Uint256(index)),
                Arrays.<TypeReference<?>>asList(new TypeReference<PriceSheetPub>() {
                }));
        return executeRemoteCallSingleValueReturn(function, PriceSheetPub.class);
    }


    /**
     * Gets the current total length of the list of offers for the specified token
     *
     * @return
     */
    public RemoteCall<BigInteger> lengthOfPriceSheets(String tokenAddress) {
        final Function function = new Function("lengthOfPriceSheets",
                Arrays.<Type>asList(new Address(tokenAddress)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static NestMiningContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NestMiningContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static NestMiningContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NestMiningContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
