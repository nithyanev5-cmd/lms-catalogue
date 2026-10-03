import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { fetchCourseByCode } from "../api/catalog.ts";
import type { CourseDetail as CourseDetailType } from "../types.ts";

export default function CourseDetail() {
  const { code } = useParams();
  const [status, setStatus] = useState<"loading" | "ready" | "error">(
    "loading",
  );
  const [error, setError] = useState("");
  const [course, setCourse] = useState<CourseDetailType | null>(null);

  useEffect(() => {
    if (!code) return;
    let cancelled = false;
    setStatus("loading");

    fetchCourseByCode(code)
      .then((data) => {
        if (!cancelled) {
          setCourse(data);
          setStatus("ready");
        }
      })
      .catch((err: unknown) => {
        if (!cancelled) {
          setError(
            err instanceof Error ? err.message : "Failed to load course",
          );
          setStatus("error");
        }
      });

    return () => {
      cancelled = true;
    };
  }, [code]);

  if (status === "loading") {
    return <p className="state">Loading course…</p>;
  }

  if (status === "error") {
    return <p className="state state--error">{error}</p>;
  }

  if (!course) {
    return <p className="state">Course not found.</p>;
  }

  return (
    <main className="page page--detail">
      <header className="page__head">
        <p className="eyebrow">Global Learning</p>
        <h1>{course.title}</h1>
        <p className="course-meta">
          {course.code} • {course.credits} credits • {course.level}
        </p>
      </header>

      <section className="course-detail">
        <p className="course-detail__description">
          {course.description ?? "No description available."}
        </p>

        <dl className="course-detail__grid">
          <div>
            <dt>Faculty</dt>
            <dd>{course.faculty}</dd>
          </div>
          <div>
            <dt>Capacity</dt>
            <dd>{course.capacity}</dd>
          </div>
          <div>
            <dt>Average rating</dt>
            <dd>
              {course.averageRating === null
                ? "—"
                : `${course.averageRating.toFixed(1)} (${course.reviewCount} reviews)`}
            </dd>
          </div>
        </dl>

        <p>
          <Link to="/">← Back to catalogue</Link>
        </p>
      </section>
    </main>
  );
}
