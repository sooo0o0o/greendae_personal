package kr.co.greendae_personal.service;

import kr.co.greendae_personal.dto.support.LectureDTO;
import kr.co.greendae_personal.dto.support.RegisterDTO;
import kr.co.greendae_personal.entity.Lecture.Lecture;
import kr.co.greendae_personal.entity.Lecture.Register;
import kr.co.greendae_personal.entity.Lecture.Student;
import kr.co.greendae_personal.repository.support.LectureRepository;
import kr.co.greendae_personal.repository.support.RegisterRepository;
import kr.co.greendae_personal.repository.support.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SupportService {

    private final LectureRepository lectureRepository;
    private final RegisterRepository registerRepository;
    private final StudentRepository studentRepository;

    public void register(){}

    public List<LectureDTO> findAll(){
        List<Lecture> lecturesEntities = lectureRepository.findAll();

        List<LectureDTO> lectureDTOs = lecturesEntities
                .stream()
                .map(entity -> entity.toLectureDTO())
                .toList();

        return lectureDTOs;
    }

    public List<RegisterDTO> findRegisterByStdNo(@Param("stdNo") String stdNo){
        List<Object[]> optRegisterStd = registerRepository.findRegisterByStdNo(stdNo);
        log.info("optRegisterStd : {}", optRegisterStd);

        //리스트 반환
        List<RegisterDTO> registerDTOList = optRegisterStd.stream().map(obj ->
                RegisterDTO.builder()
                        .regStdNo((String) obj[0])  // 학생 번호
                        .regLecNo((String) obj[1])  // 강의 번호
                        .regCredit((Integer) obj[2]) // 학점
                        .lecName((String) obj[3])  // 강의명
                        .lecCate((String) obj[4])  // 강의 카테고리
                        .lecGrade((Integer) obj[5])  // 강의 학년
                        .lecProName((String) obj[6])  // 교수명
                        .lecRoom((String) obj[7])  // 강의실
                        .lecTime((String) obj[8])  // 강의 시간
                        .build()
                ).collect(Collectors.toList());
        log.info("registerDTOList : {}", registerDTOList);

        return registerDTOList;
    }


    public List<RegisterDTO> findGradeByStdNo(@Param("stdNo") String stdNo){
        List<Object[]> optGradeStd = registerRepository.findGradeByStdNo(stdNo);
        log.info("optGradeStd : {}", optGradeStd);

        // 각 obj의 타입을 로그로 찍어보기
        optGradeStd.stream().forEach(obj -> {
            for (int i = 0; i < obj.length; i++) {
                log.info("obj[{}] type: {}", i, obj[i].getClass().getName());
            }
        });

        List<RegisterDTO> gradeDTOList = optGradeStd.stream().map(obj ->
                RegisterDTO.builder()
                        .regStdNo((String) obj[0])      // 학생 번호
                        .regLecNo((String) obj[1])      // 강의 번호
                        .regTotalScore((Integer) obj[2])    // 강의 점수
                        .regGradeScore((String) obj[3])       // 강의 성적
                        .regCredit((Integer) obj[4])       // 학점
                        .lecName((String) obj[5])     // 과목명
                        .lecCate((String) obj[6])    // 카테고리
                        .lecGrade((Integer) obj[7]) // 이수학년
                        .lecProName((String) obj[8])// 교수명
                        .build()
                ).collect(Collectors.toList());
        log.info("gradeDTOList : {}", gradeDTOList);

        return gradeDTOList;
    }



    /*
    public boolean registerLecture(String stdNo, int lecNo){
        Optional<Lecture> lectureOpt = lectureRepository.findById(lecNo);
        Optional<Student> studentOpt = studentRepository.findById(stdNo); // 학생을 조회

        if (lectureOpt.isPresent() && studentOpt.isPresent()) {
            Lecture lecture = lectureOpt.get();
            Student student = studentOpt.get();

            // Register 엔티티 생성 (수강신청 정보)
            Register register = Register.builder()
                    .student(student)  // 학생 정보
                    .lecture(lecture)  // 강의 정보
                    .regLecName(lecture.getLecName())  // 강의명
                    .regLecProName(lecture.getLecProName())  // 담당 교수
                    .regLecCate(lecture.getLecCate())  // 강의 카테고리
                    .regTotalScore(0)  // 초기 점수 (수정 필요)
                    .regGrade("미수료")  // 초기 등급 (수정 필요)
                    .regCredit(lecture.getLecCredit())  // 강의 학점
                    .build();

            // Register 레코드 저장
            registerRepository.save(register);

            // 강의의 수강 인원 수 증가
            lecture.setLecStdNo(lecture.getLecStdNo() + 1);
            lectureRepository.save(lecture);

            return true;  // 수강 신청 성공
        }

        return false;  // 강의나 학생을 찾을 수 없을 때
    }

    public List<LectureDTO> getRegisteredLectures() {

        List<Lecture> registeredLectures = lectureRepository.findAll().stream()
                .filter(lecture -> lecture.getLecStdNo() > 0)  // 신청 인원이 1명 이상인 강의만 필터링
                .toList();

        return registeredLectures.stream()
                .map(Lecture::toDTO)
                .toList();
    }


    public List<LectureDTO> getRegisteredLecturesByStudent(String stdNo) {
        // RegisterRepository에서 학생 번호로 해당 학생이 수강한 강의를 조회
        List<Register> registers = registerRepository.findByStudent_StdNo(stdNo);

        // 수강한 강의 목록을 LectureDTO로 변환하여 반환
        return registers.stream()
                .map(register -> register.getLecture().toDTO())  // Register에서 Lecture 객체를 가져와 DTO로 변환
                .collect(Collectors.toList());
    }
     */

    public RegisterDTO findById(String stdNo){
        Optional<Register> optRegister = registerRepository.findById(stdNo);
        log.info("optRegister: ", optRegister);
        if (optRegister.isPresent()) {
            Register register = optRegister.get();
            return register.toRegisterDTO();
        }
        return null;
    }

    public void findByName(){}

    public void findByClassName(){}

    public void findByProfessor(){}

    public void modify(){}

    /*
    public boolean delete(String lecNo){
        if(lectureRepository.existsById(Integer.valueOf(lecNo))){
            lectureRepository.deleteById(Integer.valueOf(lecNo));

            return true;
        }
        return false;
    }


    public List<LectureDTO> getGradeByStudent(String stdNo) {
        return registerRepository.findGradesByStudentNo(stdNo);
    }

     */
}
