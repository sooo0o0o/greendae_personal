package kr.co.greendae_personal.repository.support;


import kr.co.greendae_personal.entity.Lecture.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {

}
