import type { CourseLevel } from '../constants.ts';
import type { CourseDetail, CourseSummary } from '../types.ts';

const BASE_URL = '/api/courses';

/**
 * `level` is `CourseLevel | null` because App represents "no filter" as null.
 * `keyword` is optional because nothing passes it yet - the backend already
 * accepts it as ?q=.
 */
export interface FetchCoursesParams {
  level?: CourseLevel | null;
  keyword?: string;
}

export async function fetchCourses(
  { level, keyword }: FetchCoursesParams = {}
): Promise<CourseSummary[]> {
  const params = new URLSearchParams();
  if (level) {
    params.set('level', level);
  }
  if (keyword) {
    params.set('q', keyword);
  }

  const query = params.toString();
  const response = await fetch(query ? `${BASE_URL}?${query}` : BASE_URL);

  if (!response.ok) {
    throw new Error(`Catalogue request failed with status ${response.status}`);
  }
  // response.json() is typed Promise<any>, so this line needs no cast. The
  // Promise<CourseSummary[]> annotation above is a CLAIM about what the server
  // sent, not a check: a renamed backend field would surface as `undefined` in a
  // card, not as an error.
  return response.json();
}

export async function fetchCourseByCode(code: string): Promise<CourseDetail> {
  const response = await fetch(`${BASE_URL}/${encodeURIComponent(code)}`);
  if (!response.ok) {
    // The error body is deliberately discarded here; see ApiErrorBody in
    // src/types.ts for the shape being thrown away.
    throw new Error(`Course ${code} could not be loaded`);
  }
  return response.json();
}
