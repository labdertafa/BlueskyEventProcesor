package com.laboratorio.blueskyeventprocesor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 15/08/2024
 * @updated 15/08/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskyHandleEvent {
    private int seq;
    private String did;
    private String handle;
    private String time;

    @Override
    public String toString() {
        return "BlueskyHandleEvent{" + "seq=" + seq + ", did=" + did + ", handle=" + handle + ", time=" + time + '}';
    }
}