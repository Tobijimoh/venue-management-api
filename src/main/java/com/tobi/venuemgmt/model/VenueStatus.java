package com.tobi.venuemgmt.model;

/**
 * Represents the lifecycle and operational status of a trading venue.
 */
public enum VenueStatus {
   
    /**
     * The venue is approved and is currently open for trading during market hours.
     */
    OPEN,

    /**
     * The venue is approved but is currently closed (e.g., outside of market hours).
     * This is a normal, expected state.
     */
    CLOSED,
}