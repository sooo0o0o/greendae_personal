package kr.co.greendae.repository.department;

import kr.co.greendae.entity.department.Chairperson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChairPersonRepository extends JpaRepository<Chairperson,String> {
}
