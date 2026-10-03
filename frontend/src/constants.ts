// The level values the API sends: the names of the Java CourseLevel enum.
//
// `as const` freezes this into a readonly tuple of string literals. Without it
// the array would be plain string[] and CourseLevel below would widen to string.
export const LEVEL_FILTERS = ["FOUNDATION", "INTERMEDIATE", "ADVANCED"] as const;

/** "FOUNDATION" | "INTERMEDIATE" | "ADVANCED", derived from the array above. */
export type CourseLevel = (typeof LEVEL_FILTERS)[number];

// Display labels for course levels. Kept as an explicit map rather than a
// derived string so the wording can be tuned per region.
//
// Typing it Record<CourseLevel, string> makes the map exhaustive: adding a value
// to LEVEL_FILTERS is a compile error here until a label is added too.
export const LEVEL_LABELS: Record<CourseLevel, string> = {
  FOUNDATION: "Foundation",
  INTERMEDIATE: "Intermediate",
  ADVANCED: "Advanced",
};

/**
 * Takes `string`, not `CourseLevel`, on purpose.
 *
 * LevelFilter passes a value out of LEVEL_FILTERS, which really is a CourseLevel.
 * CourseCard passes `course.level`, which came from an unvalidated response.json()
 * and is only CLAIMED to be one. The `?? level` fallback is what stops an
 * unrecognised value rendering as "undefined", so the parameter type stays wide
 * enough for that fallback to be reachable in practice.
 */
export function formatLevel(level: string): string {
  return LEVEL_LABELS[level as CourseLevel] ?? level;
}

export function formatCredits(credits: number): string {
  if (credits === 1) {
    return "1 credit";
  }
  return `${credits} credits`;
}
