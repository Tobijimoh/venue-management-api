package com.tobi.venuemgmt.model;

/**
 * Represents the lifecycle and operational status of a trading venue.
 */
public enum VenueStatus {
    /**
     * The venue is newly created and awaiting approval from an administrator.
     * It cannot be opened for trading.
     */
    PENDING_APPROVAL,

    /**
     * The venue is approved and is currently open for trading during market hours.
     */
    OPEN,

    /**
     * The venue is approved but is currently closed (e.g., outside of market hours).
     * This is a normal, expected state.
     */
    CLOSED,

    /**
     * Trading has been temporarily stopped by an administrator due to unusual
     * market conditions or technical issues. This is an emergency state.
     */
    HALTED,

    /**
     * The venue has been permanently decommissioned and can no longer be used.
     */
    DECOMMISSIONED
}
