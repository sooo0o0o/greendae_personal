package kr.co.greendae.service;

import kr.co.greendae.dto.college.CollegeDTO;
import kr.co.greendae.dto.department.DepartmentDTO;
import kr.co.greendae.entity.college.College;
import kr.co.greendae.entity.department.Department;
import kr.co.greendae.repository.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;


    public List<DepartmentDTO> findDepartmentByCollege(String college) {

        List<Department> collegeList = departmentRepository.findAllByCollege(college);
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();
        for (Department department : collegeList) {
            DepartmentDTO departmentDTO = modelMapper.map(department, DepartmentDTO.class);
            departmentDTOS.add(departmentDTO);
        }

        return departmentDTOS;

    }
}
