package kr.co.greendae.data.first;

import kr.co.greendae.entity.department.Chairperson;
import kr.co.greendae.repository.department.ChairPersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
* 학과장 정보 추가 테스트 코드
* */
@SpringBootTest
public class ChairPersonData {

    /*
    @Autowired
    private ChairPersonRepository chairPersonRepository;

    @Test
    public void  CollegeTest(){




        Chairperson chairperson1 = Chairperson.builder().no(1).name("김국어").build();
        Chairperson chairperson2 = Chairperson.builder().no(2).name("김영어").build();
        Chairperson chairperson3 = Chairperson.builder().no(3).name("김일어").build();
        Chairperson chairperson4 = Chairperson.builder().no(4).name("김중어").build();
        Chairperson chairperson5 = Chairperson.builder().no(5).name("김역사").build();
        Chairperson chairperson6 = Chairperson.builder().no(6).name("김경제").build();
        Chairperson chairperson7 = Chairperson.builder().no(7).name("김법학").build();
        Chairperson chairperson8 = Chairperson.builder().no(8).name("김철학").build();
        Chairperson chairperson9 = Chairperson.builder().no(9).name("김행정").build();
        Chairperson chairperson10 = Chairperson.builder().no(10).name("김사회").build();

        // Repository를 통해 데이터 저장
        chairPersonRepository.save(chairperson1);
        chairPersonRepository.save(chairperson2);
        chairPersonRepository.save(chairperson3);
        chairPersonRepository.save(chairperson4);
        chairPersonRepository.save(chairperson5);
        chairPersonRepository.save(chairperson6);
        chairPersonRepository.save(chairperson7);
        chairPersonRepository.save(chairperson8);
        chairPersonRepository.save(chairperson9);
        chairPersonRepository.save(chairperson10);

    }

*/
}
