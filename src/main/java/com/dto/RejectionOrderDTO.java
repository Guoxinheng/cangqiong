package com.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RejectionOrderDTO implements Serializable {
    Long id;
    String rejectionReason;
}
