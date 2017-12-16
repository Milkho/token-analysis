package io.github.milkho.tokenanalysis.filewriter;

import java.io.IOException;
import java.util.List;

import io.github.milkho.tokenanalysis.model.TransferEvent;

public interface IFileWriter {

	String getfilePath();
	
	void setfilePath(String filePath);
	
	void writeToFile(List<TransferEvent> transferEvents) throws IOException;
}
