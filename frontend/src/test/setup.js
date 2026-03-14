import '@testing-library/jest-dom/vitest'

// JSDOM does not provide ResizeObserver (used by SearchResultsGrid for gallery back-to-top).
if (typeof globalThis.ResizeObserver === 'undefined') {
  globalThis.ResizeObserver = class ResizeObserver {
    constructor(callback) {
      this.callback = callback
    }
    observe() {}
    unobserve() {}
    disconnect() {}
  }
}
