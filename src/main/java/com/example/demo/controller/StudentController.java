package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//all'interno del controller metteremo tutte le risorse per le nostre API
@RestController
@RequestMapping("api/v1/students") //path in cui trovare le API
public class StudentController {

    private final StudentService studentService; //riferimento per il livello service

    //costruttore dello student service
    @Autowired
    public StudentController(StudentService universityService) { //costruttore riferimento livello service
        this.studentService = universityService;
    }


    // ottieni la lista degli studenti presenti sul DB
    @GetMapping(path="/getAllStudents")
    public ResponseEntity<?> getAllStudents(){
        try {
            return new ResponseEntity<List<Student>>(
                    this.studentService.getAllStudents(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //GET usata quando si vogliono prendere risorse al DB
    //informazioni di uno studente, tramite il suo id
    @GetMapping(path = "getStudent/{studentId}")
    public ResponseEntity<?> getStudent(@PathVariable("studentId") Long studentId){
        try {
            return new ResponseEntity<Student>(
                    this.studentService.getStudent(studentId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //POST usata quando si vogliono aggiungere risorse al nostro DB
    @PostMapping(path = "uploadStudent")
    public ResponseEntity<?> uploadStudent(@RequestBody Student student){ //RequestBody serve per prendere la richiesta nel body della chiamata
        try {
            studentService.uploadStudent(student);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    // Elimina uno studente dal DB
    @DeleteMapping(path = "deleteStudent/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable("studentId") Long studentId){
        try {
            studentService.deleteStudent(studentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    // PUT usata per aggiornare i dati disponibili nel DB
    @PutMapping(path = "updateStudent/{studentId}") // PUT usata per aggiornare i dati disponibili nel DB
    public ResponseEntity<?> updateStudent(@PathVariable ("studentId") Long studentId,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) String email){

        try {
            studentService.updateStudent(studentId, name, email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping(path = "getStudentName/{studentId}")
    public ResponseEntity<?> getStudentName(@PathVariable("studentId") Long studentId){
        try {
            return new ResponseEntity<String>(
                    this.studentService.getStudentName(studentId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(path = "getStudentSurname/{studentId}")
    public ResponseEntity<?> getStudentSurname(@PathVariable("studentId") Long studentId){
        try {
            return new ResponseEntity<String>(
                    this.studentService.getStudentSurname(studentId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(path = "getStudentEmail/{studentId}")
    public ResponseEntity<?> getStudentEmail(@PathVariable("studentId") Long studentId){
        try {
            return new ResponseEntity<String>(
                    this.studentService.getStudentEmail(studentId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
