package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TakeRepository;
import org.assertj.core.internal.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

//eseguendo il debug with coverage abbiamo la percentuale di copertura del codice ottenuta con i test
// nel mentre si può andare nel codice esistente, che si sta testando, per vedere quali parti di codice
// sono coperte da test e quali invece non sono state testate. Questo si nota da una linea  (verde o rossa)
// presente alla sinistra del codice, accanto ai numeri di riga

@ExtendWith(MockitoExtension.class) // questa aggiunta permette di non scrivere la variabile autoCloseable ed il metodo tearDown
class StudentServiceTest {

    @Mock //invece che usare @Autowired nei test si usa @Mockito
    private StudentRepository studentRepositoryUnderTest;

    @Mock
    private CourseRepository courseRepositoryUnderTest;

    @Mock
    private TakeRepository takeRepositoryUnderTest;

    private AutoCloseable autoCloseable;

    private StudentService underTest;

    @BeforeEach //prima di ogni test
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this); // iniziliazza tutti mock
        underTest = new StudentService(studentRepositoryUnderTest, courseRepositoryUnderTest, takeRepositoryUnderTest);
    }

    @AfterEach //dopo i test
    void tearDown() throws Exception {
        autoCloseable.close(); //questo chiude le risorse dopo i test
        studentRepositoryUnderTest.deleteAll();
        courseRepositoryUnderTest.deleteAll();
        takeRepositoryUnderTest.deleteAll();
    }

    //----- INIZIO TEST -----

    /**
     * Test metodo getStudent
     * Se tutto va a buon fine, ovvero restituisce uno studente
     */
    @Test
    void canGetAStudent() {
        //given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        //when
        long studentId = any(long.class);

        given(studentRepositoryUnderTest.findById(studentId))
                .willReturn(Optional.of(studentTest));

        //then
        assertThat(studentTest).isEqualTo(underTest.getStudent(studentId));

    }

    /**
     * Test metodo getStudent
     * Se viene lanciato l'errore, cioè viene passato uno studente che non esiste
     */
    @Test
    void willThrowAnErroreWhenStudentDoesNotExist() {
        //given
        //given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        //when
        long studentId = any(long.class); //qualsiasi long

        //devo far scattare l'errore
        given(studentRepositoryUnderTest.findById(studentId))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy( () -> underTest.getStudent(studentId)) //assicuriamo che scatti il throw quando chiamiamo il metodo uploadStudent
                .isInstanceOf(IllegalStateException.class) //classe dell'errore che si deve ottenere
                .hasMessageContaining("The student with id "+studentId+ " doesn't exist"); //scattato l'errore dobbiamo avere questo messaggio, cioè quello programmato

    }

    //Test metodo getStudents
    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();
        //then
        verify(studentRepositoryUnderTest).findAll(); //controlliamo che nel nostro repository (cioè il nostro mock) è stato invocato il metodo findAll, usato nel metodo di test

        //andiamo a fare in questa maniera perchè non vogliamo testare il nostro repository,
        // che abbiamo già testato e sappiamo che funziona (per questo lo mockiamo), ma controlliamo che funziona il service
    }

    //Test metodo uploadStudent
    @Test
    void canUploadAStudent() {
        //Given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        //when
        underTest.uploadStudent(studentTest);

        //then

        //controlliamo che i servizi usati dentro il metodo siano stati usati e che l'argomento utilizzato per il test del metodo sia corretto
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class); //catturiamo lo studente appena usato nella riga precedente (underTest.uploadStudent(studentTest);)

        //controllo che sia stato usato il metodo save e salvo il valore con cui è stato usato
        verify(studentRepositoryUnderTest).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(studentTest); //ci assicuriamo che lo studente creato nel test e quello catturato siano uguali
    }

    //Test metodo uploadStudent
    @Test
    void willThrowWhenEmailIsTaken() {
        //Given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        //forziamo che il metodo findStudentByEmail restituisca il valore che ci serve per far scattare l'errore
        given(studentRepositoryUnderTest.findStudentByEmail(studentTest.getEmail()))
                .willReturn(Optional.of(studentTest));

        //when
        //then
        assertThatThrownBy( () -> underTest.uploadStudent(studentTest)) //assicuriamo che scatti il throw quando chiamiamo il metodo uploadStudent
                .isInstanceOf(IllegalStateException.class) //classe dell'errore che si deve ottenere
                .hasMessageContaining("Email taken"); //scattato l'errore dobbiamo avere questo messaggio, cioè quello programmato

        //per concludere verifichiamo che il metodo save non venga mai lanciato perchè a causa del lancio del'exeption non dev'essere lanciato
        verify(studentRepositoryUnderTest, never()).save(any());
    }

    /**
     * test del servizio deleteStudent,
     * se il servizio va a buon fine e lo studente viene eliminato
     */
    @Test
    void TheStudentWillBeDeleted() {
        //Given
        Long studentId = any(long.class);

        //when
        //forziamo che il metodo studentRepository.existsById(studentId) restituisca true
        given(studentRepositoryUnderTest.existsById(studentId))
                .willReturn(true);

        underTest.deleteStudent(studentId);
        //then
        //se l'id esiste viene lanciato il metodo deleteById
        verify(studentRepositoryUnderTest).deleteById(studentId);
    }

    /**
     * test del servizio deleteStudent,
     * se il servizio non va a buon fine perchè l'id passato è di uno studente che non esiste
     */
    @Test
    void TheStudentPassedDoesNotExist() {
        //Given
        Long studentId = any(long.class);

        //when
        //forziamo che il metodo studentRepository.existsById(studentId) restituisca true
        given(studentRepositoryUnderTest.existsById(studentId))
                .willReturn(false);

       //Then
        assertThatThrownBy( () -> underTest.deleteStudent(studentId)) //assicuriamo che scatti il throw quando chiamiamo il metodo uploadStudent
                .isInstanceOf(IllegalStateException.class) //classe dell'errore che si deve ottenere
                .hasMessageContaining("Student with id "+ studentId +"doesn't exist"); //scattato l'errore dobbiamo avere questo messaggio, cioè quello programmato

        //per concludere verifichiamo che il metodo save non venga mai lanciato perchè a causa del lancio del'exeption non dev'essere lanciato
        verify(studentRepositoryUnderTest, never()).deleteById(any());
    }

    /**
     * test del servizio updateStudent,
     * se il servizio va a buon fine e modifica i dati di uno studente
     */
    @Test
    void WhenAnUpdateOfAStudentSucced() {
        //Given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        studentRepositoryUnderTest.save(studentTest);

        //When
        given(studentRepositoryUnderTest.findById(studentTest.getId()))
                .willReturn(Optional.of(studentTest));

        given(studentRepositoryUnderTest.findStudentByEmail(studentTest.getEmail()))
                .willReturn(Optional.empty());

        underTest.updateStudent(studentTest.getId(), studentTest.getName(), studentTest.getEmail());

        //Then
        //controlliamo che i servizi usati dentro il metodo siano stati usati e che l'argomento utilizzato per il test del metodo sia corretto
        ArgumentCaptor<Long> studentIdCaptor = ArgumentCaptor.forClass(long.class);
        ArgumentCaptor<String> studentEmailCaptor = ArgumentCaptor.forClass(String.class);

        //controllo che sia stato usato il metodo save e salvo il valore con cui è stato usato
        verify(studentRepositoryUnderTest).findById(studentIdCaptor.capture());
        verify(studentRepositoryUnderTest).findStudentByEmail(studentEmailCaptor.capture());

        assertThat(studentIdCaptor.getValue()).isEqualTo(studentTest.getId());
        assertThat(studentEmailCaptor.getValue()).isEqualTo(studentTest.getEmail());

    }

    /**
     * test del servizio updateStudent,
     * se il servizio non va a buon fine
     */
    @Test
    void WhenAnUpdateOfAStudentDoesNotSuccedBecauseStudentIdDoesNotExist() {
        //Given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        studentRepositoryUnderTest.save(studentTest);

        //When
        given(studentRepositoryUnderTest.findById(studentTest.getId()))
                .willReturn(Optional.of(studentTest));

        //Then
        assertThatThrownBy( () -> underTest.updateStudent(studentTest.getId(), null, studentTest.getEmail()))
                .isInstanceOf(IllegalStateException.class) //classe dell'errore che si deve ottenere
                .hasMessageContaining("Impossible use the new name passed"); //scattato l'errore dobbiamo avere questo messaggio, cioè quello programmato

        //per concludere verifichiamo che il metodo save non venga mai lanciato perchè a causa del lancio del'exeption non dev'essere lanciato
        verify(studentRepositoryUnderTest, never()).findStudentByEmail(any());
    }


    /**
     * test del servizio updateStudent,
     * se il servizio non va a buon fine
     */
    @Test
    void WhenUpdateAStudentDoesNotSuccedBecauseTheNewStudentEmailIsInvalid() {
        //Given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        studentRepositoryUnderTest.save(studentTest);

        //When
        given(studentRepositoryUnderTest.findById(studentTest.getId()))
                .willReturn(Optional.of(studentTest));

        given(studentRepositoryUnderTest.findStudentByEmail(studentTest.getEmail()))
                .willReturn(Optional.of(studentTest));

        //Then
        assertThatThrownBy( () -> underTest.updateStudent(studentTest.getId(), studentTest.getName(), studentTest.getEmail()))
                .isInstanceOf(IllegalStateException.class) //classe dell'errore che si deve ottenere
                .hasMessageContaining("Invalid Email"); //scattato l'errore dobbiamo avere questo messaggio, cioè quello programmato
    }

    /**
     * Test sul servizio getStudentName
     * public String getStudentName(Long studentId)
     */
    @Test
    void itShouldTakeStudentNameusingStudentId(){
        //Given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        studentRepositoryUnderTest.save(studentTest);
        Long studentId = studentTest.getId();
        String nameExpected = studentTest.getName();

        //When
        given(studentRepositoryUnderTest.findById(studentTest.getId()))
                .willReturn(Optional.of(studentTest));

        String nameExtracted = underTest.getStudentName(studentId);


        //Then
        assertThat(nameExtracted).isEqualTo(nameExpected);

        ArgumentCaptor<Long> studentIdCaptor = ArgumentCaptor.forClass(long.class);
        verify(studentRepositoryUnderTest).findById(studentIdCaptor.capture());
    }

    /**
     * test del servizio getStudentName,
     * se il servizio non va a buon fine
     */
    @Test
    void itShouldTakeStudentNameUsingStudentIdButIdDoesNotExist() {
        //Given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        studentRepositoryUnderTest.save(studentTest);
        Long studentId = studentTest.getId();

        //When
        given(studentRepositoryUnderTest.findById(studentTest.getId()))
                .willReturn(Optional.empty());

        //Then
        assertThatThrownBy( () -> underTest.getStudentName(studentId))
                .isInstanceOf(IllegalStateException.class) //classe dell'errore che si deve ottenere
                .hasMessageContaining("Student with id "+studentId+" doesn't exist"); //scattato l'errore dobbiamo avere questo messaggio, cioè quello programmato
    }


    /**
     * Test sul servizio getStudentSurname
     * public String getStudentSurname(Long studentId)
     */
    @Test
    void itShouldTakeStudentSurnameUsingStudentId(){
        //Given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        studentRepositoryUnderTest.save(studentTest);
        Long studentId = studentTest.getId();
        String surnameExpected = studentTest.getSurname();

        //When
        given(studentRepositoryUnderTest.findById(studentTest.getId()))
                .willReturn(Optional.of(studentTest));

        String surnameExtracted = underTest.getStudentSurname(studentId);


        //Then
        assertThat(surnameExpected).isEqualTo(surnameExtracted);

        ArgumentCaptor<Long> studentIdCaptor = ArgumentCaptor.forClass(long.class);
        verify(studentRepositoryUnderTest).findById(studentIdCaptor.capture());
    }

    /**
     * test del servizio getStudentSurname,
     * se il servizio non va a buon fine
     */
    @Test
    void itShouldTakeStudentSurnameUsingStudentIdButStudentIdDoesNotExist() {
        //Given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        studentRepositoryUnderTest.save(studentTest);
        Long studentId = studentTest.getId();

        //When
        given(studentRepositoryUnderTest.findById(studentTest.getId()))
                .willReturn(Optional.empty());

        //Then
        assertThatThrownBy( () -> underTest.getStudentSurname(studentId))
                .isInstanceOf(IllegalStateException.class) //classe dell'errore che si deve ottenere
                .hasMessageContaining("Student with id "+studentId+" doesn't exist"); //scattato l'errore dobbiamo avere questo messaggio, cioè quello programmato
    }

    /**
     * Test sul servizio getStudentEmail
     * public String getStudentSurname(Long studentId)
     */
    @Test
    void itShouldTakeStudentEmailUsingStudentId(){
        //Given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        studentRepositoryUnderTest.save(studentTest);
        Long studentId = studentTest.getId();
        String emailExpected = studentTest.getEmail();

        //When
        given(studentRepositoryUnderTest.findById(studentTest.getId()))
                .willReturn(Optional.of(studentTest));

        String emailExtracted = underTest.getStudentEmail(studentId);


        //Then
        assertThat(emailExpected).isEqualTo(emailExtracted);

        ArgumentCaptor<Long> studentIdCaptor = ArgumentCaptor.forClass(long.class);
        verify(studentRepositoryUnderTest).findById(studentIdCaptor.capture());
    }

    /**
     * test del servizio getStudentName,
     * se il servizio non va a buon fine
     */
    @Test
    void itShouldTakeStudentEmailUsingStudentIdButStudentIdDoesNotExist() {
        //Given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        studentRepositoryUnderTest.save(studentTest);
        Long studentId = studentTest.getId();

        //When
        given(studentRepositoryUnderTest.findById(studentTest.getId()))
                .willReturn(Optional.empty());

        //Then
        assertThatThrownBy( () -> underTest.getStudentEmail(studentId))
                .isInstanceOf(IllegalStateException.class) //classe dell'errore che si deve ottenere
                .hasMessageContaining("Student with id "+studentId+" doesn't exist"); //scattato l'errore dobbiamo avere questo messaggio, cioè quello programmato
    }
}