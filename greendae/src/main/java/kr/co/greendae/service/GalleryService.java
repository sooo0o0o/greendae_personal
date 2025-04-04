package kr.co.greendae.service;


import kr.co.greendae.dto.event.GalleryDTO;
import kr.co.greendae.entity.event.Gallery;
import kr.co.greendae.repository.event.GalleryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GalleryService {

    private final ModelMapper modelMapper;
    private final GalleryRepository galleryRepository;



    public List<Gallery> getAllGalleries(){
        return galleryRepository.findAll();
    }

    public GalleryDTO getGalleryById(int no) {
        Optional<Gallery> gallery = galleryRepository.findById(no); // Optional로 받기







        // 임시 맛보기용 코드

        GalleryDTO galleryDTO = modelMapper.map(gallery, GalleryDTO.class);

        return galleryDTO;
    }



}
