package com.globallearning.lms.catalog.service;

import com.globallearning.lms.catalog.domain.CourseLevel;
import org.springframework.stereotype.Component;

/**
 * Parses legacy course codes carried over from the 2014 catalogue.
 *
 * Format: [FACULTY 2-4 alpha][LEVEL digit][SEQUENCE 3 digit][optional revision letter]
 * e.g. "ENGG2104B", "CS1007", "MATH3220A"
 */
@Component
public class LegacyCourseCodeParser {

    public CourseLevel levelOf(String code) {
        if (code == null || code.length() < 5) {
            return CourseLevel.FOUNDATION;
        }
        int i = 0;
        while (i < code.length() && Character.isLetter(code.charAt(i))) {
            i++;
        }
        if (i == 0 || i > 4 || i >= code.length()) {
            return CourseLevel.FOUNDATION;
        }
        char levelDigit = code.charAt(i);
        if (!Character.isDigit(levelDigit)) {
            return CourseLevel.FOUNDATION;
        }
        int band = Character.getNumericValue(levelDigit);
        if (band <= 1) {
            return CourseLevel.FOUNDATION;
        }
        if (band <= 2) {
            return CourseLevel.INTERMEDIATE;
        }
        return CourseLevel.ADVANCED;
    }

    public String facultyOf(String code) {
        if (code == null) {
            return "UNKNOWN";
        }
        StringBuilder faculty = new StringBuilder();
        for (char c : code.toCharArray()) {
            if (!Character.isLetter(c)) {
                break;
            }
            faculty.append(Character.toUpperCase(c));
        }
        return faculty.length() == 0 ? "UNKNOWN" : faculty.toString();
    }

    public boolean isRevised(String code) {
        return code != null && !code.isEmpty() && Character.isLetter(code.charAt(code.length() - 1))
            && code.length() > 5;
    }
}
