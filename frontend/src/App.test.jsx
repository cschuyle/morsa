import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import App from './App'

describe('App', () => {
  beforeEach(() => {
    vi.stubGlobal('fetch', vi.fn())
  })

  it('renders Morsor heading', async () => {
    vi.mocked(fetch).mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve({ status: 'UP' }),
    })
    render(<App />)
    expect(screen.getByRole('heading', { name: 'Morsor' })).toBeInTheDocument()
    await waitFor(() => {
      expect(screen.getByText('Backend is up')).toBeInTheDocument()
    })
  })

  it('increments count when button is clicked', async () => {
    vi.mocked(fetch).mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve({ status: 'UP' }),
    })
    render(<App />)
    await waitFor(() => {
      expect(screen.getByText('Backend is up')).toBeInTheDocument()
    })
    const button = screen.getByRole('button', { name: /count is 0/i })
    fireEvent.click(button)
    expect(screen.getByRole('button', { name: /count is 1/i })).toBeInTheDocument()
    fireEvent.click(button)
    expect(screen.getByRole('button', { name: /count is 2/i })).toBeInTheDocument()
  })
})
