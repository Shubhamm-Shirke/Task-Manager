package com.leetjourney.taskmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadResponse {

    private String message;
    private Long totalCount;
    private Long successCount;
    private Long FailedCount;

}
