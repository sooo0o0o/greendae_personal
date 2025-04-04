package kr.co.greendae.repository.user;

import kr.co.greendae.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    long countByUid(String uid);

    long countByEmail(String email);

    long countByHp(String hp);


    Optional<User> findUserByEmail(String email);

    User findByEmail(String email);


    User findByName(String name);

    Optional<User> findByUid(String uid);

    List<User> findByEmailAndUid(String email, String uid);
}


