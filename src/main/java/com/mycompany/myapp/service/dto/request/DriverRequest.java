package com.mycompany.myapp.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverRequest {
    @NotBlank
    private String name;

    private String licenseNumber;

    private MultipartFile faceImage;
}
