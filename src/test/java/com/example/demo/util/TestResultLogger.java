package com.example.demo.util;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestResultLogger implements TestWatcher {

	private static final Logger log = LoggerFactory.getLogger(TestResultLogger.class);

	@Override
	public void testSuccessful(ExtensionContext context) {
		log.info("✅ {} 성공!", context.getDisplayName());
	}

	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		log.error("❌ {} 실패! 원인: {}", context.getDisplayName(), cause.getMessage());
	}

}
