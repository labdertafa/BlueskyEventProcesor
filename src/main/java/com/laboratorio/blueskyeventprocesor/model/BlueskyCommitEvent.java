package com.laboratorio.blueskyeventprocesor.model;

import java.util.List;
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
public class BlueskyCommitEvent {
    private int seq;
    private boolean rebase;
    private boolean tooBig;
    private String repo;
    private String commit;
    private String prev;
    private String rev;
    private String since;
    private byte[] blocks;
    private List<BlueskyRepoOp> ops;
    private List<String> blobs;
    private String time;

    @Override
    public String toString() {
        return "BlueskyCommit{" + "seq=" + seq + ", repo=" + repo + ", commit=" + commit + ", ops=" + ops + ", blobs=" + blobs + ", time=" + time + '}';
    }
}