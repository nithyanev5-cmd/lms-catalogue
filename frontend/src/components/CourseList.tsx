import CourseCard from './CourseCard.tsx';
import type { CatalogStatus, CourseSummary } from '../types.ts';

interface CourseListProps {
  courses: CourseSummary[];
  status: CatalogStatus;
  error: string;
}

export default function CourseList({ courses, status, error }: CourseListProps) {
  if (status === 'loading') {
    return <p className="state">Loading the catalogue…</p>;
  }

  if (status === 'error') {
    return <p className="state state--error">{error} Check that the API is running on port 8080.</p>;
  }

  if (courses.length === 0) {
    return <p className="state">No courses match this filter. Clear the filter to see the full catalogue.</p>;
  }

  return (
    <ul className="course-list">
      {courses.map((course) => (
        <CourseCard key={course.id} course={course} />
      ))}
    </ul>
  );
}
