package com.example.itqan.repository;

import com.example.itqan.model.CourseTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CourseTimeRepository extends JpaRepository<CourseTime, Long> {

    @Query("SELECT ct FROM CourseTime ct " +
            "WHERE ct.nextLessonDate >= :now AND ct.course.teacher.id = :teacherId " +
            "ORDER BY ct.nextLessonDate ASC")
    List<CourseTime> findNextLessonsByTeacher(Long teacherId, LocalDateTime now, Pageable pageable);


}

