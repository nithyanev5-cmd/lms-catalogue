package com.globallearning.lms.catalog.service;

import com.globallearning.lms.catalog.domain.TestCourse;
import com.globallearning.lms.catalog.repository.TestCourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TestCourseService {
    private final TestCourseRepository testCourseRepository;

    public TestCourseService(TestCourseRepository testCourseRepository) {
        this.testCourseRepository = testCourseRepository;
    }

    public TestCourse createTestCourse(TestCourse testCourse) {
        testCourse.setIsActive(true);
        return testCourseRepository.save(testCourse);
    }

    @Transactional(readOnly = true)
    public List<TestCourse> getAllActiveTestCourses() {
        return testCourseRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public Optional<TestCourse> getTestCourseById(Long id) {
        return testCourseRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<TestCourse> getTestCoursesByDifficultyLevel(String difficultyLevel) {
        return testCourseRepository.findByDifficultyLevel(difficultyLevel);
    }

    public TestCourse updateTestCourse(Long id, TestCourse updatedCourse) {
        return testCourseRepository.findById(id).map(course -> {
            course.setName(updatedCourse.getName());
            course.setDescription(updatedCourse.getDescription());
            course.setDurationHours(updatedCourse.getDurationHours());
            course.setDifficultyLevel(updatedCourse.getDifficultyLevel());
            course.setMaxAttempts(updatedCourse.getMaxAttempts());
            course.setPassingScore(updatedCourse.getPassingScore());
            return testCourseRepository.save(course);
        }).orElseThrow(() -> new TestCourseNotFoundException(id));
    }

    public void deleteTestCourse(Long id) {
        TestCourse course = testCourseRepository.findById(id)
                .orElseThrow(() -> new TestCourseNotFoundException(id));
        course.setIsActive(false);
        testCourseRepository.save(course);
    }
}
