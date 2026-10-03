package com.globallearning.lms.catalog.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.globallearning.lms.catalog.audit.CatalogAuditLogger;
import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.repository.CoursePrerequisiteRepository;
import com.globallearning.lms.catalog.repository.CourseRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CoursePrerequisiteRepository prerequisiteRepository;

    @Mock
    private CatalogAuditLogger auditLogger;

    @InjectMocks
    private CourseService courseService;

    private Course courseA;
    private Course courseB;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        courseA = new Course("CS1007", "Intro CS", "desc", CourseLevel.FOUNDATION, 3, true);
        // set ids via reflection-like approach (not ideal but sufficient for unit tests)
        try {
            java.lang.reflect.Field idField = Course.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(courseA, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        courseB = new Course("CS3401", "Advanced", "desc", CourseLevel.ADVANCED, 3, true);
        try {
            java.lang.reflect.Field idField = Course.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(courseB, 2L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addPrerequisite_success() {
        when(courseRepository.findByCodeIgnoreCase("CS3401")).thenReturn(Optional.of(courseB));
        when(courseRepository.findByCodeIgnoreCase("CS1007")).thenReturn(Optional.of(courseA));
        when(prerequisiteRepository.countByCourseIdAndPrerequisiteCourseId(2L, 1L)).thenReturn(0);
        when(prerequisiteRepository.insertPrerequisite(2L, 1L)).thenReturn(1);

        Course result = courseService.addPrerequisite("CS3401", "CS1007");
        // returned course is the prerequisite
        org.junit.jupiter.api.Assertions.assertEquals("CS1007", result.getCode());
        verify(prerequisiteRepository).insertPrerequisite(2L, 1L);
    }

    @Test
    public void addPrerequisite_duplicate_throws() {
        when(courseRepository.findByCodeIgnoreCase("CS3401")).thenReturn(Optional.of(courseB));
        when(courseRepository.findByCodeIgnoreCase("CS1007")).thenReturn(Optional.of(courseA));
        when(prerequisiteRepository.countByCourseIdAndPrerequisiteCourseId(2L, 1L)).thenReturn(1);

        assertThrows(DuplicatePrerequisiteException.class, () ->
            courseService.addPrerequisite("CS3401", "CS1007")
        );
    }

    @Test
    public void addPrerequisite_self_throws() {
        when(courseRepository.findByCodeIgnoreCase("CS1007")).thenReturn(Optional.of(courseA));

        assertThrows(CyclicPrerequisiteException.class, () ->
            courseService.addPrerequisite("CS1007", "CS1007")
        );
    }

    @Test
    public void addPrerequisite_prereq_missing_throws() {
        when(courseRepository.findByCodeIgnoreCase("CS3401")).thenReturn(Optional.of(courseB));
        when(courseRepository.findByCodeIgnoreCase("MISSING")).thenReturn(Optional.empty());

        assertThrows(PrerequisiteNotFoundException.class, () ->
            courseService.addPrerequisite("CS3401", "MISSING")
        );
    }

    @Test
    public void removePrerequisite_not_found_throws() {
        when(courseRepository.findByCodeIgnoreCase("CS3401")).thenReturn(Optional.of(courseB));
        when(courseRepository.findByCodeIgnoreCase("CS1007")).thenReturn(Optional.of(courseA));
        when(prerequisiteRepository.deleteByCourseIdAndPrerequisiteCourseId(2L, 1L)).thenReturn(0);

        assertThrows(PrerequisiteNotFoundException.class, () ->
            courseService.removePrerequisite("CS3401", "CS1007")
        );
    }
}
