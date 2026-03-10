set -e
: "${MOOCHO_REGISTRY:=namespace/morsor}"
: "${MOOCHO_VERSION:=latest}"
# Note: MOOCHO_ARCHITECTURE for cloud is probably linux/amd64
if [ -n "${MOOCHO_ARCHITECTURE}" ]; then
  echo "Building for architecture: ${MOOCHO_ARCHITECTURE}"
  docker build --platform "${MOOCHO_ARCHITECTURE}" -t morsor -t "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}" . || true
else
  echo "Building for host architecture"
  docker build -t morsor -t "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}" . || true
fi
docker push "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}"
