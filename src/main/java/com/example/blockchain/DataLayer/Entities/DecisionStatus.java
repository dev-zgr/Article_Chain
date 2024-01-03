package com.example.blockchain.DataLayer.Entities;

/**
 * This class is uses to represent the status of a decision
 * Whether it might be the first review or the revision review
 */
public enum DecisionStatus {
    /**
     * This enum case indicates that publication submitted for the first time
     */
    FirstReview,

    /**
     * This enum case indicates that publication submitted for the revision
     */
    RevisionReview
}
