package kr.co.greendae.repository.department;

import kr.co.greendae.dto.department.DepartmentDTO;
import kr.co.greendae.entity.department.Department;
import kr.co.greendae.repository.department.custom.DepartmentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer>, DepartmentRepositoryCustom {
    Optional<Department> findFirstByOrderByDeptNoDesc();

    List<Department> findAllByCollege(String name);

    Department findByDeptName(String departmentD);
}
