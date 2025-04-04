package kr.co.greendae.service;

import kr.co.greendae.dto.college.CollegeDTO;
import kr.co.greendae.entity.college.College;
import kr.co.greendae.repository.department.CollegeRepository;
import kr.co.greendae.repository.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final ModelMapper modelMapper;

    public CollegeDTO findByName(String college) {

        Optional<College> optCollege = collegeRepository.findByName(college);
        if (optCollege.isPresent()) {
            return modelMapper.map(optCollege.get(), CollegeDTO.class);
        }
        return null;
    }

}
