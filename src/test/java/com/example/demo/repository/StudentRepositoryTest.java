package com.example.demo.repository;

import com.example.demo.entity.Student;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Andiamo a testare tutti i metodi da noi creati in StudentRepository
 */

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach //Dopo ogni test viene eliminato tutto, per avere un ambiente pulito
    void tearDown(){
        underTest.deleteAll();
    }


    /**
     Test del metodo findStudentByEmail all'interno di StudentRepository
     Optional<Student> findStudentByEmail(String email);
    */
    @Test
    void itShouldCheckIfStudentExistsByEmail() {
        //given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        underTest.save(studentTest);

        //when
        String email = studentTest.getEmail();
        Optional<Student> studentExtracted = underTest.findStudentByEmail(email);

        //then
        assertThat(studentTest).isEqualTo(studentExtracted.get());
    }


}