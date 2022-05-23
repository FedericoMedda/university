package com.example.demo.service;

import com.example.demo.entity.Instructor;
import com.example.demo.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {

    @Autowired
    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }


    public List<Instructor> getInstructors() {
        return instructorRepository.findAll();
    }

    public Instructor getInstructor(Long instructorId) {

        Optional<Instructor> instructor = instructorRepository.findById(instructorId);

        if( !instructor.isPresent() ){
            throw new IllegalStateException("The instructor with id "+instructorId+ "doesn't exist");
        }
        return instructor.get();
    }

    public void deleteInstructor(Long instructorId) {
        if( !instructorRepository.findById(instructorId).isPresent() ){
            throw new IllegalStateException("The instructor with id "+instructorId+ "doesn't exist");
        }
        instructorRepository.deleteById(instructorId);
    }
}
