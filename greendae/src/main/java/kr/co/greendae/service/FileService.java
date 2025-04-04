package kr.co.greendae.service;

import kr.co.greendae.dto.community.ArticleDTO;
import kr.co.greendae.dto.community.FileDTO;
import kr.co.greendae.entity.community.file.BasicFile;
import kr.co.greendae.repository.community.file.BasicFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final BasicFileRepository basicFileRepository;
    private final ModelMapper modelMapper;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    public List<FileDTO> uploadFile(ArticleDTO articleDTO) {
        // 파일 업로드 디렉터리 객체 생성
        File fileUploadDir = new File(uploadDir);

        if(!fileUploadDir.exists()){
            // 파일 업로드 디렉터리가 존재하지 않으면 생성
            fileUploadDir.mkdirs();
        }

        // 파일 업로드 디렉터리 시스템 경로 구하기
        String fileUploadPath = fileUploadDir.getAbsolutePath();
        log.info("fileUploadPath : {}", fileUploadPath);

        // 파일 정보 객체 가져오기
        List<MultipartFile> multipartFiles = articleDTO.getMultipartFiles();

        // 업로드 파일 정보 리스트 생성(반환용)
        List<FileDTO> fileDTOList = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles){

            // 파일 첨부 했으면
            if(!multipartFile.isEmpty()){

                String oName = multipartFile.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf("."));
                String sName = UUID.randomUUID().toString() + ext;

                // 파일 저장
                try {
                    multipartFile.transferTo(new java.io.File(fileUploadPath, sName));
                } catch (IOException e) {
                    log.error(e.getMessage());
                }

                // 반환용 객체 생성
                FileDTO fileDTO = FileDTO.builder()
                        .oName(oName)
                        .sName(sName)
                        .build();

                fileDTOList.add(fileDTO);
            }
        }
        return fileDTOList;
    }

    public void save(FileDTO fileDTO) {

        BasicFile basicFile = modelMapper.map(fileDTO, BasicFile.class);
        basicFileRepository.save(basicFile);

    }

    public ResponseEntity downloadFile(int fno) {

        Optional<BasicFile> optBasicFile = basicFileRepository.findById(fno);

        BasicFile basicFile = null;

        if (optBasicFile.isPresent()) {
            basicFile = optBasicFile.get();

            int count = basicFile.getDownload();
            basicFile.setDownload(count + 1);

            basicFileRepository.save(basicFile);
        }

        try {
            Path path = Paths.get(uploadDir + File.separator + basicFile.getSName());
            String contentType = Files.probeContentType(path);

            // 헤더 정보
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename(basicFile.getOName(), StandardCharsets.UTF_8)
                            .build());
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);

            //파일 다운로드 스트림 작업
            Resource resource = new InputStreamResource(Files.newInputStream(path));

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(resource);
        }catch (IOException e){
            log.error(e.getMessage());
        }

        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();

    }

    public List<FileDTO> findById(int no) {
        List<BasicFile> list = basicFileRepository.findByAno(no);

        List<FileDTO> fileDTOList = new ArrayList<>();
        for(BasicFile basicFile : list){
            FileDTO fileDTO = modelMapper.map(basicFile, FileDTO.class);
            fileDTOList.add(fileDTO);
        }
        return fileDTOList;
    }

    @Transactional
    public void deletebasicFile(int no) {
        basicFileRepository.deleteByAno(no);  // pk
    }
}
