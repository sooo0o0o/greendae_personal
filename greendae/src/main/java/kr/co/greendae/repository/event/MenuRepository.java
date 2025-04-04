package kr.co.greendae.repository.event;

import kr.co.greendae.entity.event.Event;
import kr.co.greendae.entity.event.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

}

