package com.example.demo;

import com.example.demo.entity.Course;
import com.example.demo.entity.Take;
import com.example.demo.repository.CourseRepository;
import com.example.demo.entity.Instructor;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TakeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration //file di configurazione del nostro repository
public class UniversityConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository,
                                        InstructorRepository instructorRepository,
                                        CourseRepository courseRepository,
                                        TakeRepository takeRepository) {
        return args -> {

            Student giovanna = new Student("Giovanna", "D'Arco", "giovannadarco@gmail.com", LocalDate.of(2000, Month.JANUARY, 5));
            Student giovanni = new Student("Giovanni", "Paolo", "giovannipaolo@gmail.com", LocalDate.of(2000, Month.JANUARY, 5));
            Student marylin = new Student("Marylin", "Monroe", "marylinmonroe@gmail.com", LocalDate.of(2000, Month.JANUARY, 5));
            Student brigitte = new Student("Brigitte", "Bardot", "brigittebardot@gmail.com", LocalDate.of(2000, Month.JANUARY, 5));
            Student piersilvio = new Student("Piersilvio", "Berlusconi", "piersilvioberlusconi@gmail.com", LocalDate.of(2000, Month.JANUARY, 5));
            Student vladimir = new Student("Vladimi", "Putin", "vladimirputin@gmail.com", LocalDate.of(2000, Month.JANUARY, 5));
            Student draghi = new Student("Mario", "Draghi", "mariodraghi@gmail.com", LocalDate.of(2000, Month.JANUARY, 5));
            Student luciano = new Student("Luciano", "Spalletti", "lucianospalletti@gmail.com", LocalDate.of(2000, Month.JANUARY, 5));

            Instructor walter = new Instructor("Walter","White","Chemistry");
            Instructor lavrov = new Instructor("Sergej ","Lavrov ","Foreign Policy");
            Instructor eliot = new Instructor("Eliot","Alderson","Computer Science");
            Instructor massimo = new Instructor("Massimo","Caccciari","Art");

            Course geography = new Course("Geography", "Basic knowledge about geography",lavrov);
            Course art = new Course("Art", "Basic knowledge about art",massimo);
            Course physics = new Course( "Physics", "Basic knowledge about physics",walter);
            Course computerScience = new Course("Computer Science", "Basic knowledge about computer science",eliot);
            Course appliedPhysics = new Course( "Applied Physics", "Basic knowledge about Applied Physics",walter);

            Take take1 = new Take(giovanna, physics);
            Take take2 = new Take(giovanna, geography);
            Take take3 = new Take(giovanna, art);
            Take take4 = new Take(giovanna, computerScience);

            Take take5 = new Take(marylin, physics);
            Take take6 = new Take(marylin, geography);
            Take take7 = new Take(marylin, art);
            Take take8 = new Take(marylin, computerScience);

            Take take9 = new Take(giovanni, physics);
            Take take10 = new Take(giovanna, computerScience);

            Take take11 = new Take(brigitte, physics);
            Take take12 = new Take(brigitte, computerScience);

            Take take13 = new Take(piersilvio, art);
            Take take14 = new Take(piersilvio, geography);

            Take take15 = new Take(vladimir, art);
            Take take16 = new Take(vladimir, geography);

            Take take17 = new Take(draghi,geography);


            studentRepository.saveAll(
                    List.of(giovanna, giovanni, marylin, brigitte, piersilvio, vladimir,draghi,luciano)
            );

            instructorRepository.saveAll(
                    List.of(walter, lavrov, eliot, massimo)
            );

            courseRepository.saveAll(
                    List.of(geography, art, physics, computerScience, appliedPhysics)
            );

            takeRepository.saveAll(
                    List.of(take1,take2,take3,take4,take5,take6,take7,take8,take9,
                            take10,take11,take12,take13,take14,take15,take16,take17)
            );

        };
    }


}
