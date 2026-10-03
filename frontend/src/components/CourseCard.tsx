import { formatLevel } from "../constants.ts";
import { Link } from "react-router-dom";
import type { CourseSummary } from "../types.ts";

// CourseSummary, not CourseDetail: this card is rendered from the list endpoint,
// which returns only five fields.
interface CourseCardProps {
  course: CourseSummary;
}

export default function CourseCard({ course }: CourseCardProps) {
  return (
    <li className="course-card">
      <Link
        to={`/courses/${encodeURIComponent(course.code)}`}
        className="course-card__link"
      >
        <div className="course-card__head">
          <span className="course-card__code">{course.code}</span>
          <span className="course-card__level">
            {formatLevel(course.level)}
          </span>
        </div>
        <h2 className="course-card__title">{course.title}</h2>
        <p className="course-card__credits">{course.credits} credits</p>
      </Link>
    </li>
  );
}
