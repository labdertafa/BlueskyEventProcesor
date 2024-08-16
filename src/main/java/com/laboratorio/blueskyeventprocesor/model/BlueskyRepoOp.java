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
public class BlueskyRepoOp {
    private String action;      // create, update, delete
    private String path;
    private String cid;

    @Override
    public String toString() {
        return "BlueskyRepoOp{" + "action=" + action + ", path=" + path + ", cid=" + cid + '}';
    }
}