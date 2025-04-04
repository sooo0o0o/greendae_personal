package kr.co.greendae.service;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.querydsl.core.Tuple;

import kr.co.greendae.dto.support.LectureDTO;
import kr.co.greendae.dto.support.RecordDTO;
import kr.co.greendae.dto.support.RegisterDTO;

import kr.co.greendae.dto.support.StudentDTO;
import kr.co.greendae.dto.support.pageRegister.PageRequestDTO;
import kr.co.greendae.dto.support.pageRegister.PageResponseDTO;
import kr.co.greendae.dto.support.pageRegisterList.RegisteredPageRequestDTO;
import kr.co.greendae.dto.support.pageRegisterList.RegisteredPageResponseDTO;
import kr.co.greendae.entity.Lecture.Lecture;
import kr.co.greendae.entity.Lecture.Register;
import kr.co.greendae.entity.user.Professor;
import kr.co.greendae.entity.user.Student;
import kr.co.greendae.repository.support.LectureRepository;
import kr.co.greendae.repository.support.RegisterRepository;

import kr.co.greendae.repository.user.ProfessorRepository;
import kr.co.greendae.repository.user.StudentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SupportService {

    private final LectureRepository lectureRepository;
    private final RegisterRepository registerRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final ProfessorRepository professorRepository;

    public List<LectureDTO> findAll() {
        List<Lecture> lecturesEntities = lectureRepository.findAll();
        // ModelMapper를 사용하여 Lecture 엔티티를 LectureDTO로 변환 후 리스트로 반환
        return lecturesEntities
                .stream()
                .map(lecture -> modelMapper.map(lecture, LectureDTO.class))
                .collect(Collectors.toList());
    }

    public List<String> getLectureYearsByStudent(String stdNo) {

        return lectureRepository.findLectureYearsByStudent(stdNo);
    }

    public int findStudentYearByStdNo(String stdNo) {
        log.info("service##stdNo: {}", stdNo);

        return studentRepository.findYearByStdNo(stdNo);
    }

    public PageResponseDTO searchAll(PageRequestDTO pageRequestDTO, int stdYear) {
        //페이징 처리를 위한 pageable 객체 생성
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageLecture = lectureRepository.searchLecturesByStdNoAndStdYear(pageRequestDTO, pageable, stdYear);

        //변환
        List<LectureDTO> lectureDTOList = pageLecture.getContent().stream().map(tuple -> {
            Lecture lecture = tuple.get(0, Lecture.class);
            String proName = tuple.get(1, String.class);

            LectureDTO lectureDTO = modelMapper.map(lecture, LectureDTO.class);
            lectureDTO.setProName(proName);

            return lectureDTO;
        }).toList();

        int total = (int) pageLecture.getTotalElements();

        return PageResponseDTO
                .builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(lectureDTOList)
                .total(total)
                .build();
    }

    public PageResponseDTO findRegisterByStdNoByGrade(PageRequestDTO pageRequestDTO, int stdYear) {

        // 페이징 처리
        Pageable pageable = pageRequestDTO.getPageable("lecNo");

        // 페이징을 포함한 강의 목록 조회
        Page<Tuple> pageLecture = lectureRepository.selectAllByStdNoAndStdYear(pageable, stdYear);
        log.info("pageLecture: {}", pageLecture);

        // 페이지 정보에서 강의 DTO 리스트 변환
        List<LectureDTO> lectureDTOList = pageLecture.getContent().stream()
                .map(tuple -> {
                    Lecture lecture = tuple.get(0, Lecture.class);
                    LectureDTO lectureDTO = modelMapper.map(lecture, LectureDTO.class);
                    lectureDTO.setLecGrade(lecture.getLecGrade());
                    return lectureDTO;
                })
                .toList();

        // 전체 강의 수
        int total = (int) pageLecture.getTotalElements();

        // 페이지 DTO 반환
        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(lectureDTOList)
                .total(total)
                .build();
    }

    public RegisteredPageResponseDTO findRegisterByStdNo(RegisteredPageRequestDTO registeredPageRequestDTO, String stdNo) {
        //페이징
        Pageable pageable = registeredPageRequestDTO.getPageable("no");

        //페이징을 포함한 목록 조회
        Page<Tuple> pageRegistered = registerRepository.findRegisterByStdNo(pageable, stdNo);
        log.info("pageRegistered: {}", pageRegistered);

        List<RegisterDTO> registerDTOList = pageRegistered.getContent().stream()
                .map(tuple -> {
                    Register register = tuple.get(0, Register.class);
                    RegisterDTO registerDTO = modelMapper.map(register, RegisterDTO.class);

                    registerDTO.setRegStdNo(register.getStudent().getStdNo());// 학생 번호
                    registerDTO.setRegLecNo(register.getLecture().getLecNo());  // 강의 번호
                    registerDTO.setLecCredit(register.getLecture().getLecCredit()); // 학점
                    registerDTO.setLecName(register.getLecture().getLecName());  // 강의명
                    registerDTO.setLecCate(register.getLecture().getLecCate());  // 강의 카테고리
                    registerDTO.setLecGrade(register.getLecture().getLecGrade());  // 강의 학년
                    registerDTO.setLecProName(register.getLecture().getProfessor().getUser().getName());  // 교수명
                    registerDTO.setLecRoom(register.getLecture().getLecRoom());  // 강의실
                    registerDTO.setLecWeekday(register.getLecture().getLecWeekday());

                    return registerDTO;
                })
                .toList();

        //전체 강의 수
        int total = (int) pageRegistered.getTotalElements();

        //페이지 DTO 반환
         return RegisteredPageResponseDTO.builder()
                 .pageRequestDTO(registeredPageRequestDTO)
                 .dtoList(registerDTOList)
                 .total(total)
                 .build();
    }

    public RegisteredPageResponseDTO findGradeByStdNo(RegisteredPageRequestDTO registeredPageRequestDTO, String stdNo) {
        //페이징
        Pageable pageable = registeredPageRequestDTO.getPageable("no");

        //페이징 포함 목록 조회
        Page<Tuple> pageRegisteredGrade = registerRepository.findGradeByStdNo(pageable, stdNo);
        //List<Object[]> optGradeStd = registerRepository.findGradeByStdNo(stdNo);

        List<RegisterDTO> gradeDTOList = pageRegisteredGrade.getContent().stream()
                .map(tuple -> {
                    Register register = tuple.get(0, Register.class);
                    RegisterDTO registerDTO = modelMapper.map(register, RegisterDTO.class);
                    String proNo = register.getLecture().getProfessor().getProNo();
                    Professor professor = professorRepository.findById(proNo).get();

                    registerDTO.setRegStdNo(register.getStudent().getStdNo());
                    registerDTO.setRegLecNo(register.getLecture().getLecNo());
                    registerDTO.setRegTotalScore(register.getRegTotalScore());
                    registerDTO.setRegGradeScore(register.getRegGradeScore());
                    registerDTO.setLecCredit(register.getLecture().getLecCredit());
                    registerDTO.setLecName(register.getLecture().getLecName());
                    registerDTO.setLecCate(register.getLecture().getLecCate());
                    registerDTO.setLecGrade(register.getLecture().getLecGrade());
                    registerDTO.setLecProName(professor.getUser().getName());

                    return registerDTO;
                })
                .toList();

        //전체 강의 수
        int total = (int) pageRegisteredGrade.getTotalElements();

        //페이지 DTO 반환
        return RegisteredPageResponseDTO.builder()
                .pageRequestDTO(registeredPageRequestDTO)
                .dtoList(gradeDTOList)
                .total(total)
                .build();
    }

    public List<StudentDTO> findRecordByStdNo(String stdNo) {
        List<Object[]> optRecordStd = studentRepository.findRecordByStdNo(stdNo);
        log.info("service##optRecordStd : {}", optRecordStd);

        // 각 obj의 타입을 로그로 찍어보기
        optRecordStd.stream().forEach(obj -> {
            for (int i = 0; i < obj.length; i++) {
                log.info("obj[{}] type: {}", i, obj[i].getClass().getName());
            }
        });

        List<StudentDTO> recordList = optRecordStd.stream().map(obj -> {
            StudentDTO studentDTO = modelMapper.map(obj, StudentDTO.class);
            studentDTO.setStdNo((String) obj[0]);
            studentDTO.setStdYear((Integer) obj[1]);
            studentDTO.setStdSemester((String) obj[2]);
            studentDTO.setStdClass((String) obj[3]);
            studentDTO.setStdStatus((String) obj[4]);
            studentDTO.setName((String) obj[5]);
            studentDTO.setHp((String) obj[6]);
            studentDTO.setEmail((String) obj[7]);
            studentDTO.setSsn((String) obj[8]);
            studentDTO.setRegisterCredits((Integer) obj[9]);

            return studentDTO;

        }).collect(Collectors.toList());

        log.info("service##recordList : {}", recordList);

        return recordList;
    }

    public CreditSummary calculateCredits(String stdNo) {
        List<Object[]> registerList = registerRepository.findRegisterByStdNo(stdNo);

        // 학점을 합산할 변수들
        int major = 0;
        int liberalArts = 0;
        int elective = 0;
        int volunteer = 0;
        int other = 0;

        // 갯수를 합산할 변수들
        int majorC = 0;
        int liberalArtsC = 0;
        int electiveC = 0;

        //각 카테고리별 학점 계산 (갯수 * 3)
        int majorCredit = 0;
        int liberalArtsCredit = 0;
        int electiveCredit = 0;
        int volunteerCredit = 0;
        int otherCredit = 0;

        if (!registerList.isEmpty()) {
            Object[] firstRegister = registerList.get(0); // 첫 번째 객체 가져오기
            for (int i = 0; i < firstRegister.length; i++) {
                log.info("Element[{}]: Value = {}, Type = {}", i, firstRegister[i], firstRegister[i] != null ? firstRegister[i].getClass().getName() : "null");
            }
        }

        // 'registerList'에서 각 Object[]를 순회하면서 학점 계산
        Lecture lecture = null;
        for (Object[] register : registerList) {
            registerList.stream().forEach(obj -> {
                for (int i = 0; i < obj.length; i++) {
                    log.info("obj[{}] type: {}", i, obj[i].getClass().getName());
                }
            });

            // regLecNo를 통해 해당 lecture 엔티티 정보 가져오기
            String regLecNo = (String) register[1];

            // regLecNo를 이용해 Lecture 엔티티를 조회
            Optional<Lecture> optlec = lectureRepository.findById(regLecNo); // regLecNo를 통해 Lecture 엔티티 조회
            lecture = optlec.get();

            if (lecture == null) {
                continue; // 만약 해당하는 Lecture가 없다면, 해당 항목을 무시하고 넘어갑니다.
            }

            // Object 배열에서 필요한 데이터를 추출
            String lecCate = (String) register[4]; // '전공', '교양' 등 카테고리 정보
            int lecCredits = (Integer) register[2];
            log.info("@#@##@#@#"+lecCredits);

            // 카테고리에 따라 학점 계산
            if ("전공".equals(lecCate) || "전공필수".equals(lecCate) || "전공선택".equals(lecCate)) {
                major++; // 전공 또는 전공필수는 전공으로 처리
                majorC++;
                majorCredit += (Integer) register[2];
            } else if ("교양".equals(lecCate)) {
                liberalArts++;
                liberalArtsC++;
                liberalArtsCredit += (Integer) register[2];
            } else if ("선택".equals(lecCate)) {
                elective++;
                electiveC++;
                electiveCredit  += (Integer) register[2];
            } else if ("사회봉사".equals(lecCate)) {
                volunteer++;
                volunteerCredit += (Integer) register[2];
            } else {
                other++;
                otherCredit += (Integer) register[2];
            }


        }

        // 총 취득 학점 계산
        int total = majorCredit + liberalArtsCredit + electiveCredit + volunteerCredit + otherCredit;

        // CreditSummary 객체로 결과 반환
        return new CreditSummary(majorCredit, liberalArtsCredit, electiveCredit, volunteerCredit, otherCredit, total, majorC, liberalArtsC, electiveC);

    }

    public StudentDTO findStudentByStdNo(String stdNo) {

        Optional<Student> optstd = studentRepository.findById(stdNo);

        if (optstd.isPresent()) {
            return modelMapper.map(optstd.get(), StudentDTO.class);
        }

        return null;

    }

    public List<LectureDTO> findLectureByLecCate(StudentDTO studentDTO, String year) {

        int lecGrade = Integer.parseInt(year);
        String lecClass = studentDTO.getStdClass();

        // 전공필수 전공선택
        List<Lecture> lectureList = lectureRepository.findByLecClassAndLecGrade(lecClass, lecGrade);

        List<LectureDTO> lectureDTOList = new ArrayList<>();

        for (Lecture lecture : lectureList) {
            LectureDTO lectureDTO = modelMapper.map(lecture, LectureDTO.class);

            if (lectureDTO.getLecCate().contains("전공")) {
                lectureDTOList.add(lectureDTO);
            }
        }

        return lectureDTOList;
    }

    public List<LectureDTO> findLectureByLecClass(StudentDTO studentDTO, String cate) {
        String lecClass = studentDTO.getStdClass();
        List<Lecture> lectureGeneralList = lectureRepository.findByLecClassAndLecCate(lecClass, cate);

        List<LectureDTO> lectureDTOList = new ArrayList<>();
        for (Lecture lecture : lectureGeneralList) {
            LectureDTO lectureDTO = modelMapper.map(lecture, LectureDTO.class);
            lectureDTOList.add(lectureDTO);
        }

        return lectureDTOList;
    }

    public Lecture findLectureByLecNo(String lecNo) {
        Optional<Lecture> optLecture = lectureRepository.findById(lecNo);
        return optLecture.get();

    }

    public boolean registerLecture(StudentDTO studentDTO, Lecture lecture) {

        Student student = modelMapper.map(studentDTO, Student.class);

        Register register = new Register();
        register.setStudent(student);
        register.setLecture(lecture);

        String lecYear = lecture.getLecScheduleStart();
        String[] strs = lecYear.split("-");

        String year = strs[0];
        String month = strs[1];

        String regSemester = null;

        if(month.equals("03") ||month.equals("04") || month.equals("05") || month.equals("06") ){
            regSemester = "1학기";
        }else{
            regSemester = "2학기";
        }

        register.setRegSemester(regSemester);
        register.setRegYear(year);

        // 수강 등록하기
        registerRepository.save(register);

        // 수강 인원 수 더하기
        // lecStdCount를 들고와서 +1하고 save하기
        // lecture.set
        int count = lecture.getLecStdCount();
        lecture.setLecStdCount(count + 1);
        lectureRepository.save(lecture);
        
        // 학생 총점 더하기
        int score = lecture.getLecCredit();
        student.setRegisterCredits(student.getRegisterCredits() + score);
        studentRepository.save(student);

        return true;
    }

    public int totalCredit(RegisteredPageResponseDTO registeredPageResponseDTO) {

        List<RegisterDTO> list = registeredPageResponseDTO.getDtoList();
        int total = 0;
        for(RegisterDTO registerDTO : list){
            total += registerDTO.getLecCredit();
        }

        return total;
    }

    // 수강 취소 메서드
    public boolean cancelLecture(String lecNo) {
        // lecNo로 Lecture 객체 조회
        Optional<Lecture> optionalLecture = lectureRepository.findById(lecNo);

        // Lecture가 존재하는지 확인
        if (optionalLecture.isPresent()) {
            Lecture lecture = optionalLecture.get();

            // 해당 Lecture에 대한 수강 신청 삭제
            Register register = registerRepository.findByLecture(lecture);
            registerRepository.deleteById(register.getRegNo());

            // 강의 인원 수 빼기
            int count = lecture.getLecStdCount();
            lecture.setLecStdCount(count - 1);

            lectureRepository.save(lecture);

            // 학생 총점 -
            //

            // 삭제가 완료되었으면 true 반환
            return true;
        }

        // Lecture가 존재하지 않으면 실패
        return false;
    }

    public List<RecordDTO> calculateRecode(StudentDTO studentDTO) {

        List<RecordDTO> recordDTOS = new ArrayList<>();
        Student student = studentRepository.findByStdNo(studentDTO.getStdNo());
        int startYear = Integer.parseInt(student.getAdmission_year());
        List<Register> re = registerRepository.findByStudent(student);

        int grade = 1;

        System.out.println(startYear);

        // 학생 학번에도 연도 구분
        String semester = "시작";
        Boolean stop = true;
        while(stop){

            RecordDTO recordDTO1 = new RecordDTO();

            // 1학기 시작
            String year = String.valueOf(startYear);

            semester = "1학기";
            List<Register> list = registerRepository.findByStudentAndRegSemesterAndRegYear(student, semester, year );

            if(list.isEmpty()){
                break;
            }

            // 데이터 수작업으로 넣기
            recordDTO1.setYear(year);
            recordDTO1.setGrade(grade);
            recordDTO1.setSemester(semester);

            int score = 0;
            int major = 0;
            for(Register register : list){
                score += register.getLecture().getLecCredit();
                if(register.getLecture().getLecCate().contains("전공")){
                    major += register.getLecture().getLecCredit();
                }
            }

            recordDTO1.setRegister(score);
            recordDTO1.setTotal(score);
            recordDTO1.setEtc(0);
            recordDTO1.setElective(0);
            recordDTO1.setAverage("4.5");
            recordDTO1.setMajor(major);
            recordDTOS.add(recordDTO1);

            // 2학기 데이터 뿌리기
            semester = "2학기";
            List<Register> list2 = registerRepository.findByStudentAndRegSemesterAndRegYear(student, semester, year );

            if(list2.isEmpty()){
                break;
            }

            //데이터 삽입
            RecordDTO recordDTO2 = new RecordDTO();
            recordDTO2.setYear(year);
            recordDTO2.setGrade(grade);
            recordDTO2.setSemester(semester);

            int score2 = 0;
            int major2 = 0;

            for(Register register : list2){
                score2 += register.getLecture().getLecCredit();
                if(register.getLecture().getLecCate().contains("전공")){
                    major2 += register.getLecture().getLecCredit();
                }
            }

            recordDTO2.setRegister(score2);
            recordDTO2.setTotal(score2);
            recordDTO2.setEtc(0);
            recordDTO2.setElective(0);
            recordDTO2.setAverage("4.5");
            recordDTO2.setMajor(major2);

            recordDTOS.add(recordDTO2);

            startYear += 1;

       }

        return recordDTOS;
        
    }

    public Boolean CheckRegister(StudentDTO std, Lecture lecture) {

        Student student = modelMapper.map(std, Student.class);

        //Student, Lecture findByStudent
        return registerRepository.existsByStudentAndLecture(student, lecture);
    }

    @Getter
    public static class CreditSummary {
        private int major;
        private int liberalArts;
        private int elective;
        private int volunteer;
        private int other;
        private int total;

        private int majorC;
        private int liberalArtsC;
        private int electiveC;

        // 생성자
        public CreditSummary(int major, int liberalArts, int elective, int volunteer, int other, int total, int majorC, int liberalArtsC, int electiveC) {
            this.major = major;
            this.liberalArts = liberalArts;
            this.elective = elective;
            this.volunteer = volunteer;
            this.other = other;
            this.total = total;
            this.majorC = majorC;
            this.liberalArtsC = liberalArtsC;
            this.electiveC = electiveC;
        }

        public int getMajor() { return major; }

        public int getLiberalArts() { return liberalArts; }

        public int getElective() { return elective; }

        public int getVolunteer() { return volunteer; }

        public int getOther() { return other; }

        public int getTotal() { return total; }

        public int getMajorC() { return majorC; }

        public int getElectiveC() { return electiveC; }

        public int getLiberalArtsC() { return liberalArtsC; }
    }






    //학과별
    public void findByClass() {
    }

    //과목명
    public void findByClassName() {
    }

    //교수명
    public void findByProfessor() {
    }

    //구분
    public void findByCate() {
    }


    public void modify() {
    }
}