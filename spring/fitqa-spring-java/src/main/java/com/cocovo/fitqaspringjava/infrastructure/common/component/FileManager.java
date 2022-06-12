package com.cocovo.fitqaspringjava.infrastructure.common.component;

import com.cocovo.fitqaspringjava.common.exception.FailedConvertToFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Component
public class FileManager {
    public static Optional<File> convertMultipartFileToFile(MultipartFile file) throws FailedConvertToFileException {
        try {
            File convertedFile = new File(file.getOriginalFilename());

            if (convertedFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(convertedFile);
                fos.write(file.getBytes());
                return Optional.of(convertedFile);
            }
            return Optional.empty();
        } catch (IOException e) {
            throw new FailedConvertToFileException("MultipartFile 을 File 로 변환하는데 실패하였습니다.");
        }
    }

    public static Optional<String> getExtensionOfFileName(String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1));
    }
}
