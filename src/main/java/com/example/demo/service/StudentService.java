package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    private final CourseRepository courseRepository;

    @Autowired
    private final TakeRepository takeRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository, TakeRepository takeRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.takeRepository = takeRepository;
    }


    public Student getStudent(Long studentId) {

        Optional<Student> student = studentRepository.findById(studentId);

        if( !studentRepository.findById(studentId).isPresent() ){
            throw new IllegalStateException("The student with id "+studentId+ " doesn't exist");
        }
        return student.get();
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public void uploadStudent(Student student) {

        if( studentRepository.findStudentByEmail(student.getEmail()).isPresent() ){
            throw new IllegalStateException("Email taken");
        }
        studentRepository.save(student);

    }

    public void deleteStudent(Long studentId) {
        boolean exist = studentRepository.existsById(studentId);
        if(!exist){
            throw new IllegalStateException("Student with id "+ studentId +"doesn't exist");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional //questa annotazione ci permette di non usare JPA
    public void updateStudent(Long studentId, String name, String email) {

        Student student = studentRepository.findById(studentId).
                orElseThrow( () -> new IllegalStateException("Student with id "+studentId+" doesn't exist") );

        if( name == null || name.length() <= 0){
            throw new IllegalStateException("Impossible use the new name passed");
        }
        student.setName(name);


        if( email != null && email.length() > 0){

            Optional<Student> studentEmail = studentRepository.findStudentByEmail(email);

            if( studentEmail.isPresent()) {
                throw new IllegalStateException("Invalid Email");
            }
            student.setEmail(email);
        }
    }


    public String getStudentName(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow( ()-> new IllegalStateException("Student with id "+studentId+" doesn't exist") );
        return student.getName();
    }

    public String getStudentSurname(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow( ()-> new IllegalStateException("Student with id "+studentId+" doesn't exist") );
        return student.getSurname();
    }

    public String getStudentEmail(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow( ()-> new IllegalStateException("Student with id "+studentId+" doesn't exist") );
        return student.getEmail();
    }
}
