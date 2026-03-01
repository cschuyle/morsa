/**
 * Mock fetch that returns canned data from test fixtures.
 * Use in frontend tests when you want to assert against the same data shape as the real API
 * (e.g. little-prince trove, "The Little Prince, in Ancient Greek" search result).
 *
 * Usage:
 *   import { mockFetchWithCannedData } from './test/mockFetchWithCannedData'
 *   beforeEach(() => { vi.stubGlobal('fetch', mockFetchWithCannedData()) })
 */
import trovesFixture from '../fixtures/troves.json'
import searchResponseFixture from '../fixtures/searchResponse.json'

export function mockFetchWithCannedData() {
  return (url, _options) => {
    const path = typeof url === 'string' ? url : url?.url ?? ''
    if (path.includes('/api/troves')) {
      return Promise.resolve({
        ok: true,
        status: 200,
        json: () => Promise.resolve(trovesFixture),
      })
    }
    if (path.includes('/api/search')) {
      return Promise.resolve({
        ok: true,
        status: 200,
        json: () => Promise.resolve(searchResponseFixture),
      })
    }
    if (path.includes('/api/status')) {
      return Promise.resolve({
        ok: true,
        status: 200,
        json: () => Promise.resolve({ status: 'UP', cache: { entries: 0, estimatedBytes: 0 } }),
      })
    }
    return Promise.resolve({ ok: true, status: 200, json: () => Promise.resolve({}) })
  }
}
