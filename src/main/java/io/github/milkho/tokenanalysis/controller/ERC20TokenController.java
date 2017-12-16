package io.github.milkho.tokenanalysis.controller;

import java.io.IOException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;
import org.web3j.protocol.http.HttpService;

import io.github.milkho.tokenanalysis.dao.ITransferEventDao;
import io.github.milkho.tokenanalysis.dao.TransferEventDao;
import io.github.milkho.tokenanalysis.filewriter.IFileWriter;
import io.github.milkho.tokenanalysis.filewriter.XlsFileWriter;
import io.github.milkho.tokenanalysis.generated.ERC20;
import io.github.milkho.tokenanalysis.model.TransferEvent;
import rx.Subscription;

public class ERC20TokenController {

	private static final String INFURA_MAINNET_URL = "https://mainnet.infura.io/JzOH5HhH3zDd3SrsYDI4";
		
	private Web3j web3j;
	private Credentials credentials;
	private ERC20 token;
	private ITransferEventDao transferEventDao;
	private IFileWriter fileWriter;
	
	private Subscription subscription;
	
	public ERC20TokenController()  {
		web3j = Web3j.build(new HttpService(INFURA_MAINNET_URL));	
		credentials = Credentials.create("0x00");	
		transferEventDao = new TransferEventDao();
		fileWriter = new XlsFileWriter();
	}

	public void processTransactions(final String contractAddress, final long firstBlock, final long lastBlock) {
		token = ERC20.load(contractAddress, web3j, credentials, ERC20.GAS_PRICE, ERC20.GAS_LIMIT);
		
		subscription = web3j.replayBlocksObservable(new DefaultBlockParameterNumber(firstBlock),
				new DefaultBlockParameterNumber(lastBlock), true)		
				.doOnCompleted(this::exportData)
				.doOnTerminate(this::terminate)
				.doOnError(t->{this.printError(t);})
				.subscribe(block -> {
					block.getBlock().getTransactions().stream().map(tx -> TransactionObject.class.cast(tx))
					.filter(tx -> contractAddress.equals(tx.getTo())).forEach(tx -> {
						System.out.println("> new block # " + tx.getBlockNumber().toString());
						
						web3j.ethGetTransactionReceipt(tx.getHash()).sendAsync().thenAccept(txReceipt -> {
							txReceipt.getResult().setContractAddress(contractAddress);
							token.getTransferEvents(txReceipt.getResult()).forEach(eventResponse -> {							
								TransferEvent transferEvent = new TransferEvent();
								transferEvent.setReceiverAddress(eventResponse.to.toString());
								transferEvent.setSenderAddress(eventResponse.from.toString());
								transferEvent.setValue(eventResponse.value.doubleValue());
								transferEvent.setBlockNum(tx.getBlockNumber().longValue());
								transferEvent.setTransactionHash(txReceipt.getResult().getTransactionHash());
								transferEvent.setTimestamp(block.getBlock().getTimestamp().longValue());
								transferEventDao.save(transferEvent);

								System.out.println(">> new transaction at block # " + tx.getBlockNumber().toString());
							});
						});
					});
		});
	}
		 

	private void printError(Throwable a) {
		System.out.println("Error: " + a.getMessage());
	}

	private void exportData() {
		System.out.println("Data loading is complete. Data export started.");
		try {
			fileWriter.writeToFile(transferEventDao.getAll());
			System.out.println("File saved at " + fileWriter.getfilePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void terminate() {
		subscription.unsubscribe();
		transferEventDao.closeConnection();
		System.out.println("Exiting");
		System.exit(0);
	}

}
