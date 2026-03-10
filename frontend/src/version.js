const fullSha = import.meta.env.VITE_GIT_SHA || 'dev'
const shortSha = fullSha.substring(0, 7)

const envBuildDate = import.meta.env.VITE_BUILD_DATE
const today = new Date().toISOString().slice(0, 10).replace(/-/g, '')
const buildDate = envBuildDate || today

const envTimeTenths = import.meta.env.VITE_BUILD_TIME_TENTHS
const now = new Date()
const msSinceMidnight = now.getTime() - new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime()
const timeTenths = envTimeTenths != null && envTimeTenths !== '' ? String(envTimeTenths) : String(Math.floor(msSinceMidnight / 1000 / 10))

export const APP_VERSION = `${buildDate}-${timeTenths}-${shortSha}`

