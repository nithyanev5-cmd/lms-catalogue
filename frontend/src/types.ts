import type { CourseLevel } from './constants.ts';

/**
 * Shapes returned by the Spring Boot catalogue API.
 *
 * These are claims, not guarantees: nothing validates the JSON at runtime, so a
 * backend change surfaces as `undefined` in the UI rather than a compile error.
 *
 * Java `Long` and `Integer` both arrive as JSON numbers. Nullable Java fields are
 * `T | null` rather than `T?` because Jackson's Spring Boot default includes nulls
 * — the key is present with the value null, not absent.
 */

/** GET /api/courses?level=&q=  ->  CourseSummaryResponse[]. Five fields, no rating. */
export interface CourseSummary {
  id: number;
  code: string;
  title: string;
  level: CourseLevel;
  credits: number;
}

/**
 * GET /api/courses/{code}  ->  CourseResponse. Fourteen fields.
 *
 * Written out in full rather than `extends CourseSummary` on purpose. These are two
 * separate hand-mapped Java classes that can drift independently, and structural
 * typing already lets a CourseDetail be passed wherever a CourseSummary is expected
 * — so `extends` would add no capability, only a promise the API does not make.
 */
export interface CourseDetail {
  id: number;
  code: string;
  title: string;
  description: string | null;
  level: CourseLevel;
  credits: number;
  /** Java primitive `boolean`, so never null. */
  active: boolean;
  /** Derived by LegacyCourseCodeParser, which returns "UNKNOWN" rather than null. */
  faculty: string;
  capacity: number;
  instructorEmail: string | null;
  /** Java Instant, serialised as an ISO-8601 string. */
  archivedAt: string | null;
  archiveReason: string | null;
  /** null when the course has no reviews yet - NOT 0. */
  averageRating: number | null;
  reviewCount: number;
}

/** GET /api/courses/{code}/reviews  ->  ReviewResponse[]. */
export interface Review {
  id: number;
  courseCode: string;
  learnerRef: string;
  rating: number;
  comment: string | null;
  /** Java Instant, serialised as an ISO-8601 string. */
  createdAt: string;
  updatedAt: string | null;
}

/**
 * Error bodies from CatalogExceptionHandler and EnrollmentExceptionHandler.
 *
 * `message` is optional because four codes omit the key entirely - DUPLICATE_REVIEW,
 * DUPLICATE_ENROLLMENT, SEAT_UNAVAILABLE and COURSE_NOT_OPEN, whose handler methods
 * do not even accept the exception. VALIDATION_FAILED also omits it, carrying
 * `fields` instead.
 *
 * This covers the HANDLED errors only. An unhandled failure - ?level=BOGUS, which
 * fails Spring's enum binding - falls through to Spring's default response, whose
 * `error` is a reason phrase like "Bad Request" rather than a code.
 */
export interface ApiErrorBody {
  error: string;
  message?: string;
  fields?: Record<string, string>;
}

/** The three states the catalogue screen can be in. */
export type CatalogStatus = 'loading' | 'ready' | 'error';
