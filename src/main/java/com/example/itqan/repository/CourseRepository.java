package com.example.itqan.repository;

import com.example.itqan.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByTeacherId(int teacherId);

    /**
     * get the courses in one call by teacher id , and not go nested
     * @param teacherId
     * @return
     */
    @Query("SELECT DISTINCT c FROM Course c " +
            "LEFT JOIN FETCH c.students " +
            "JOIN FETCH c.teacher " +
            "WHERE c.teacher.id = :teacherId")
    List<Course> findByTeacherIdWithStudentsAndTeacher(@Param("teacherId") int teacherId);

}
