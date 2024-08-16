package com.laboratorio.blueskyeventprocesor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 15/08/2024
 * @updated 15/08/2024
 */
public class BlueskyEventException extends RuntimeException {
    private static final Logger log = LogManager.getLogger(BlueskyEventException.class);

    public BlueskyEventException(String className, String message) {
        super(message);
        log.error(String.format("Error %s: %s", className, message));
    }
}