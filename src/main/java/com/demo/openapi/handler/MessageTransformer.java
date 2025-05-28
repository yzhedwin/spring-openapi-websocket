package com.demo.openapi.handler;

import com.demo.openapi.model.GenericMessageModel;

public class MessageTransformer {

    // This class is intended to transform entities between different formats or
    // structures.
    // Currently, it does not contain any methods or properties.
    // You can implement transformation logic as needed.

    // Example transformation method (to be implemented):
    public GenericMessageModel transform(Object message) {
        // Transformation logic here
        switch (message) {
            case GenericMessageModel genericMessage -> {
                return genericMessage; // Return as is if already a GenericMessage
            }
            default -> {
                // Handle other types or throw an exception
                throw new IllegalArgumentException("Unsupported message type: " + message.getClass());
            }
        }
    }
}
