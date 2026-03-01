/**
 * Format a number with comma separators for thousands (e.g. 1234 → "1,234").
 * Used for result counts and trove item counts in the UI.
 */
export function formatCount(n) {
  if (typeof n === 'number' && Number.isFinite(n)) {
    return n.toLocaleString()
  }
  return String(n ?? 0)
}
