import { Fragment } from 'react'

/**
 * Renders duplicate-finder results: each row has one primary item and N match rows (different style).
 */
export function DuplicateResultsView({ rows = [] }) {
  if (!rows.length) {
    return (
      <p className="duplicate-results-empty">No duplicate rows. Try a different query or trove selection.</p>
    )
  }
  return (
    <div className="duplicate-results">
      <table className="duplicate-results-table">
        <thead>
          <tr>
            <th className="col-title">Title</th>
            <th className="col-trove">Trove</th>
            <th className="col-score">Score</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((row, rowIdx) => (
            <Fragment key={rowIdx}>
              <tr className="duplicate-row-primary">
                <td className="col-title">{row.primary?.title ?? '—'}</td>
                <td className="col-trove">{row.primary?.trove ?? row.primary?.troveId ?? ''}</td>
                <td className="col-score" aria-label="Primary item">—</td>
              </tr>
              {(row.matches ?? []).filter((m) => m.result?.id !== row.primary?.id).map((m, matchIdx) => (
                <tr key={matchIdx} className="duplicate-row-match">
                  <td className="col-title">{m.result?.title ?? '—'}</td>
                  <td className="col-trove">{m.result?.trove ?? m.result?.troveId ?? ''}</td>
                  <td className="col-score">{typeof m.score === 'number' ? m.score.toFixed(2) : '—'}</td>
                </tr>
              ))}
            </Fragment>
          ))}
        </tbody>
      </table>
    </div>
  )
}
