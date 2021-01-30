package com.nest.ib.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.6.4.
 */
@SuppressWarnings("rawtypes")
public class INestStaking extends Contract {
    public static final String BINARY = "";

    public static final String FUNC_ADDETHREWARD = "addETHReward";

    public static final String FUNC_CLAIM = "claim";

    public static final String FUNC_LOADCONTRACTS = "loadContracts";

    public static final String FUNC_LOADGOVERNANCE = "loadGovernance";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_RESUME = "resume";

    public static final String FUNC_STAKE = "stake";

    public static final String FUNC_STAKEFROMNESTPOOL = "stakeFromNestPool";

    public static final String FUNC_STAKEDBALANCEOF = "stakedBalanceOf";

    public static final String FUNC_TOTALSTAKED = "totalStaked";

    public static final String FUNC_UNSTAKE = "unstake";

    public static final Event FLAGSET_EVENT = new Event("FlagSet", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NTOKENSTAKED_EVENT = new Event("NTokenStaked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NTOKENUNSTAKED_EVENT = new Event("NTokenUnstaked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event REWARDADDED_EVENT = new Event("RewardAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event REWARDCLAIMED_EVENT = new Event("RewardClaimed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event SAVINGWITHDRAWN_EVENT = new Event("SavingWithdrawn", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected INestStaking(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected INestStaking(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected INestStaking(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected INestStaking(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<FlagSetEventResponse> getFlagSetEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(FLAGSET_EVENT, transactionReceipt);
        ArrayList<FlagSetEventResponse> responses = new ArrayList<FlagSetEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FlagSetEventResponse typedResponse = new FlagSetEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.gov = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.flag = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<FlagSetEventResponse> flagSetEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, FlagSetEventResponse>() {
            @Override
            public FlagSetEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(FLAGSET_EVENT, log);
                FlagSetEventResponse typedResponse = new FlagSetEventResponse();
                typedResponse.log = log;
                typedResponse.gov = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.flag = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<FlagSetEventResponse> flagSetEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FLAGSET_EVENT));
        return flagSetEventFlowable(filter);
    }

    public List<NTokenStakedEventResponse> getNTokenStakedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NTOKENSTAKED_EVENT, transactionReceipt);
        ArrayList<NTokenStakedEventResponse> responses = new ArrayList<NTokenStakedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NTokenStakedEventResponse typedResponse = new NTokenStakedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.ntoken = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NTokenStakedEventResponse> nTokenStakedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NTokenStakedEventResponse>() {
            @Override
            public NTokenStakedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NTOKENSTAKED_EVENT, log);
                NTokenStakedEventResponse typedResponse = new NTokenStakedEventResponse();
                typedResponse.log = log;
                typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.ntoken = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NTokenStakedEventResponse> nTokenStakedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NTOKENSTAKED_EVENT));
        return nTokenStakedEventFlowable(filter);
    }

    public List<NTokenUnstakedEventResponse> getNTokenUnstakedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NTOKENUNSTAKED_EVENT, transactionReceipt);
        ArrayList<NTokenUnstakedEventResponse> responses = new ArrayList<NTokenUnstakedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NTokenUnstakedEventResponse typedResponse = new NTokenUnstakedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.ntoken = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NTokenUnstakedEventResponse> nTokenUnstakedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NTokenUnstakedEventResponse>() {
            @Override
            public NTokenUnstakedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NTOKENUNSTAKED_EVENT, log);
                NTokenUnstakedEventResponse typedResponse = new NTokenUnstakedEventResponse();
                typedResponse.log = log;
                typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.ntoken = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NTokenUnstakedEventResponse> nTokenUnstakedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NTOKENUNSTAKED_EVENT));
        return nTokenUnstakedEventFlowable(filter);
    }

    public List<RewardAddedEventResponse> getRewardAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REWARDADDED_EVENT, transactionReceipt);
        ArrayList<RewardAddedEventResponse> responses = new ArrayList<RewardAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RewardAddedEventResponse typedResponse = new RewardAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ntoken = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.reward = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RewardAddedEventResponse> rewardAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RewardAddedEventResponse>() {
            @Override
            public RewardAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REWARDADDED_EVENT, log);
                RewardAddedEventResponse typedResponse = new RewardAddedEventResponse();
                typedResponse.log = log;
                typedResponse.ntoken = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.reward = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RewardAddedEventResponse> rewardAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REWARDADDED_EVENT));
        return rewardAddedEventFlowable(filter);
    }

    public List<RewardClaimedEventResponse> getRewardClaimedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REWARDCLAIMED_EVENT, transactionReceipt);
        ArrayList<RewardClaimedEventResponse> responses = new ArrayList<RewardClaimedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RewardClaimedEventResponse typedResponse = new RewardClaimedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.ntoken = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.reward = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RewardClaimedEventResponse> rewardClaimedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RewardClaimedEventResponse>() {
            @Override
            public RewardClaimedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REWARDCLAIMED_EVENT, log);
                RewardClaimedEventResponse typedResponse = new RewardClaimedEventResponse();
                typedResponse.log = log;
                typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.ntoken = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.reward = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RewardClaimedEventResponse> rewardClaimedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REWARDCLAIMED_EVENT));
        return rewardClaimedEventFlowable(filter);
    }

    public List<SavingWithdrawnEventResponse> getSavingWithdrawnEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SAVINGWITHDRAWN_EVENT, transactionReceipt);
        ArrayList<SavingWithdrawnEventResponse> responses = new ArrayList<SavingWithdrawnEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SavingWithdrawnEventResponse typedResponse = new SavingWithdrawnEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.ntoken = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SavingWithdrawnEventResponse> savingWithdrawnEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SavingWithdrawnEventResponse>() {
            @Override
            public SavingWithdrawnEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SAVINGWITHDRAWN_EVENT, log);
                SavingWithdrawnEventResponse typedResponse = new SavingWithdrawnEventResponse();
                typedResponse.log = log;
                typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.ntoken = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SavingWithdrawnEventResponse> savingWithdrawnEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SAVINGWITHDRAWN_EVENT));
        return savingWithdrawnEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addETHReward(String ntoken) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDETHREWARD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, ntoken)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> claim(String ntoken) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CLAIM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, ntoken)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> loadContracts() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_LOADCONTRACTS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> loadGovernance() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_LOADGOVERNANCE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> pause() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_PAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> resume() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RESUME, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> stake(String ntoken, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_STAKE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, ntoken), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> stakeFromNestPool(String ntoken, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_STAKEFROMNESTPOOL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, ntoken), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> stakedBalanceOf(String ntoken, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_STAKEDBALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, ntoken), 
                new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> totalStaked(String ntoken) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALSTAKED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, ntoken)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> unstake(String ntoken, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UNSTAKE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, ntoken), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static INestStaking load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new INestStaking(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static INestStaking load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new INestStaking(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static INestStaking load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new INestStaking(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static INestStaking load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new INestStaking(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<INestStaking> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(INestStaking.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<INestStaking> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(INestStaking.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<INestStaking> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(INestStaking.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<INestStaking> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(INestStaking.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class FlagSetEventResponse extends BaseEventResponse {
        public String gov;

        public BigInteger flag;
    }

    public static class NTokenStakedEventResponse extends BaseEventResponse {
        public String user;

        public String ntoken;

        public BigInteger amount;
    }

    public static class NTokenUnstakedEventResponse extends BaseEventResponse {
        public String user;

        public String ntoken;

        public BigInteger amount;
    }

    public static class RewardAddedEventResponse extends BaseEventResponse {
        public String ntoken;

        public String sender;

        public BigInteger reward;
    }

    public static class RewardClaimedEventResponse extends BaseEventResponse {
        public String user;

        public String ntoken;

        public BigInteger reward;
    }

    public static class SavingWithdrawnEventResponse extends BaseEventResponse {
        public String to;

        public String ntoken;

        public BigInteger amount;
    }
}
