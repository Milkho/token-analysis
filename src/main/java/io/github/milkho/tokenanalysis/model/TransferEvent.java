package io.github.milkho.tokenanalysis.model;

import java.sql.Timestamp;

import lombok.Data;



public class TransferEvent {

	private Long id;
	private Double value;
	private String receiverAddress;
	private String senderAddress;
	private Long blockNum;
	private String transactionHash;
	private Long timestamp;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public Long getBlockNum() {
		return blockNum;
	}
	public void setBlockNum(Long blockNum) {
		this.blockNum = blockNum;
	}
	public String getTransactionHash() {
		return transactionHash;
	}
	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	

}
