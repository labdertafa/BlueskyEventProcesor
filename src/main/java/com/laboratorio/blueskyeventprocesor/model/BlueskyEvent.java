package com.laboratorio.blueskyeventprocesor.model;

import com.laboratorio.blueskyeventprocesor.BlueskyEventEnum;
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

@Getter @Setter @NoArgsConstructor
public class BlueskyEvent {
    private BlueskyEventEnum type;
    private BlueskyEventHeader header;
    private BlueskyCommitEvent commitEvent;
    private BlueskyIdentityEvent identityEvent;
    private BlueskyAccountEvent accountEvent;
    private BlueskyHandleEvent handleEvent;
    private BlueskyMigrateEvent migrateEvent;
    private BlueskyTombstoneEvent tombstoneEvent;
    private BlueskyInfoEvent infoEvent;

    public BlueskyEvent(BlueskyEventHeader header, BlueskyCommitEvent blueskyCommit) {
        this.type = BlueskyEventEnum.COMMIT;
        this.header = header;
        this.commitEvent = blueskyCommit;
    }

    public BlueskyEvent(BlueskyEventHeader header, BlueskyIdentityEvent blueskyIdentity) {
        this.type = BlueskyEventEnum.IDENTITY;
        this.header = header;
        this.identityEvent = blueskyIdentity;
    }

    public BlueskyEvent(BlueskyEventHeader header, BlueskyAccountEvent accountEvent) {
        this.type = BlueskyEventEnum.ACCOUNT;
        this.header = header;
        this.accountEvent = accountEvent;
    }

    public BlueskyEvent(BlueskyEventHeader header, BlueskyHandleEvent handleEvent) {
        this.type = BlueskyEventEnum.HANDLE;
        this.header = header;
        this.handleEvent = handleEvent;
    }

    public BlueskyEvent(BlueskyEventHeader header, BlueskyMigrateEvent migrateEvent) {
        this.type = BlueskyEventEnum.MIGRATE;
        this.header = header;
        this.migrateEvent = migrateEvent;
    }

    public BlueskyEvent(BlueskyEventHeader header, BlueskyTombstoneEvent tombstoneEvent) {
        this.type = BlueskyEventEnum.TOMBSTONE;
        this.header = header;
        this.tombstoneEvent = tombstoneEvent;
    }

    public BlueskyEvent(BlueskyEventHeader header, BlueskyInfoEvent infoEvent) {
        this.type = BlueskyEventEnum.INFO;
        this.header = header;
        this.infoEvent = infoEvent;
    }
}