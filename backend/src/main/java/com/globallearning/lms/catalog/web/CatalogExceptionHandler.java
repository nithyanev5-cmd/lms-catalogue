package com.globallearning.lms.catalog.web;

import com.globallearning.lms.catalog.service.CourseNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CatalogExceptionHandler {

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(CourseNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "COURSE_NOT_FOUND");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "VALIDATION_FAILED");
        Map<String, String> fields = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(fe -> fields.put(fe.getField(), fe.getDefaultMessage()));
        body.put("fields", fields);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(com.globallearning.lms.catalog.service.CourseAlreadyArchivedException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyArchived(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "COURSE_ALREADY_ARCHIVED");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(com.globallearning.lms.catalog.service.CourseAlreadyActiveException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyActive(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "COURSE_ALREADY_ACTIVE");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(com.globallearning.lms.catalog.service.CourseArchivedException.class)
    public ResponseEntity<Map<String, Object>> handleArchived(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "COURSE_ARCHIVED");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(com.globallearning.lms.catalog.service.DuplicatePrerequisiteException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicatePrerequisite(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "DUPLICATE_PREREQUISITE");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(com.globallearning.lms.catalog.service.PrerequisiteNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePrerequisiteNotFound(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "PREREQUISITE_NOT_FOUND");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(com.globallearning.lms.catalog.service.CyclicPrerequisiteException.class)
    public ResponseEntity<Map<String, Object>> handleCyclic(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "CYCLIC_DEPENDENCY");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }
}
