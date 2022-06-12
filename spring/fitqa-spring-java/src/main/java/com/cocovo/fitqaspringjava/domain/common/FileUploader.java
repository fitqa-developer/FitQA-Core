package com.cocovo.fitqaspringjava.domain.common;

import com.cocovo.fitqaspringjava.common.exception.FailedConvertToFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploader {

    List<String> uploadFiles(String destination, MultipartFile... multipartFiles) throws FailedConvertToFileException;
}

