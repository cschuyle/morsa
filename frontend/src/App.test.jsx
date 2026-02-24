import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import App from './App'

describe('App', () => {
  beforeEach(() => {
    vi.stubGlobal('fetch', vi.fn((url) => {
      if (typeof url === 'string' && url.includes('/api/troves')) {
        return Promise.resolve({ ok: true, json: () => Promise.resolve([]) })
      }
      return Promise.resolve({ ok: true, json: () => Promise.resolve({ status: 'UP' }) })
    }))
  })

  it('renders Morsor heading', async () => {
    render(<App />)
    expect(screen.getByRole('heading', { name: 'Morsor' })).toBeInTheDocument()
    await waitFor(() => {
      expect(screen.getByText('Backend is up')).toBeInTheDocument()
    })
  })

  it('renders search form with Search button', async () => {
    render(<App />)
    await waitFor(() => {
      expect(screen.getByText('Backend is up')).toBeInTheDocument()
    })
    expect(screen.getByRole('button', { name: 'Search' })).toBeInTheDocument()
  })
})
