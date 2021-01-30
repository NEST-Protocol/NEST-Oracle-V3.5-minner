package com.nest.ib.contract;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class NestPoolContract extends Contract {

    protected NestPoolContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected NestPoolContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static NestPoolContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NestPoolContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static NestPoolContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NestPoolContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> getNTokenFromToken(String token) throws Exception {
        final Function function = new Function("getNTokenFromToken",
                Arrays.<Type>asList(new Address(token)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * Gets the number of ETH and Tokens thawed within the contract
     * returns (uint256 ethAmount, uint256 tokenAmount)
     *
     * @param miner
     * @param token
     * @return
     */
    public RemoteCall<Tuple2<BigInteger, BigInteger>> getMinerEthAndToken(String miner, String token) {
        final Function function = new Function("getMinerEthAndToken",
                Arrays.<Type>asList(new Address(miner), new Address(token)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }));
        return new RemoteCall<Tuple2<BigInteger, BigInteger>>(
                new Callable<Tuple2<BigInteger, BigInteger>>() {
                    @Override
                    public Tuple2<BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }


    /**
     * Get the Nest defrosted within the contract
     *
     * @param miner
     * @return
     */
    public RemoteCall<BigInteger> getMinerNest(String miner) {
        final Function function = new Function("getMinerNest",
                Arrays.<Type>asList(new Address(miner)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    /**
     * Get the unfrozen token within the contract
     *
     * @param miner
     * @return
     */
    public RemoteCall<BigInteger> balanceOfTokenInPool(String miner, String token) {
        final Function function = new Function("balanceOfTokenInPool",
                Arrays.<Type>asList(new Address(miner), new Address(token)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

}
