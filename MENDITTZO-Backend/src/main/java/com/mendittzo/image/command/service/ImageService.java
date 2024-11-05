package com.mendittzo.image.command.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ImageService {

    private final AmazonS3Client amazonS3Client;
    private final String bucket;

    // 허용된 파일 확장자 목록
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    // S3Config 에서 만든 amazonS3 client 의존성 주입
    public ImageService(AmazonS3Client amazonS3Client, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
    }

    // 요청받은 이미지 리스트를 S3와 DB에 저장
    @Transactional
    public String upload(MultipartFile multipartFile) throws IOException {

        log.info("upload 시작");
        String originalFileName;
        String uuid;
        String uniqueFileName;
        String fileName;
        String fileType;

        // 파일 이름에서 공백을 제거한 새로운 파일 이름 생성 >> origin_file_name
        originalFileName = multipartFile.getOriginalFilename();

        // 확장자 추출
        fileType = FilenameUtils.getExtension(originalFileName).toLowerCase();

        // 확장자 검증
        if (!ALLOWED_EXTENSIONS.contains(fileType)) {
            throw new CustomException(ErrorCode.NOT_MATCH_FILE_EXTENSION); // 유효하지 않은 확장자 처리
        }

        // UUID를 파일명에 추가
        uuid = UUID.randomUUID().toString();

        uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        // 저장될 directory 명 + 파일명 >> filName
        fileName ="profileImages/" + uniqueFileName;
        log.info("fileName: " + fileName);

        File uploadFile = convert(multipartFile,uniqueFileName);

        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);

        return uploadImageUrl;
    }

    private File convert(MultipartFile file, String uniqueFileName) throws IOException {

        File convertFile = new File(uniqueFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                log.error("파일 변환 중 오류 발생: {}", e.getMessage());
                throw e;
            }
            return convertFile;
        }
        throw new IllegalArgumentException(String.format("파일 변환에 실패했습니다."));
    }

    private String putS3(File uploadFile, String fileName) {

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        log.info(uploadFile + "업로드");
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {

        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    @Transactional
    public String updateImage(MultipartFile newImage, String oldImage) throws IOException {

        deleteS3File(oldImage);

        return upload(newImage);
    }

    public void deleteS3File(String fileName) {

        try {
            // URL 디코딩을 통해 원래의 파일 이름을 가져옵니다.
            String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("Deleting file from S3: " + decodedFileName);
            amazonS3Client.deleteObject(bucket, decodedFileName);

        } catch (UnsupportedEncodingException e) {
            log.error("Error while decoding the file name: {}", e.getMessage());
        }
    }

}