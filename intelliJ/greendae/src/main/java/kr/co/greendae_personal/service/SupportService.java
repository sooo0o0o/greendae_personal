package kr.co.greendae_personal.service;

import kr.co.greendae_personal.dto.support.LectureDTO;
import kr.co.greendae_personal.entity.Lecture.Lecture;
import kr.co.greendae_personal.entity.Lecture.Register;
import kr.co.greendae_personal.entity.Lecture.Student;
import kr.co.greendae_personal.repository.support.LectureRepository;
import kr.co.greendae_personal.repository.support.RegisterRepository;
import kr.co.greendae_personal.repository.support.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                .map(entity -> entity.toDTO())
                .toList();

        return lectureDTOs;
    }

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

    public void findById(){}

    public void findByName(){}

    public void findByClassName(){}

    public void findByProfessor(){}

    public void modify(){}

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
}
