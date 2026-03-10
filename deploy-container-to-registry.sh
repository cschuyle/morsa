set -e

if [ -n "$(git status --porcelain)" ]; then
  echo "Error: there are uncommitted changes. Commit or stash them before deploying." >&2
  exit 1
fi

# Default image coordinates
: "${MOOCHO_REGISTRY:=namespace/morsor}"
: "${MOOCHO_VERSION:=latest}"

# Derive build metadata for frontend versioning
GIT_SHA="$(git rev-parse HEAD)"
BUILD_DATE="$(date +%Y%m%d)"
# Seconds elapsed in the current day, divided by 10
BUILD_TIME_TENTHS="$(( (10#$(date +%H) * 3600 + 10#$(date +%M) * 60 + 10#$(date +%S)) / 10 ))"

export VITE_GIT_SHA="${GIT_SHA}"
export VITE_BUILD_DATE="${BUILD_DATE}"
export VITE_BUILD_TIME_TENTHS="${BUILD_TIME_TENTHS}"
echo "Frontend build metadata: VITE_GIT_SHA=${VITE_GIT_SHA} VITE_BUILD_DATE=${VITE_BUILD_DATE} VITE_BUILD_TIME_TENTHS=${VITE_BUILD_TIME_TENTHS}"

# Note: MOOCHO_ARCHITECTURE for cloud is probably linux/amd64
if [ -n "${MOOCHO_ARCHITECTURE}" ]; then
  echo "Building for architecture: ${MOOCHO_ARCHITECTURE}"
  docker build --platform "${MOOCHO_ARCHITECTURE}" -t morsor -t "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}" . || true
else
  echo "Building for host architecture"
  docker build -t morsor -t "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}" . || true
fi
docker push "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}"
