package io.github.milkho.tokenanalysis.filewriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import io.github.milkho.tokenanalysis.model.TransferEvent;

public class XlsFileWriter implements IFileWriter {
	
	private String excelFilePath;
	
	public XlsFileWriter() {
		this.setfilePath("transactionInfo.xls");
	}
	
	public XlsFileWriter(String excelFilePath) {
		this.setfilePath(excelFilePath);
	}
	
	public String getfilePath() {
		return excelFilePath;
	}

	public void setfilePath(String excelFilePath) {
		this.excelFilePath = excelFilePath;
	}
	
	public void writeToFile(List<TransferEvent> transferEvents) throws IOException {
	    Workbook workbook = new HSSFWorkbook();
	    Sheet sheet = workbook.createSheet();
	    
	    int rowCount = 0;
	 
	    setColumnsNames(sheet.createRow(rowCount++));
	    
	    for (TransferEvent transferEvent : transferEvents) {
	        Row row = sheet.createRow(rowCount++);
	        writeTransferEvent(transferEvent, row);
	    }
	 
	    writeStats(sheet, transferEvents);
		

	    try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
	        workbook.write(outputStream);
	    }
	    
	    
	}
	
	private void writeStats(Sheet sheet, List <TransferEvent> transferEvents) {
		
	    Cell cell = sheet.getRow(0).createCell(8);
		cell.setCellValue("Total amount of currency transmitted in transactions");
		
		cell = sheet.getRow(0).createCell(9);
		cell.setCellValue(transferEvents.stream().mapToDouble(o -> o.getValue()).sum());
		
		cell = sheet.getRow(1).createCell(8);
		cell.setCellValue("Min transcation value");
		
		cell = sheet.getRow(1).createCell(9);
		cell.setCellValue(transferEvents.stream().mapToDouble(o -> o.getValue()).min().getAsDouble());
		
		cell = sheet.getRow(2).createCell(8);
		cell.setCellValue("Max transaction value");
		
		cell = sheet.getRow(2).createCell(9);
		cell.setCellValue(transferEvents.stream().mapToDouble(o -> o.getValue()).max().getAsDouble());
		
		cell = sheet.getRow(3).createCell(8);
		cell.setCellValue("Unique senders count");
		
		cell = sheet.getRow(3).createCell(9);
		cell.setCellValue(transferEvents.stream().map(TransferEvent::getReceiverAddress).distinct().count());
		
		cell = sheet.getRow(4).createCell(8);
		cell.setCellValue("Unique receivers count");
		
		cell = sheet.getRow(4).createCell(9);
		cell.setCellValue(transferEvents.stream().map(TransferEvent::getSenderAddress).distinct().count());
	}
	
	private void setColumnsNames(Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue("#");
		
		cell = row.createCell(1);
		cell.setCellValue("Value");
		
		cell = row.createCell(2);
		cell.setCellValue("Sender");
		
		cell = row.createCell(3);
		cell.setCellValue("Receiver");
		
		cell = row.createCell(4);
		cell.setCellValue("BlockNum");
		
		cell = row.createCell(5);
		cell.setCellValue("Tx Hash");
		
		cell = row.createCell(6);
		cell.setCellValue("Date");
	}
	
	private void writeTransferEvent(TransferEvent transferEvent, Row row) {
	    Cell cell = row.createCell(0);
	    cell.setCellValue( transferEvent.getId());

	    cell = row.createCell(1);
	    cell.setCellValue(transferEvent.getValue());
	 
	    cell = row.createCell(2);
	    cell.setCellValue(transferEvent.getSenderAddress());
	    
	    cell = row.createCell(3);
	    cell.setCellValue(transferEvent.getReceiverAddress());
	    
	    cell = row.createCell(4);
	    cell.setCellValue(transferEvent.getBlockNum());
	    
	    cell = row.createCell(5);
	    cell.setCellValue(transferEvent.getTransactionHash());
	    
	    cell = row.createCell(6);
	    cell.setCellValue(new Timestamp(transferEvent.getTimestamp()*1000).toLocalDateTime().toString());
	}

}
