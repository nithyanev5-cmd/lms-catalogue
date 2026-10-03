CREATE TABLE course (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    code        VARCHAR(32)  NOT NULL,
    title       VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    level       VARCHAR(20)  NOT NULL,
    credits     INT          NOT NULL,
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_course PRIMARY KEY (id),
    CONSTRAINT uq_course_code UNIQUE (code)
);

CREATE INDEX ix_course_title ON course (title);
CREATE INDEX ix_course_level_active ON course (level, active);
