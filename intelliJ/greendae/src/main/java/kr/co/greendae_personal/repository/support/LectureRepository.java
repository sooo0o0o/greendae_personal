package kr.co.greendae_personal.repository.support;

import kr.co.greendae_personal.dto.support.LectureDTO;
import kr.co.greendae_personal.entity.Lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {

}
