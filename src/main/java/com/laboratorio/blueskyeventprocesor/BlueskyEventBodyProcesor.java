package com.laboratorio.blueskyeventprocesor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Rafael
 * @version 1.0
 * @param <T>
 * @created 15/08/2024
 * @updated 15/08/2024
 */
public class BlueskyEventBodyProcesor<T> {
    private final Class<T> entityClass;
    private final ObjectMapper mapper;

    public BlueskyEventBodyProcesor(Class<T> entityClass, ObjectMapper mapper) {
        this.entityClass = entityClass;
        this.mapper = mapper;
    }
    
    public T getEventBody(String jsonMessage) throws JsonProcessingException {
        int pos = jsonMessage.indexOf("{", 5);
        String commitStr = jsonMessage.substring(pos);
        return mapper.readValue(commitStr, this.entityClass);
    }
}