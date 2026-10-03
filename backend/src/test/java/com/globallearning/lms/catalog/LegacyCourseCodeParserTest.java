package com.globallearning.lms.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.service.LegacyCourseCodeParser;
import org.junit.jupiter.api.Test;

class LegacyCourseCodeParserTest {

    private final LegacyCourseCodeParser parser = new LegacyCourseCodeParser();

    @Test
    void mapsLowBandsToFoundation() {
        assertThat(parser.levelOf("CS1007")).isEqualTo(CourseLevel.FOUNDATION);
    }

    @Test
    void mapsMidBandsToIntermediate() {
        assertThat(parser.levelOf("ENGG2104B")).isEqualTo(CourseLevel.INTERMEDIATE);
    }

    @Test
    void mapsHighBandsToAdvanced() {
        assertThat(parser.levelOf("MATH3220A")).isEqualTo(CourseLevel.ADVANCED);
    }

    @Test
    void extractsFacultyPrefix() {
        assertThat(parser.facultyOf("ENGG2104B")).isEqualTo("ENGG");
        assertThat(parser.facultyOf("")).isEqualTo("UNKNOWN");
    }
}
