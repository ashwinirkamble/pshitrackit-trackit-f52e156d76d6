package com.premiersolutionshi.common.dao;

public interface UtilDao {

    int getLastInsertPk();

    /**
     * Call BEGIN IMMEDIATE;
     * Other processes can continue to read from the
     * database, however. An exclusive transaction causes EXCLUSIVE locks to be
     * acquired on all databases. After a BEGIN EXCLUSIVE, no other database
     * connection except for read_uncommitted connections will be able to read
     * the database and no other connection without exception will be able to
     * write the database until the transaction is complete.
     */
    void beginImmediate();

    void commit();
}
