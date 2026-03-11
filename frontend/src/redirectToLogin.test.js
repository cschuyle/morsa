import { describe, it, expect, afterEach } from 'vitest'
import { redirectToLogin } from './redirectToLogin'

describe('redirectToLogin', () => {
  afterEach(() => {
    delete window.location
  })

  it('preserves the full query string in next', () => {
    const locationMock = {
      pathname: '/',
      search: '?q=walrus&trove=one&trove=two&fileTypes=jpg&fileTypes=png',
      hash: '',
      href: 'http://localhost:5173/',
    }
    Object.defineProperty(window, 'location', {
      configurable: true,
      writable: true,
      value: locationMock,
    })

    redirectToLogin()

    expect(window.location.href).toBe('/login?next=%2F%3Fq%3Dwalrus%26trove%3Done%26trove%3Dtwo%26fileTypes%3Djpg%26fileTypes%3Dpng')
  })
})
