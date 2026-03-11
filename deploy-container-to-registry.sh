set -e

if [ -n "$(git status --porcelain)" ]; then
  echo "Error: there are uncommitted changes. Commit or stash them before deploying." >&2
  exit 1
fi

# Default image coordinates
: "${MOOCHO_REGISTRY:=namespace/morsor}"

# Derive unified version string if not already provided
if [ -z "${MOOCHO_VERSION:-}" ]; then
  GIT_SHA_SHORT="$(git rev-parse --short=7 HEAD)"
  BUILD_DATE="$(date +%Y%m%d)"
  # 24-hour HHMM timestamp
  BUILD_TIME_HHMM="$(date +%H%M)"
  MOOCHO_VERSION="${BUILD_DATE}-${BUILD_TIME_HHMM}-${GIT_SHA_SHORT}"
fi

echo "Using MOOCHO_VERSION=${MOOCHO_VERSION}"

# Note: MOOCHO_ARCHITECTURE for cloud is probably linux/amd64
if [ -n "${MOOCHO_ARCHITECTURE}" ]; then
  echo "Building for architecture: ${MOOCHO_ARCHITECTURE}"
  docker build --platform "${MOOCHO_ARCHITECTURE}" \
    --build-arg MOOCHO_VERSION="${MOOCHO_VERSION}" \
    -t morsor \
    -t "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}" \
    -t "${MOOCHO_REGISTRY}:latest" \
    . || true
else
  echo "Building for host architecture"
  docker build \
    --build-arg MOOCHO_VERSION="${MOOCHO_VERSION}" \
    -t morsor \
    -t "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}" \
    -t "${MOOCHO_REGISTRY}:latest" \
    . || true
fi
docker push "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}"
docker push "${MOOCHO_REGISTRY}:latest"
