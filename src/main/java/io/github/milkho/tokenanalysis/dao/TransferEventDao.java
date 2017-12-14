package io.github.milkho.tokenanalysis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import io.github.milkho.tokenanalysis.model.TransferEvent;

public class TransferEventDao implements ITransferEventDao {

	private Connection connection = null;

	private Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			if (connection == null)
				connection = DriverManager
						.getConnection("jdbc:mysql://localhost/token_analysis?user=root&password=horoshun");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public void save(TransferEvent transferEvent) {

		try {
			PreparedStatement preparedStatement = getConnection()
					.prepareStatement("INSERT INTO token_analysis.transfer_event (value, receiver_address, "
							+ "sender_address, block_num, trans_hash, timestamp) VALUES ( ?, ?, ?, ?, ?, ? )");

			preparedStatement.setDouble(1, transferEvent.getValue());
			preparedStatement.setString(2, transferEvent.getReceiverAddress());
			preparedStatement.setString(3, transferEvent.getSenderAddress());
			preparedStatement.setLong(4, transferEvent.getBlockNum());
			preparedStatement.setString(5, transferEvent.getTransactionHash());
			preparedStatement.setLong(6, transferEvent.getTimestamp());

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public List<TransferEvent> getAll() {
		List<TransferEvent> events = new LinkedList<TransferEvent>();
		try {
			Statement statement = getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM token_analysis.transfer_event");

			TransferEvent event = null;
			while (resultSet.next()) {
				event = new TransferEvent();
				event.setId(Long.parseLong(resultSet.getString("id")));
				event.setValue(Double.valueOf(resultSet.getString("value")));
				event.setReceiverAddress(resultSet.getString("receiver_address"));
				event.setSenderAddress(resultSet.getString("sender_address"));
				event.setBlockNum(Long.parseLong(resultSet.getString("block_num")));
				event.setTransactionHash(resultSet.getString("trans_hash"));
				event.setTimestamp(Long.parseLong(resultSet.getString("timestamp")));

				events.add(event);
			}
			resultSet.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events;
	}

	
	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			// do nothing
		}
	}

}
