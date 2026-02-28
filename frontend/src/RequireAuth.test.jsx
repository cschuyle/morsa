import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import { RequireAuth } from './RequireAuth'

describe('RequireAuth', () => {
  let fetchMock

  beforeEach(() => {
    fetchMock = vi.fn((url, options) => {
      return Promise.resolve({
        status: 200,
        ok: true,
        json: () => Promise.resolve([]),
      })
    })
    vi.stubGlobal('fetch', fetchMock)
    // Ensure dev token is used (localhost)
    Object.defineProperty(window, 'location', {
      value: { ...window.location, hostname: 'localhost' },
      writable: true,
    })
  })

  afterEach(() => {
    vi.unstubAllGlobals()
  })

  it('sends Authorization Bearer header on initial /api/troves auth check', async () => {
    render(
      <RequireAuth>
        <span>Child content</span>
      </RequireAuth>
    )

    await waitFor(() => {
      expect(fetchMock).toHaveBeenCalled()
    })

    const trovesCall = fetchMock.mock.calls.find(
      (call) => typeof call[0] === 'string' && call[0].includes('/api/troves')
    )
    expect(trovesCall).toBeDefined()
    const options = trovesCall[1]
    expect(options?.headers).toBeDefined()
    expect(options.headers?.Authorization).toBe('Bearer dev-token')
  })

  it('renders children after successful auth', async () => {
    render(
      <RequireAuth>
        <span>Child content</span>
      </RequireAuth>
    )

    await waitFor(() => {
      expect(screen.getByText('Child content')).toBeInTheDocument()
    })
  })
})
