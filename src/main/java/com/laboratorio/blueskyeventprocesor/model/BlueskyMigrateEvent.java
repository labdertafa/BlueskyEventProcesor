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
public class BlueskyMigrateEvent {
    private int seq;
    private String did;
    private String migrateTo;
    private String time;

    @Override
    public String toString() {
        return "BlueskyMigrateEvent{" + "seq=" + seq + ", did=" + did + ", migrateTo=" + migrateTo + ", time=" + time + '}';
    }   
}