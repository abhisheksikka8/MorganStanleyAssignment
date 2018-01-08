package com.morgan.processor;

import java.io.File;


public class MessageProcessingApplication {
	public static void main(String[] args) {		
		File file = new File(ClassLoader.getSystemResource("SampleData.txt").getFile());
		MessageProcessor messageProcessor = new MessageProcessor(file.getAbsolutePath());
		
		messageProcessor.processMessage();
	}
}
