package kr.co.greendae.repository.user;

import kr.co.greendae.entity.department.Department;
import kr.co.greendae.entity.user.Professor;
import kr.co.greendae.entity.user.User;
import kr.co.greendae.repository.user.custom.ProfessorRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor,String> , ProfessorRepositoryCustom {
    List<Professor> findByDepartment(Department department);

    Professor findByUser(User user);
}
