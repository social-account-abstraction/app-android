package md.hackaton.aasocialrecovery.contract;

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
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
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
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class SocialRecoveryAccount extends FixedContract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_ADDDEPOSIT = "addDeposit";

    public static final String FUNC_ALERTAGENTS = "alertAgents";

    public static final String FUNC_ENTRYPOINT = "entryPoint";

    public static final String FUNC_EXECUTE = "execute";

    public static final String FUNC_EXECUTEBATCH = "executeBatch";

    public static final String FUNC_FREEZE = "freeze";

    public static final String FUNC_FROZEN = "frozen";

    public static final String FUNC_GETDEPOSIT = "getDeposit";

    public static final String FUNC_GETNONCE = "getNonce";

    public static final String FUNC_INITSOCIALRECOVERY = "initSocialRecovery";

    public static final String FUNC_INITIALIZE = "initialize";

    public static final String FUNC_ONERC1155BATCHRECEIVED = "onERC1155BatchReceived";

    public static final String FUNC_ONERC1155RECEIVED = "onERC1155Received";

    public static final String FUNC_ONERC721RECEIVED = "onERC721Received";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PROXIABLEUUID = "proxiableUUID";

    public static final String FUNC_SOCIALRECOVERYAGENTS = "socialRecoveryAgents";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_TOKENSRECEIVED = "tokensReceived";

    public static final String FUNC_UNFREEZE = "unfreeze";

    public static final String FUNC_UPGRADETO = "upgradeTo";

    public static final String FUNC_UPGRADETOANDCALL = "upgradeToAndCall";

    public static final String FUNC_VALIDATEUSEROP = "validateUserOp";

    public static final String FUNC_WITHDRAWDEPOSITTO = "withdrawDepositTo";

    public static final Event ADMINCHANGED_EVENT = new Event("AdminChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event BEACONUPGRADED_EVENT = new Event("BeaconUpgraded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event FROZEN_EVENT = new Event("Frozen", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event INITIALIZED_EVENT = new Event("Initialized", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
    ;

    public static final Event NEWALERTAGENTS_EVENT = new Event("NewAlertAgents", 
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
    ;

    public static final Event NEWSOCIALRECOVERYAGENTS_EVENT = new Event("NewSocialRecoveryAgents", 
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
    ;

    public static final Event OWNERTRANSFERRED_EVENT = new Event("OwnerTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event SOCIALRECOVERYACCOUNTINITIALIZED_EVENT = new Event("SocialRecoveryAccountInitialized", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event UPGRADED_EVENT = new Event("Upgraded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected SocialRecoveryAccount(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SocialRecoveryAccount(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SocialRecoveryAccount(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SocialRecoveryAccount(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<AdminChangedEventResponse> getAdminChangedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ADMINCHANGED_EVENT, transactionReceipt);
        ArrayList<AdminChangedEventResponse> responses = new ArrayList<AdminChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AdminChangedEventResponse typedResponse = new AdminChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousAdmin = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newAdmin = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AdminChangedEventResponse> adminChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AdminChangedEventResponse>() {
            @Override
            public AdminChangedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ADMINCHANGED_EVENT, log);
                AdminChangedEventResponse typedResponse = new AdminChangedEventResponse();
                typedResponse.log = log;
                typedResponse.previousAdmin = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newAdmin = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AdminChangedEventResponse> adminChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADMINCHANGED_EVENT));
        return adminChangedEventFlowable(filter);
    }

    public static List<BeaconUpgradedEventResponse> getBeaconUpgradedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(BEACONUPGRADED_EVENT, transactionReceipt);
        ArrayList<BeaconUpgradedEventResponse> responses = new ArrayList<BeaconUpgradedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            BeaconUpgradedEventResponse typedResponse = new BeaconUpgradedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.beacon = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BeaconUpgradedEventResponse> beaconUpgradedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, BeaconUpgradedEventResponse>() {
            @Override
            public BeaconUpgradedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(BEACONUPGRADED_EVENT, log);
                BeaconUpgradedEventResponse typedResponse = new BeaconUpgradedEventResponse();
                typedResponse.log = log;
                typedResponse.beacon = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BeaconUpgradedEventResponse> beaconUpgradedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BEACONUPGRADED_EVENT));
        return beaconUpgradedEventFlowable(filter);
    }

    public static List<FrozenEventResponse> getFrozenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(FROZEN_EVENT, transactionReceipt);
        ArrayList<FrozenEventResponse> responses = new ArrayList<FrozenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            FrozenEventResponse typedResponse = new FrozenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.alertAgent = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.reasson = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<FrozenEventResponse> frozenEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, FrozenEventResponse>() {
            @Override
            public FrozenEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(FROZEN_EVENT, log);
                FrozenEventResponse typedResponse = new FrozenEventResponse();
                typedResponse.log = log;
                typedResponse.alertAgent = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.reasson = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<FrozenEventResponse> frozenEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FROZEN_EVENT));
        return frozenEventFlowable(filter);
    }

    public static List<InitializedEventResponse> getInitializedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(INITIALIZED_EVENT, transactionReceipt);
        ArrayList<InitializedEventResponse> responses = new ArrayList<InitializedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            InitializedEventResponse typedResponse = new InitializedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<InitializedEventResponse> initializedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, InitializedEventResponse>() {
            @Override
            public InitializedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(INITIALIZED_EVENT, log);
                InitializedEventResponse typedResponse = new InitializedEventResponse();
                typedResponse.log = log;
                typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<InitializedEventResponse> initializedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(INITIALIZED_EVENT));
        return initializedEventFlowable(filter);
    }

    public static List<NewAlertAgentsEventResponse> getNewAlertAgentsEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NEWALERTAGENTS_EVENT, transactionReceipt);
        ArrayList<NewAlertAgentsEventResponse> responses = new ArrayList<NewAlertAgentsEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewAlertAgentsEventResponse typedResponse = new NewAlertAgentsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newAlertAgents = (List<String>) ((Array) eventValues.getNonIndexedValues().get(0)).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewAlertAgentsEventResponse> newAlertAgentsEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewAlertAgentsEventResponse>() {
            @Override
            public NewAlertAgentsEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWALERTAGENTS_EVENT, log);
                NewAlertAgentsEventResponse typedResponse = new NewAlertAgentsEventResponse();
                typedResponse.log = log;
                typedResponse.newAlertAgents = (List<String>) ((Array) eventValues.getNonIndexedValues().get(0)).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewAlertAgentsEventResponse> newAlertAgentsEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWALERTAGENTS_EVENT));
        return newAlertAgentsEventFlowable(filter);
    }

    public static List<NewSocialRecoveryAgentsEventResponse> getNewSocialRecoveryAgentsEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NEWSOCIALRECOVERYAGENTS_EVENT, transactionReceipt);
        ArrayList<NewSocialRecoveryAgentsEventResponse> responses = new ArrayList<NewSocialRecoveryAgentsEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewSocialRecoveryAgentsEventResponse typedResponse = new NewSocialRecoveryAgentsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newSocialRecoveryAgents = (List<byte[]>) ((Array) eventValues.getNonIndexedValues().get(0)).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewSocialRecoveryAgentsEventResponse> newSocialRecoveryAgentsEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewSocialRecoveryAgentsEventResponse>() {
            @Override
            public NewSocialRecoveryAgentsEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWSOCIALRECOVERYAGENTS_EVENT, log);
                NewSocialRecoveryAgentsEventResponse typedResponse = new NewSocialRecoveryAgentsEventResponse();
                typedResponse.log = log;
                typedResponse.newSocialRecoveryAgents = (List<byte[]>) ((Array) eventValues.getNonIndexedValues().get(0)).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewSocialRecoveryAgentsEventResponse> newSocialRecoveryAgentsEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWSOCIALRECOVERYAGENTS_EVENT));
        return newSocialRecoveryAgentsEventFlowable(filter);
    }

    public static List<OwnerTransferredEventResponse> getOwnerTransferredEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnerTransferredEventResponse> responses = new ArrayList<OwnerTransferredEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OwnerTransferredEventResponse typedResponse = new OwnerTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnerTransferredEventResponse> ownerTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnerTransferredEventResponse>() {
            @Override
            public OwnerTransferredEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERTRANSFERRED_EVENT, log);
                OwnerTransferredEventResponse typedResponse = new OwnerTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnerTransferredEventResponse> ownerTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERTRANSFERRED_EVENT));
        return ownerTransferredEventFlowable(filter);
    }

    public static List<SocialRecoveryAccountInitializedEventResponse> getSocialRecoveryAccountInitializedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SOCIALRECOVERYACCOUNTINITIALIZED_EVENT, transactionReceipt);
        ArrayList<SocialRecoveryAccountInitializedEventResponse> responses = new ArrayList<SocialRecoveryAccountInitializedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SocialRecoveryAccountInitializedEventResponse typedResponse = new SocialRecoveryAccountInitializedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.entryPoint = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SocialRecoveryAccountInitializedEventResponse> socialRecoveryAccountInitializedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SocialRecoveryAccountInitializedEventResponse>() {
            @Override
            public SocialRecoveryAccountInitializedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(SOCIALRECOVERYACCOUNTINITIALIZED_EVENT, log);
                SocialRecoveryAccountInitializedEventResponse typedResponse = new SocialRecoveryAccountInitializedEventResponse();
                typedResponse.log = log;
                typedResponse.entryPoint = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.owner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SocialRecoveryAccountInitializedEventResponse> socialRecoveryAccountInitializedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SOCIALRECOVERYACCOUNTINITIALIZED_EVENT));
        return socialRecoveryAccountInitializedEventFlowable(filter);
    }

    public static List<UpgradedEventResponse> getUpgradedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(UPGRADED_EVENT, transactionReceipt);
        ArrayList<UpgradedEventResponse> responses = new ArrayList<UpgradedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            UpgradedEventResponse typedResponse = new UpgradedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.implementation = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<UpgradedEventResponse> upgradedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, UpgradedEventResponse>() {
            @Override
            public UpgradedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(UPGRADED_EVENT, log);
                UpgradedEventResponse typedResponse = new UpgradedEventResponse();
                typedResponse.log = log;
                typedResponse.implementation = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<UpgradedEventResponse> upgradedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UPGRADED_EVENT));
        return upgradedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addDeposit(BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDDEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<String> alertAgents(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ALERTAGENTS, 
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> entryPoint() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ENTRYPOINT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> execute(String dest, BigInteger value, byte[] func) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_EXECUTE, 
                Arrays.<Type>asList(new Address(160, dest),
                new Uint256(value),
                new DynamicBytes(func)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> executeBatch(List<String> dest, List<byte[]> func) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_EXECUTEBATCH, 
                Arrays.<Type>asList(new DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(dest, Address.class)),
                new DynamicArray<DynamicBytes>(
                        DynamicBytes.class,
                        org.web3j.abi.Utils.typeMap(func, DynamicBytes.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> freeze(String reason) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FREEZE, 
                Arrays.<Type>asList(new Utf8String(reason)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> frozen() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FROZEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> getDeposit() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETDEPOSIT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getNonce() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETNONCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> initSocialRecovery(List<byte[]> newSocialRecoveryAgents, List<String> newAlertAgents) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INITSOCIALRECOVERY, 
                Arrays.<Type>asList(new DynamicArray<Bytes32>(
                        Bytes32.class,
                        org.web3j.abi.Utils.typeMap(newSocialRecoveryAgents, Bytes32.class)),
                new DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(newAlertAgents, Address.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> initialize(String anOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INITIALIZE, 
                Arrays.<Type>asList(new Address(160, anOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<byte[]> onERC1155BatchReceived(String param0, String param1, List<BigInteger> param2, List<BigInteger> param3, byte[] param4) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ONERC1155BATCHRECEIVED, 
                Arrays.<Type>asList(new Address(160, param0),
                new Address(160, param1),
                new DynamicArray<Uint256>(
                        Uint256.class,
                        org.web3j.abi.Utils.typeMap(param2, Uint256.class)),
                new DynamicArray<Uint256>(
                        Uint256.class,
                        org.web3j.abi.Utils.typeMap(param3, Uint256.class)),
                new DynamicBytes(param4)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes4>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> onERC1155Received(String param0, String param1, BigInteger param2, BigInteger param3, byte[] param4) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ONERC1155RECEIVED, 
                Arrays.<Type>asList(new Address(160, param0),
                new Address(160, param1),
                new Uint256(param2),
                new Uint256(param3),
                new DynamicBytes(param4)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes4>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> onERC721Received(String param0, String param1, BigInteger param2, byte[] param3) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ONERC721RECEIVED, 
                Arrays.<Type>asList(new Address(160, param0),
                new Address(160, param1),
                new Uint256(param2),
                new DynamicBytes(param3)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes4>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<byte[]> proxiableUUID() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PROXIABLEUUID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> socialRecoveryAgents(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SOCIALRECOVERYAGENTS, 
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new Bytes4(interfaceId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> unfreeze(List<byte[]> oneTimeSocialRecoveryAgentsKeys, List<byte[]> newSocialRecoveryAgents, List<String> newAlertAgents, String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UNFREEZE, 
                Arrays.<Type>asList(new DynamicArray<Bytes32>(
                        Bytes32.class,
                        org.web3j.abi.Utils.typeMap(oneTimeSocialRecoveryAgentsKeys, Bytes32.class)),
                new DynamicArray<Bytes32>(
                        Bytes32.class,
                        org.web3j.abi.Utils.typeMap(newSocialRecoveryAgents, Bytes32.class)),
                new DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(newAlertAgents, Address.class)),
                new Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> upgradeTo(String newImplementation) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPGRADETO, 
                Arrays.<Type>asList(new Address(160, newImplementation)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> upgradeToAndCall(String newImplementation, byte[] data, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPGRADETOANDCALL, 
                Arrays.<Type>asList(new Address(160, newImplementation),
                new DynamicBytes(data)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> validateUserOp(UserOperation userOp, byte[] userOpHash, BigInteger missingAccountFunds) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_VALIDATEUSEROP, 
                Arrays.<Type>asList(userOp, 
                new Bytes32(userOpHash),
                new Uint256(missingAccountFunds)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> withdrawDepositTo(String withdrawAddress, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_WITHDRAWDEPOSITTO, 
                Arrays.<Type>asList(new Address(160, withdrawAddress),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SocialRecoveryAccount load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SocialRecoveryAccount(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SocialRecoveryAccount load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SocialRecoveryAccount(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SocialRecoveryAccount load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SocialRecoveryAccount(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SocialRecoveryAccount load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SocialRecoveryAccount(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class UserOperation extends DynamicStruct {
        public String sender;

        public BigInteger nonce;

        public byte[] initCode;

        public byte[] callData;

        public BigInteger callGasLimit;

        public BigInteger verificationGasLimit;

        public BigInteger preVerificationGas;

        public BigInteger maxFeePerGas;

        public BigInteger maxPriorityFeePerGas;

        public byte[] paymasterAndData;

        public byte[] signature;

        public UserOperation(String sender, BigInteger nonce, byte[] initCode, byte[] callData, BigInteger callGasLimit, BigInteger verificationGasLimit, BigInteger preVerificationGas, BigInteger maxFeePerGas, BigInteger maxPriorityFeePerGas, byte[] paymasterAndData, byte[] signature) {
            super(new Address(160, sender),
                    new Uint256(nonce),
                    new DynamicBytes(initCode),
                    new DynamicBytes(callData),
                    new Uint256(callGasLimit),
                    new Uint256(verificationGasLimit),
                    new Uint256(preVerificationGas),
                    new Uint256(maxFeePerGas),
                    new Uint256(maxPriorityFeePerGas),
                    new DynamicBytes(paymasterAndData),
                    new DynamicBytes(signature));
            this.sender = sender;
            this.nonce = nonce;
            this.initCode = initCode;
            this.callData = callData;
            this.callGasLimit = callGasLimit;
            this.verificationGasLimit = verificationGasLimit;
            this.preVerificationGas = preVerificationGas;
            this.maxFeePerGas = maxFeePerGas;
            this.maxPriorityFeePerGas = maxPriorityFeePerGas;
            this.paymasterAndData = paymasterAndData;
            this.signature = signature;
        }

        public UserOperation(Address sender, Uint256 nonce, DynamicBytes initCode, DynamicBytes callData, Uint256 callGasLimit, Uint256 verificationGasLimit, Uint256 preVerificationGas, Uint256 maxFeePerGas, Uint256 maxPriorityFeePerGas, DynamicBytes paymasterAndData, DynamicBytes signature) {
            super(sender, nonce, initCode, callData, callGasLimit, verificationGasLimit, preVerificationGas, maxFeePerGas, maxPriorityFeePerGas, paymasterAndData, signature);
            this.sender = sender.getValue();
            this.nonce = nonce.getValue();
            this.initCode = initCode.getValue();
            this.callData = callData.getValue();
            this.callGasLimit = callGasLimit.getValue();
            this.verificationGasLimit = verificationGasLimit.getValue();
            this.preVerificationGas = preVerificationGas.getValue();
            this.maxFeePerGas = maxFeePerGas.getValue();
            this.maxPriorityFeePerGas = maxPriorityFeePerGas.getValue();
            this.paymasterAndData = paymasterAndData.getValue();
            this.signature = signature.getValue();
        }
    }

    public static class AdminChangedEventResponse extends BaseEventResponse {
        public String previousAdmin;

        public String newAdmin;
    }

    public static class BeaconUpgradedEventResponse extends BaseEventResponse {
        public String beacon;
    }

    public static class FrozenEventResponse extends BaseEventResponse {
        public String alertAgent;

        public String reasson;
    }

    public static class InitializedEventResponse extends BaseEventResponse {
        public BigInteger version;
    }

    public static class NewAlertAgentsEventResponse extends BaseEventResponse {
        public List<String> newAlertAgents;
    }

    public static class NewSocialRecoveryAgentsEventResponse extends BaseEventResponse {
        public List<byte[]> newSocialRecoveryAgents;
    }

    public static class OwnerTransferredEventResponse extends BaseEventResponse {
        public String newOwner;
    }

    public static class SocialRecoveryAccountInitializedEventResponse extends BaseEventResponse {
        public String entryPoint;

        public String owner;
    }

    public static class UpgradedEventResponse extends BaseEventResponse {
        public String implementation;
    }
}
