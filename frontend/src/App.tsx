import { useEffect, useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import CourseList from "./components/CourseList.tsx";
import CourseDetail from "./components/CourseDetail.tsx";
import LevelFilter from "./components/LevelFilter.tsx";
import { fetchCourses } from "./api/catalog.ts";
import type { CourseLevel } from "./constants.ts";
import type { CatalogStatus, CourseSummary } from "./types.ts";

export default function App() {
  // useState([]) on its own infers never[], so setCourses(data) would fail.
  const [courses, setCourses] = useState<CourseSummary[]>([]);
  // useState(null) on its own infers the type `null`. null means "no filter".
  const [level, setLevel] = useState<CourseLevel | null>(null);
  // useState('loading') would widen to `string`, which CourseList's status prop
  // rejects. The annotation is what makes the three states a closed set.
  const [status, setStatus] = useState<CatalogStatus>("loading");
  // No annotation needed: `string` is already exactly right.
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;
    setStatus("loading");

    fetchCourses({ level })
      .then((data) => {
        if (!cancelled) {
          setCourses(data);
          setStatus("ready");
        }
      })
      .catch((err: unknown) => {
        if (!cancelled) {
          // Promise.catch types its callback parameter as `any`, so `err.message`
          // would compile even under full strict. Annotating `unknown` opts in to
          // the check that useUnknownInCatchVariables gives a try/catch binding:
          // a rejected promise can carry anything, not just an Error.
          setError(
            err instanceof Error
              ? err.message
              : "The catalogue request failed.",
          );
          setStatus("error");
        }
      });

    return () => {
      cancelled = true;
    };
  }, [level]);

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            <main className="page">
              <header className="page__head">
                <p className="eyebrow">Global Learning</p>
                <h1>Course catalogue</h1>
              </header>
              <LevelFilter selected={level} onSelect={setLevel} />
              <CourseList courses={courses} status={status} error={error} />
            </main>
          }
        />
        <Route path="/courses/:code" element={<CourseDetail />} />
      </Routes>
    </BrowserRouter>
  );
}
