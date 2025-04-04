package kr.co.greendae.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import kr.co.greendae.dto.college.CollegeDTO;
import kr.co.greendae.dto.department.*;
import kr.co.greendae.dto.page.PageRequestDTO;
import kr.co.greendae.dto.support.LectureDTO;
import kr.co.greendae.dto.support.RegisterDTO;
import kr.co.greendae.dto.support.StudentDTO;
import kr.co.greendae.dto.support.pageRegister.PageResponseDTO;
import kr.co.greendae.dto.user.PageProfessorResponseDTO;
import kr.co.greendae.dto.user.PageStudentResponseDTO;
import kr.co.greendae.dto.user.ProfessorDTO;
import kr.co.greendae.dto.user.UserDTO;
import kr.co.greendae.entity.Lecture.Lecture;
import kr.co.greendae.entity.Lecture.Register;
import kr.co.greendae.entity.college.College;
import kr.co.greendae.entity.department.Chairperson;
import kr.co.greendae.entity.department.Department;
import kr.co.greendae.entity.user.Professor;
import kr.co.greendae.entity.user.QStudent;
import kr.co.greendae.entity.user.Student;
import kr.co.greendae.entity.user.User;
import kr.co.greendae.repository.department.ChairPersonRepository;
import kr.co.greendae.repository.department.CollegeRepository;
import kr.co.greendae.repository.department.DepartmentRepository;
import kr.co.greendae.repository.support.LectureRepository;
import kr.co.greendae.repository.support.RegisterRepository;
import kr.co.greendae.repository.user.ProfessorRepository;
import kr.co.greendae.repository.user.StudentRepository;
import kr.co.greendae.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class AdminService {

    private final CollegeRepository collegeRepository;
    private final DepartmentRepository departmentRepository;
    private final ChairPersonRepository chairPersonRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ProfessorRepository professorRepository;
    private final RegisterRepository registerRepository;
    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    // 이미지 등록
    public CollegeDTO uploadImage(CollegeDTO collageDTO) {


        java.io.File fileUploadDir = new java.io.File(uploadDir);

        if (!fileUploadDir.exists()) {
            // 파일 업로드 디렉터리가 존재하지 않으면 생성
            fileUploadDir.mkdirs();
        }

        // 파일 업로드 디렉터리 시스템 경로 구하기
        String fileUploadPath = fileUploadDir.getAbsolutePath();

        MultipartFile multipartFile = collageDTO.getImage();

        if (!multipartFile.isEmpty()) {
            String oName = multipartFile.getOriginalFilename();
            String ext = oName.substring(oName.lastIndexOf("."));
            String sName = UUID.randomUUID().toString() + ext;

            // 파일 저장
            try {
                multipartFile.transferTo(new java.io.File(fileUploadPath, sName));
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            collageDTO.setOName(oName);
            collageDTO.setSName(sName);

        }

        return collageDTO;

    }


    public void save(CollegeDTO collageDTO) {

        College college = modelMapper.map(collageDTO, College.class);
        collegeRepository.save(college);

    }

    public int getMaxDepNo() {
        Optional<Department> optDepartment = departmentRepository.findFirstByOrderByDeptNoDesc();
        if (optDepartment.isPresent()) {
            return optDepartment.get().getDeptNo() + 1;
        }
        return 10;
    }

    public List<CollegeDTO> findAllCollege() {

        List<College> collegeList = collegeRepository.findAll();
        List<CollegeDTO> collegeDTOS = new ArrayList<>();
        for (College college : collegeList) {
            CollegeDTO collegeDTO = modelMapper.map(college, CollegeDTO.class);
            collegeDTOS.add(collegeDTO);
        }
        return collegeDTOS;

    }

    public List<ChairPersonDTO> findAllChairPerson() {

        List<Chairperson> list = chairPersonRepository.findAll();
        List<ChairPersonDTO> chairPersonDTOS = new ArrayList<>();
        for (Chairperson chairperson : list) {
            ChairPersonDTO chairPersonDTO = modelMapper.map(chairperson, ChairPersonDTO.class);
            chairPersonDTOS.add(chairPersonDTO);
        }

        return chairPersonDTOS;
    }

    public void register(DepartmentDTO departmentDTO) {

        Department department = modelMapper.map(departmentDTO, Department.class);
        departmentRepository.save(department);
    }

    public List<DepartmentDTO> findAllDepartmentByName(String name) {

        List<Department> departments = departmentRepository.findAllByCollege(name);
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();

        for (Department department : departments) {
            DepartmentDTO departmentDTO = modelMapper.map(department, DepartmentDTO.class);
            departmentDTOS.add(departmentDTO);
        }

        return departmentDTOS;
    }

    public DepartmentDTO findDepartmentByName(String departmentD) {

        // 수정 이름아님 -> PK로 찾기
        Department department = departmentRepository.findById(Integer.parseInt(departmentD)).get();
        return modelMapper.map(department, DepartmentDTO.class);
    }

    // 교수 등록 메서드
    public String registerUser(UserDTO userDTO, String code) {

        int number = 1;
        String num = code + "001";

        while (userRepository.existsById(num)) {
            number += 1;
            num = String.valueOf((Integer.parseInt(num) + number));
        }

        userDTO.setUid(num);

        User user = modelMapper.map(userDTO, User.class);
        userRepository.save(user);

        return num;
    }


    public void registerProfessor(ProfessorDTO professorDTO, UserDTO userDTO, DepartmentDTO departmentDTO) {

        Professor professor = modelMapper.map(professorDTO, Professor.class);
        User user = modelMapper.map(userDTO, User.class);
        Department department = modelMapper.map(departmentDTO, Department.class);
        professor.setUser(user);
        professor.setDepartment(department);

        professorRepository.save(professor);

    }

    // 학과에 맞는 교수 출력
    public List<ProfessorDTO> findAllProfessorByName(String department) {

        Department findDepartment = departmentRepository.findById(Integer.parseInt(department)).get();
        List<Professor> professors = professorRepository.findByDepartment((findDepartment));
        List<ProfessorDTO> professorDTOS = new ArrayList<>();
        for (Professor professor : professors) {
            professorDTOS.add(modelMapper.map(professor, ProfessorDTO.class));
        }

        return professorDTOS;
    }

    public void registerStudent(StudentDTO studentDTO, UserDTO userDTO, DepartmentDTO departmentDTO) {

        studentDTO.setStdClass(departmentDTO.getDeptName());
        Optional<User> adviser = userRepository.findById(studentDTO.getAdvisor());
        if (adviser.isPresent()) {
            Professor professor = professorRepository.findByUser(adviser.get());

            User user = modelMapper.map(userDTO, User.class);
            Department department = modelMapper.map(departmentDTO, Department.class);

            Student student = modelMapper.map(studentDTO, Student.class);
            student.setUser(user);
            student.setDepartment(department);
            student.setProfessor(professor);
            studentRepository.save(student);
        }
    }

    public void registerLecture(LectureDTO lectureDTO, String lecNo) {

        int number = 1;
        String num = lecNo + "001";

        while (lectureRepository.existsById(num)) {
            number += 1;
            num = String.valueOf((Integer.parseInt(num) + number));
        }

        lectureDTO.setLecNo(num);
        Lecture lecture = modelMapper.map(lectureDTO, Lecture.class);

        User user = userRepository.findByName(lectureDTO.getProNo());
        Professor professor = professorRepository.findById(user.getUid()).get();
        lecture.setProfessor(professor);
        lectureRepository.save(lecture);
    }

    /*
     * 등록 시 카운트 올려주는 메서드
     * */

    public void CountUpProfessor(DepartmentDTO departmentDTO) {

        departmentDTO.setTotalProfessors(departmentDTO.getTotalProfessors() + 1);
        Department department = modelMapper.map(departmentDTO, Department.class);
        departmentRepository.save(department);
    }

    public void CountUpLecture(DepartmentDTO departmentDTO) {

        departmentDTO.setTotalProfessors(departmentDTO.getTotalLecturers() + 1);
        Department department = modelMapper.map(departmentDTO, Department.class);
        departmentRepository.save(department);
    }

    public void CountUpStudent(DepartmentDTO departmentDTO) {

        departmentDTO.setTotalProfessors(departmentDTO.getTotalStudents() + 1);
        Department department = modelMapper.map(departmentDTO, Department.class);
        departmentRepository.save(department);
    }

    public PageDepartmentResponseDTO findAllDepartment(PageDepartmentRequestDTO pageDepartmentRequestDTO) {

        // 페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageDepartmentRequestDTO.getPageable("no");

        Page<Tuple> pageDepartment = departmentRepository.selectAllForList(pageable);

        // Article Entity 리스트를 ArticleDTO 리스트로 변환
        List<DepartmentDTO> departmentDTOS = pageDepartment.getContent().stream().map(tuple -> {

            Department department = tuple.get(0, Department.class);

            DepartmentDTO departmentDTO = modelMapper.map(department, DepartmentDTO.class);

            return departmentDTO;

        }).toList();

        int total = (int) pageDepartment.getTotalElements();

        return PageDepartmentResponseDTO
                .builder()
                .pageRequestDTO(pageDepartmentRequestDTO)
                .dtoList(departmentDTOS)
                .total(total)
                .build();
    }

    public PageDepartmentResponseDTO searchAllDepartment(PageDepartmentRequestDTO pageRequestDTO) {

        // 페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageDepartment = departmentRepository.selectDepartmentForSearch(pageRequestDTO, pageable);
        log.info("pageArticle : {}", pageDepartment);

        // Article Entity 리스트를 ArticleDTO 리스트로 변환
        List<DepartmentDTO> articleDTOList = pageDepartment.getContent().stream().map(tuple -> {

            Department department = tuple.get(0, Department.class);

            DepartmentDTO departmentDTO = modelMapper.map(department, DepartmentDTO.class);

            return departmentDTO;

        }).toList();

        int total = (int) pageDepartment.getTotalElements();

        return PageDepartmentResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(articleDTOList)
                .total(total)
                .build();

    }

    public PageStudentResponseDTO findAllStudent(PageRequestDTO pageRequestDTO) {

        // 페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageStudent = studentRepository.selectAllForList(pageable);

        // Article Entity 리스트를 ArticleDTO 리스트로 변환
        List<StudentDTO> studentDTOS = pageStudent.getContent().stream().map(tuple -> {

            Student student = tuple.get(0, Student.class);

            StudentDTO studentDTO = modelMapper.map(student, StudentDTO.class);

            return studentDTO;

        }).toList();

        int total = (int) pageStudent.getTotalElements();

        return PageStudentResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(studentDTOS)
                .total(total)
                .build();


    }

    public PageStudentResponseDTO searchAllStudent(PageRequestDTO pageRequestDTO) {

        // 페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageObject = studentRepository.selectStudentForSearch(pageRequestDTO, pageable);

        List<StudentDTO> studentDTOList = pageObject.getContent().stream().map(tuple -> {

            Student student = tuple.get(0, Student.class);

            StudentDTO studentDTO = modelMapper.map(student, StudentDTO.class);

            return studentDTO;

        }).toList();

        int total = (int) pageObject.getTotalElements();

        return PageStudentResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(studentDTOList)
                .total(total)
                .build();

    }

    public PageProfessorResponseDTO findAllProfessor(PageRequestDTO pageRequestDTO) {

        // 페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageObject = professorRepository.selectAllForList(pageable);

        // Article Entity 리스트를 ArticleDTO 리스트로 변환
        List<ProfessorDTO> professorDTOS = pageObject.getContent().stream().map(tuple -> {

            Professor professor = tuple.get(0, Professor.class);

            ProfessorDTO professorDTO = modelMapper.map(professor, ProfessorDTO.class);

            return professorDTO;

        }).toList();

        int total = (int) pageObject.getTotalElements();

        return PageProfessorResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(professorDTOS)
                .total(total)
                .build();

    }

    public PageProfessorResponseDTO searchAllProfessor(PageRequestDTO pageRequestDTO) {

        // 페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageObject = professorRepository.selectProfessorForSearch(pageRequestDTO, pageable);

        List<ProfessorDTO> professorDTOS = pageObject.getContent().stream().map(tuple -> {

            Professor professor = tuple.get(0, Professor.class);
            ProfessorDTO professorDTO = modelMapper.map(professor, ProfessorDTO.class);

            return professorDTO;

        }).toList();

        int total = (int) pageObject.getTotalElements();

        return PageProfessorResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(professorDTOS)
                .total(total)
                .build();


    }


    public PageLectureResponseDTO allLecture(PageRequestDTO pageRequestDTO) {

        // 페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageObject = lectureRepository.selectAll(pageable);

        // Article Entity 리스트를 ArticleDTO 리스트로 변환
        List<LectureDTO> lectureDTOS = pageObject.getContent().stream().map(tuple -> {

            Lecture lecture = tuple.get(0, Lecture.class);

            LectureDTO lectureDTO = modelMapper.map(lecture, LectureDTO.class);
            double tot = lectureDTO.getLecStdTotal();
            int cnt = lectureDTO.getLecStdCount();
            double per = (cnt/tot) * 100;
            lectureDTO.setPer((int)per);

            return lectureDTO;

        }).toList();

        int total = (int) pageObject.getTotalElements();

        return PageLectureResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(lectureDTOS)
                .total(total)
                .build();

    }

    // 교육운영 검색
    public PageLectureResponseDTO searchAllLecture(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageObject = lectureRepository.selectProfessorForSearch(pageRequestDTO, pageable);

        List<LectureDTO> lectureDTOS = pageObject.getContent().stream().map(tuple -> {

            Lecture lecture = tuple.get(0, Lecture.class);
            LectureDTO lectureDTO = modelMapper.map(lecture, LectureDTO.class);

            double tot = lectureDTO.getLecStdTotal();
            int cnt = lectureDTO.getLecStdCount();
            double per = (cnt/tot) * 100;
            lectureDTO.setPer((int)per);

            return lectureDTO;

        }).toList();

        int total = (int) pageObject.getTotalElements();

        return PageLectureResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(lectureDTOS)
                .total(total)
                .build();

    }


    public PageRegisterResponseDTO allRegister(PageRequestDTO pageRequestDTO) {

        // 페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageObject = registerRepository.selectAll(pageable);

        // Article Entity 리스트를 ArticleDTO 리스트로 변환
        List<RegisterDTO> registerDTOS = pageObject.getContent().stream().map(tuple -> {

            Register register = tuple.get(0, Register.class);

            RegisterDTO registerDTO = modelMapper.map(register, RegisterDTO.class);

            return registerDTO;

        }).toList();

        int total = (int) pageObject.getTotalElements();

        return PageRegisterResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(registerDTOS)
                .total(total)
                .build();



    }

    public PageRegisterResponseDTO searchAllRegister(PageRequestDTO pageRequestDTO) {


        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageObject = registerRepository.selectRegisterForSearch(pageRequestDTO, pageable);

        List<RegisterDTO> registerDTOS = pageObject.getContent().stream().map(tuple -> {

            Register register = tuple.get(0, Register.class);
            RegisterDTO registerDTO = modelMapper.map(register, RegisterDTO.class);


            return registerDTO;

        }).toList();

        int total = (int) pageObject.getTotalElements();

        return PageRegisterResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(registerDTOS)
                .total(total)
                .build();







    }
}
