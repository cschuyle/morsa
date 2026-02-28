/**
 * In dev, use the hardwired API token so you don't need to log in.
 * Set VITE_DEV_API_TOKEN in .env.local to override (default: dev-token).
 * Token is sent when: (1) Vite dev server (import.meta.env.DEV), or
 * (2) app is loaded from localhost (e.g. gradle bootRun at http://localhost:8080).
 */
function getDevApiToken() {
  const isViteDev = import.meta.env.DEV
  const isLocalhost =
    typeof window !== 'undefined' &&
    (window.location?.hostname === 'localhost' || window.location?.hostname === '127.0.0.1')
  if (!isViteDev && !isLocalhost) {
    return null
  }
  if (import.meta.env.VITE_DEV_API_TOKEN !== undefined && import.meta.env.VITE_DEV_API_TOKEN !== '') {
    return import.meta.env.VITE_DEV_API_TOKEN
  }
  return 'dev-token'
}

/**
 * Headers to add to API requests. In dev, adds Authorization: Bearer <dev-token> when not logged in.
 */
export function getApiAuthHeaders() {
  const token = getDevApiToken()
  if (token) {
    return { Authorization: `Bearer ${token}` }
  }
  return {}
}
