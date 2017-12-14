package io.github.milkho.tokenanalysis.dao;

import java.util.List;

import io.github.milkho.tokenanalysis.model.TransferEvent;

public interface ITransferEventDao {

	public void save(TransferEvent transferEvent);

	public List<TransferEvent> getAll();

	public void closeConnection();
}