package org.bringme.service.impl;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class TestWatchersExtension implements TestWatcher {
    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("Test passed: " + context.getDisplayName());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("Test failed: " + context.getDisplayName() + " with error: " + cause.getMessage());
    }
}
