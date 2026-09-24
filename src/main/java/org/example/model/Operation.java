package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Operation {
    private int operationId;
    private int userId;
    private int type;
    private BigDecimal amount;
    private LocalDateTime operationDate;

    public Operation(int operationId, int userId, int type, BigDecimal amount, LocalDateTime operationDate){
        this.operationId = operationId;
        this.userId = userId;
        this.type =type;
        this.amount = amount;
        this.operationDate = operationDate;
    }

    public int getOperationId() {
        return operationId;
    }

    public int getUserId() {
        return userId;
    }

    public int getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getOperationDate() {
        return operationDate;
    }


}