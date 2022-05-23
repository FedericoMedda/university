package com.example.demo.controller;

import com.example.demo.entity.Student;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1L,"Giovanna","D'Arco","giovannadarco@gmail.com", LocalDate.of(2000, Month.JANUARY,5)),
            new Student(2L,"Giovann","Paolo","giovannipaolo@gmail.com",LocalDate.of(2000, Month.FEBRUARY,5)),
            new Student(3L, "Marylin"," Monroe","marylinmonroe@gmail.com",LocalDate.of(2000, Month.MARCH,5)),
            new Student(4L, "Brigitte","Bardot","brigittebardot@gmail.com",LocalDate.of(2000, Month.APRIL,5))
    );

    @GetMapping
    public static List<Student> getAllStudent() {
        return STUDENTS;
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student){

    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Integer studentId){

    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student){

    }
}
