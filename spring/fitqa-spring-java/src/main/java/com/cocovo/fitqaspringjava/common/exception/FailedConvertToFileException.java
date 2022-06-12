package com.cocovo.fitqaspringjava.common.exception;

import com.cocovo.fitqaspringjava.common.response.ErrorCode;

public class FailedConvertToFileException extends BaseException {
    public FailedConvertToFileException() {
        super(ErrorCode.FAILED_CONVERT_TO_FILE);
    }

    public FailedConvertToFileException(String message) {
        super(message, ErrorCode.FAILED_CONVERT_TO_FILE);
    }

}
