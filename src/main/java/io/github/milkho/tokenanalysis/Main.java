package io.github.milkho.tokenanalysis;

import io.github.milkho.tokenanalysis.controller.ERC20TokenController;


public class Main {

	private static final String STORJ_CONTRACT_ADDRESS = "0xb64ef51c888972c908cfacf59b47c1afbc0ab8ac";

	private static final long FIRST_BLOCK_NUM = 4703582; // 12/1/2017 12:03:13 AM	
	private static final long LAST_BLOCK_NUM =  4716840; // 12/12/2017 12:06:47 AM
																						
	public static void main(String[] agrs) {
		ERC20TokenController tokenController = new ERC20TokenController();
		tokenController.processTransactions(STORJ_CONTRACT_ADDRESS, FIRST_BLOCK_NUM, LAST_BLOCK_NUM);
	}

}
