package com.example.demo.service;

import com.example.demo.entity.Instructor;
import com.example.demo.entity.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TakeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock //invece che usare @Autowired nei test si usa @Mockito
    private InstructorRepository instructorRepositoryUnderTest;

    private AutoCloseable autoCloseable;

    private InstructorService underTest;

    @BeforeEach
        //prima di ogni test
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this); // iniziliazza tutti mock
        underTest = new InstructorService(instructorRepositoryUnderTest);
    }

    @AfterEach
        //dopo i test
    void tearDown() throws Exception {
        autoCloseable.close(); //questo chiude le risorse dopo i test
    }

    //----- INIZIO TEST -----

    /**
     * Test metodo getInstructors
     * Restituisce la lista degli istruttori
     *  public List<Instructor> getInstructors()
     */
    @Test
    void canIGetAllInstructors() {
        //given
        Instructor instructorTest = new Instructor(
                "Walter",
                "White",
                "Chemistry"
        );

        instructorRepositoryUnderTest.save(instructorTest);
        
        //when
        List<Instructor> instructorsObtained = underTest.getInstructors();

        //then
        verify(instructorRepositoryUnderTest).findAll();

    }

    /**
     * Test metodo getInstructor
     * Restituisce un solo istruttor dato il suo id
     *  public Instructor getInstructor(Long instructorId)
     */
    @Test
    void shouldIGetAnInstructorWithHisId() {
        //given
        Instructor instructorTest = new Instructor(
                "Walter",
                "White",
                "Chemistry"
        );

        instructorRepositoryUnderTest.save(instructorTest);

        //when
        Long instructorId = instructorTest.getInstructorId();
        given(instructorRepositoryUnderTest.findById(instructorId))
                .willReturn(Optional.of(instructorTest));

        //then
        Instructor instructorObtained = underTest.getInstructor(instructorId);
        verify(instructorRepositoryUnderTest).findById(instructorId);
        assertThat(instructorObtained).isEqualTo(instructorTest);
    }

    /**
     * Test metodo getInstructor
     * se passato un id scorretto viene restituita un'eccezione
     *  public Instructor getInstructor(Long instructorId)
     */
    @Test
    void TryToGetAnInstructorButTheIdDoesNotExist() {
        //given
        Instructor instructorTest = new Instructor(
                "Walter",
                "White",
                "Chemistry"
        );

        instructorRepositoryUnderTest.save(instructorTest);

        //when
        Long instructorId = instructorTest.getInstructorId();
        given(instructorRepositoryUnderTest.findById(instructorId))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy( () -> underTest.getInstructor(instructorId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The instructor with id "+instructorId+ "doesn't exist");

    }

    /**
     * Test metodo deleteInstructor
     * Elimina un istruttore dato il suo id
     * public void deleteInstructor(Long instructorId)
     */
    @Test
    void shouldIDeleteAnInstructorGivenHisInstructorId() {
        //given
        Instructor instructorTest = new Instructor(
                "Walter",
                "White",
                "Chemistry"
        );

        instructorRepositoryUnderTest.save(instructorTest);

        //when
        Long instructorId = instructorTest.getInstructorId();
        given(instructorRepositoryUnderTest.findById(instructorId))
                .willReturn(Optional.of(instructorTest));

        //then
        underTest.deleteInstructor(instructorId);
        verify(instructorRepositoryUnderTest).deleteById(instructorId);
    }

    /**
     * Test metodo deleteInstructor
     * se passato un id scorretto viene restituita un'eccezione, l'istruttore non viene elimintao
     *  public void deleteInstructor(Long instructorId)
     */
    @Test
    void tryToDeleteAnInstructorPassingAnInexistentInstructorId() {
        //given
        Instructor instructorTest = new Instructor(
                "Walter",
                "White",
                "Chemistry"
        );

        instructorRepositoryUnderTest.save(instructorTest);

        //when
        Long instructorId = instructorTest.getInstructorId();
        given(instructorRepositoryUnderTest.findById(instructorId))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy( () -> underTest.deleteInstructor(instructorId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The instructor with id "+instructorId+ "doesn't exist");

        verify(instructorRepositoryUnderTest, never()).deleteById(instructorId);

    }
}