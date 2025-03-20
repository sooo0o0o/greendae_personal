import kr.co.greendae_personal.dto.support.LectureDTO;
import kr.co.greendae_personal.entity.Lecture.Lecture;
import kr.co.greendae_personal.entity.Lecture.Register;
import kr.co.greendae_personal.entity.Lecture.Student;
import kr.co.greendae_personal.repository.support.LectureRepository;
import kr.co.greendae_personal.repository.support.RegisterRepository;
import kr.co.greendae_personal.repository.support.StudentRepository;
import kr.co.greendae_personal.service.SupportService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupportServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SupportServiceTest.class);
    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private RegisterRepository registerRepository;

    @InjectMocks
    private SupportService supportService;

    @Test
    void testRegisterLecture() {
        // Arrange
        String stdNo = "20230001"; // 학생 번호
        int lecNo = 101;  // 강의 번호

        // Mock 객체 설정
        Lecture lecture = new Lecture();
        lecture.setLecNo(lecNo);
        lecture.setLecStdNo(10);  // 기존 수강인원

        Student student = new Student();
        student.setStdNo(stdNo);

        when(lectureRepository.findById(lecNo)).thenReturn(Optional.of(lecture));
        when(studentRepository.findById(stdNo)).thenReturn(Optional.of(student));

        log.info("registerLecture");
        log.info("lecNo: {}", lecNo);
        log.info("student: {}", student);

        // Act
        boolean result = supportService.registerLecture(stdNo, lecNo);

        // Assert
        assertTrue(result);  // 수강 신청이 성공했는지 확인
        verify(lectureRepository, times(1)).save(lecture);  // 강의 저장이 호출되었는지 확인
        verify(registerRepository, times(1)).save(any(Register.class));  // Register 저장이 호출되었는지 확인
    }

    @Test
    void testGetRegisteredLectures() {
        // Arrange
        Lecture lecture1 = new Lecture();
        lecture1.setLecNo(101);
        lecture1.setLecStdNo(5);  // 수강 인원 5명
        lecture1.setLecName("Lecture 1");

        Lecture lecture2 = new Lecture();
        lecture2.setLecNo(102);
        lecture2.setLecStdNo(0);  // 수강 인원 0명
        lecture2.setLecName("Lecture 2");

        List<Lecture> lectures = List.of(lecture1, lecture2);
        when(lectureRepository.findAll()).thenReturn(lectures);

        log.info("getRegisteredLectures");
        log.info("lecture1: {}", lecture1);
        log.info("lecture2: {}", lecture2);


        // Act
        List<LectureDTO> result = supportService.getRegisteredLectures();

        // Assert
        assertEquals(1, result.size());  // 수강 인원 1명 이상인 강의만 반환되어야 함
        assertEquals("Lecture 1", result.get(0).getLecName());  // Lecture 1이 반환되어야 함
    }


    @Test
    void testGetRegisteredLecturesByStudent() {
        // Arrange
        String stdNo = "20230001";  // 학생 번호

        // Mock Lecture 객체
        Lecture lecture1 = new Lecture();
        lecture1.setLecNo(101);
        lecture1.setLecName("Lecture 1");

        // Mock Register 객체
        Register register = new Register();
        register.setLecture(lecture1);

        List<Register> registers = List.of(register);
        when(registerRepository.findByStudent_StdNo(stdNo)).thenReturn(registers);

        // Act
        List<LectureDTO> result = supportService.getRegisteredLecturesByStudent(stdNo);

        log.info("getRegisteredLecturesByStudent");
        log.info("lecture1: {}", lecture1);
        log.info("register: {}", register);


        // Assert
        assertEquals(1, result.size());  // 수강한 강의가 1개여야 함
        assertEquals("Lecture 1", result.get(0).getLecName());  // 수강한 강의명 "Lecture 1" 확인
    }

}
