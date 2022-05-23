package com.example.demo.repository;

import com.example.demo.entity.Instructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class InstructorRepositoryTest {

    @Autowired
    private InstructorRepository underTest;

    @AfterEach//Dopo ogni test viene eliminato tutto, per avere un ambiente pulito
    void tearDown(){
        underTest.deleteAll();
    }

    /**
     *  Test del metodo getNameInstructor, deve restituire il nome dell'istrutore dato il suo id
     *  public String getNameInstructor(Long instructorId);
     */
    @Test
    void itShouldTakeTheInstructorNameFromTheInstructorId(){
        //given
        Instructor instructorTest = new Instructor(
                "Walter",
                "White",
                "Chemistry");

        underTest.save(instructorTest);

        //when
        String nameInstructorExtracted = underTest.getNameInstructor(instructorTest.getInstructorId());
        String nameInstructorExpected = instructorTest.getName();

        //then
        assertThat(nameInstructorExpected).isEqualTo(nameInstructorExtracted);
    }

    /**
     *  Test del metodo getSurnameInstructor, deve restituire il cognome dell'istrutore dato il suo id
     *  public String getSurnameInstructor(Long instructorId);
     */

    @Test
    void itShouldTakeTheInstructorSurnameFromTheInstructorId(){
        //given
        Instructor instructorTest = new Instructor(
                "Walter",
                "White",
                "Chemistry");

        underTest.save(instructorTest);

        //when
        String surnameInstructorExtracted = underTest.getSurnameInstructor(instructorTest.getInstructorId());
        String surnameInstructorExpected = instructorTest.getSurname();

        //then
        assertThat(surnameInstructorExpected).isEqualTo(surnameInstructorExtracted);
    }

    /**
     *  Test del metodo getNameAndSurnameInstructor, deve restituire il nome ed il cognome dell'istrutore dato il suo id
     *  public String getNameAndSurnameInstructor(Long instructorId);
     */

    @Test
    void itShouldTakeTheInstructorNameAndSurnameFromTheInstructorId(){
        //given
        Instructor instructorTest = new Instructor(
                "Walter",
                "White",
                "Chemistry");

        underTest.save(instructorTest);

        //when
        String nameInstructorExtracted = underTest.getNameAndSurnameInstructor(instructorTest.getInstructorId());
        String nameInstructorExpected = instructorTest.getName()+","+instructorTest.getSurname();

        //then
        assertThat(nameInstructorExpected).isEqualTo(nameInstructorExtracted);
    }



}