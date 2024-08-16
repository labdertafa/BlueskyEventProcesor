package com.laboratorio.blueskyeventprocesor;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import com.laboratorio.blueskyeventprocesor.model.BlueskyAccountEvent;
import com.laboratorio.blueskyeventprocesor.model.BlueskyCommitEvent;
import com.laboratorio.blueskyeventprocesor.model.BlueskyEvent;
import com.laboratorio.blueskyeventprocesor.model.BlueskyEventHeader;
import com.laboratorio.blueskyeventprocesor.model.BlueskyHandleEvent;
import com.laboratorio.blueskyeventprocesor.model.BlueskyIdentityEvent;
import com.laboratorio.blueskyeventprocesor.model.BlueskyInfoEvent;
import com.laboratorio.blueskyeventprocesor.model.BlueskyMigrateEvent;
import com.laboratorio.blueskyeventprocesor.model.BlueskyRepoOp;
import com.laboratorio.blueskyeventprocesor.model.BlueskyTombstoneEvent;
import io.ipfs.cid.Cid;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 15/08/2024
 * @updated 15/08/2024
 */
public class BlueskyEventProcesor {
    private final CBORFactory cborFactory;
    private final JsonFactory jsonFactory;
    private final ObjectMapper mapper;

    public BlueskyEventProcesor() {
        this.cborFactory = new CBORFactory();
        this.jsonFactory = new JsonFactory();
        this.mapper = new ObjectMapper();
    }
    
    private String decodeCBORtoJSON(ByteBuffer message) throws IOException {
        // Se recuperan los bytes del mensaje
        byte[] bytes = new byte[message.remaining()];
        message.get(bytes);
        
        // Se convierten los bytes al formato JSON
        CBORParser cborParser = cborFactory.createParser(bytes);
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
        while (cborParser.nextToken() != null) {
            jsonGenerator.copyCurrentEvent(cborParser);
        }
        jsonGenerator.flush();
        
        return stringWriter.toString();
    }
    
    private BlueskyEventHeader getEventHeader(String jsonMessage) throws JsonProcessingException, BlueskyEventException {
        int pos = jsonMessage.indexOf("{", 5);
        
        if (pos <= 5) {
            throw new BlueskyEventException(BlueskyEventProcesor.class.getName(), "El mensaje decodificado tiene un formato inválido");
        }
        
        String headerStr = jsonMessage.substring(0, pos - 1);
        return mapper.readValue(headerStr, BlueskyEventHeader.class);
    }
        
    public BlueskyEvent getBlueskyEvent(ByteBuffer message) throws IOException, BlueskyEventException, JsonProcessingException {
        // Se convierte el mensaje a formato JSON
        String jsonMessage = this.decodeCBORtoJSON(message);
        
        // Se recuperan el header del mensaje
        BlueskyEventHeader header = this.getEventHeader(jsonMessage);

        // Se descartan los mensajes inválidos
        if ((header.getOp() == null) || (header.getT() == null)) {
            throw new BlueskyEventException(BlueskyEventProcesor.class.getName(), "Se trata de un mensaje inválido");
        }
        if (header.getOp().equals(-1)) {
            throw new BlueskyEventException(BlueskyEventProcesor.class.getName(), "Se trata de un mensaje de error");
        }
        
        switch (header.getT()) {
            case "#commit" -> {
                BlueskyEventBodyProcesor<BlueskyCommitEvent> eventBodyProcesor = new BlueskyEventBodyProcesor<>(BlueskyCommitEvent.class, mapper);
                BlueskyCommitEvent commitEvent = eventBodyProcesor.getEventBody(jsonMessage);
                return new BlueskyEvent(header, commitEvent);
            }
            case "#identity" -> {
                BlueskyEventBodyProcesor<BlueskyIdentityEvent> eventBodyProcesor = new BlueskyEventBodyProcesor<>(BlueskyIdentityEvent.class, mapper);
                BlueskyIdentityEvent identityEvent = eventBodyProcesor.getEventBody(jsonMessage);
                return new BlueskyEvent(header, identityEvent);
            }
            case "#account" -> {
                BlueskyEventBodyProcesor<BlueskyAccountEvent> eventBodyProcesor = new BlueskyEventBodyProcesor<>(BlueskyAccountEvent.class, mapper);
                BlueskyAccountEvent accountEvent = eventBodyProcesor.getEventBody(jsonMessage);
                return new BlueskyEvent(header, accountEvent);
            }
            case "#handle" -> {
                BlueskyEventBodyProcesor<BlueskyHandleEvent> eventBodyProcesor = new BlueskyEventBodyProcesor<>(BlueskyHandleEvent.class, mapper);
                BlueskyHandleEvent handleEvent = eventBodyProcesor.getEventBody(jsonMessage);
                return new BlueskyEvent(header, handleEvent);
            }
            case "#migrate" -> {
                BlueskyEventBodyProcesor<BlueskyMigrateEvent> eventBodyProcesor = new BlueskyEventBodyProcesor<>(BlueskyMigrateEvent.class, mapper);
                BlueskyMigrateEvent migrateEvent = eventBodyProcesor.getEventBody(jsonMessage);
                return new BlueskyEvent(header, migrateEvent);
            }
            case "#tombstone" -> {
                BlueskyEventBodyProcesor<BlueskyTombstoneEvent> eventBodyProcesor = new BlueskyEventBodyProcesor<>(BlueskyTombstoneEvent.class, mapper);
                BlueskyTombstoneEvent tombstoneEvent = eventBodyProcesor.getEventBody(jsonMessage);
                return new BlueskyEvent(header, tombstoneEvent);
            }
            case "#info" -> {
                BlueskyEventBodyProcesor<BlueskyInfoEvent> eventBodyProcesor = new BlueskyEventBodyProcesor<>(BlueskyInfoEvent.class, mapper);
                BlueskyInfoEvent infoEvent = eventBodyProcesor.getEventBody(jsonMessage);
                return new BlueskyEvent(header, infoEvent);
            }
            default -> throw new BlueskyEventException(BlueskyEventProcesor.class.getName(), "Se trata de un mensaje desconocido");
        }
    }
    
    private String procesarCid(String encodedCid) {
        // Decodificar el Base64
        byte[] allCidBytes = Base64.getDecoder().decode(encodedCid);
        // Nos saltamos el primer byte
        byte[] decodedBytes = allCidBytes;
        if (allCidBytes[0] != (byte)0x01) {
            decodedBytes = Arrays.copyOfRange(allCidBytes, 1, allCidBytes.length);
        }
        
        Cid cid = Cid.cast(decodedBytes);
        
        return cid.toString();
    }
    
    public BlueskyEvent decodificarCID(BlueskyEvent event) {
        if (event.getType() != BlueskyEventEnum.COMMIT) {
            return event;
        }
        
        try {
            String encodedCommitCid = event.getCommitEvent().getCommit();
            String decodedCommitCid = procesarCid(encodedCommitCid);
            event.getCommitEvent().setCommit(decodedCommitCid);
            for (BlueskyRepoOp repoOp : event.getCommitEvent().getOps()) {
                if (repoOp.getCid() != null) {
                    String decodedCid = procesarCid(repoOp.getCid());
                    repoOp.setCid(decodedCid);
                }
            }
        } catch (Exception e) {
            throw new BlueskyEventException(BlueskyEventProcesor.class.getName(), "Ha ocurrido un error decodificando los CID del evento");
        }
        
        return event;
    }
}