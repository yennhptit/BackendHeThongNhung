package com.mycompany.myapp.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import java.io.IOException;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

@Service
public class GoogleDriveService {

    private final Drive driveService;

    public GoogleDriveService(@Value("${google.drive.credentials.path}") String credentialsPath,
                              @Value("${google.drive.application.name}") String appName) throws GeneralSecurityException, IOException {
        // Load credentials từ file trong classpath
        InputStream in = new ClassPathResource(credentialsPath).getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
            .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

        this.driveService = new Drive.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(),
            new HttpCredentialsAdapter(credentials))
            .setApplicationName(appName)
            .build();
    }

    /**
     * Upload một file lên Google Drive và trả về URL truy cập.
     *
     * @param file MultipartFile nhận từ client
     * @return URL để truy cập file vừa upload
     * @throws IOException nếu có lỗi đọc file hoặc upload
     */
    public String uploadFile(MultipartFile file) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());

        InputStreamContent mediaContent = new InputStreamContent(
            file.getContentType(),
            file.getInputStream()
        );

        // Gọi API upload
        File uploaded = driveService.files()
            .create(fileMetadata, mediaContent)
            .setFields("id")
            .execute();

        String fileId = uploaded.getId();
        com.google.api.services.drive.model.Permission permission = new com.google.api.services.drive.model.Permission()
            .setType("anyone")
            .setRole("reader"); // Cho phép ai cũng có thể xem

        driveService.permissions().create(fileId, permission).execute();

        return "https://drive.google.com/uc?id=" + fileId + "&export=download";
    }
}

