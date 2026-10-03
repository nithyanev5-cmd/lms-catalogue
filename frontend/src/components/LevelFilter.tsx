import { LEVEL_FILTERS, formatLevel, type CourseLevel } from "../constants.ts";

interface LevelFilterProps {
  /** null means "All levels". */
  selected: CourseLevel | null;
  /**
   * A plain function type rather than React's Dispatch<SetStateAction<...>>, so
   * this component does not need to know it is talking to a useState setter.
   *
   * App can still pass `setLevel` straight through: parameter types are checked
   * contravariantly, and the setter's parameter accepts CourseLevel | null. The
   * narrower type is also safer - it forbids passing a function, which React
   * would treat as an updater.
   */
  onSelect: (level: CourseLevel | null) => void;
}

export default function LevelFilter({ selected, onSelect }: LevelFilterProps) {
  return (
    <div className="level-filter">
      <label className="visually-hidden" htmlFor="level-select">
        Filter by level
      </label>
      <select
        id="level-select"
        className="level-select"
        value={selected === null ? "all" : selected}
        onChange={(e) =>
          onSelect(
            e.target.value === "all" ? null : (e.target.value as CourseLevel),
          )
        }
        aria-label="Filter courses by level"
      >
        <option value="all">All levels</option>
        {LEVEL_FILTERS.map((level) => (
          <option key={level} value={level}>
            {formatLevel(level)}
          </option>
        ))}
      </select>
    </div>
  );
}
