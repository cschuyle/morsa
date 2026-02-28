/**
 * Renders "find uniques" results: items in primary trove that have no match in compare troves.
 * Simple table of primary items (same row style as duplicate primary rows).
 * sortBy / sortDir / onSortChange: optional column sort (title, trove).
 */
export function UniquesResultsView({ results = [], sortBy = null, sortDir = 'asc', onSortChange }) {
  const handleSort = (columnId) => {
    if (!onSortChange) return
    const nextDir = sortBy === columnId && sortDir === 'asc' ? 'desc' : 'asc'
    onSortChange(columnId, nextDir)
  }

  if (!results.length) {
    return (
      <p className="duplicate-results-empty">No unique items. Every primary item has a match in the compare troves.</p>
    )
  }
  return (
    <div className="duplicate-results uniques-results">
      <table className="duplicate-results-table">
        <thead>
          <tr>
            <th
              className={`col-title ${onSortChange ? 'sortable' : ''}`}
              onClick={onSortChange ? () => handleSort('title') : undefined}
            >
              Title
              {sortBy === 'title' && <span className="sort-indicator">{sortDir === 'asc' ? ' ↑' : ' ↓'}</span>}
            </th>
            <th
              className={`col-trove ${onSortChange ? 'sortable' : ''}`}
              onClick={onSortChange ? () => handleSort('trove') : undefined}
            >
              Trove
              {sortBy === 'trove' && <span className="sort-indicator">{sortDir === 'asc' ? ' ↑' : ' ↓'}</span>}
            </th>
          </tr>
        </thead>
        <tbody>
          {results.map((row, idx) => (
            <tr key={idx} className="duplicate-row-primary">
              <td className="col-title">{row?.title ?? '—'}</td>
              <td className="col-trove">{row?.trove ?? row?.troveId ?? ''}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
