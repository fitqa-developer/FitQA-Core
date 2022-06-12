package com.cocovo.fitqaspringjava.infrastructure.common.component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cocovo.fitqaspringjava.common.exception.FailedConvertToFileException;
import com.cocovo.fitqaspringjava.domain.common.FileUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3FileUploader implements FileUploader {

    private final FileManager fileManager;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public List<String> uploadFiles(String destination, MultipartFile... multipartFiles) throws FailedConvertToFileException {
        var files = convertToFile(multipartFiles);
        var uploadedFileUrl = files.stream().map(file -> {
            final String fileName = createUploadFileNameWithCreationDate(destination, file);
            final String fileUrl = putS3(file, fileName);
            return fileUrl;
        }).collect(Collectors.toList());
        return uploadedFileUrl;
    }

    private ArrayList<File> convertToFile(MultipartFile... multipartFiles) throws FailedConvertToFileException {
        var files = new ArrayList<File>();

        for (MultipartFile multipartFile : multipartFiles) {
            Optional<File> convertedFileOpt = fileManager.convertMultipartFileToFile(multipartFile);
            if (convertedFileOpt.isEmpty()) {
                throw new FailedConvertToFileException("파일 생성을 실패하였습니다.");
            }
            files.add(convertedFileOpt.get());
        }
        return files;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
        return amazonS3Client.getUrl(bucket, fileName)
                .toString();
    }

    private String createUploadFileNameWithCreationDate(String destination, File file) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        Optional<String> fileExtension = FileManager.getExtensionOfFileName(file.getName());
        return destination + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss"))
                + "." + fileExtension.get();
    }
}
