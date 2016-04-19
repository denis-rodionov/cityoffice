package com.rodionov.cityoffice.services;

import org.apache.log4j.Logger;

public class MailingJob implements Runnable {

	Logger logger = Logger.getLogger(MailingJob.class);
	
	@Override
	public void run() {
		logger.info("MAILING");
	}

}
