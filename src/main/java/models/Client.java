package models;

import enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Client entity
 */
@Data
@AllArgsConstructor
public class Client {
    /**
     * Client's money count
     */
    private double money;
    /**
     * Client's service time
     */
    private long serviceTime;
    private OperationType operationType;
}
