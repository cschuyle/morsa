set -e
: "${MOOCHO_REGISTRY:=cschuyle/morsor}"
: "${MOOCHO_VERSION:=latest}"
# MOOCHO_ARCHITECTURE for cloud is probably linux/amd64
if [ -n "${MOOCHO_ARCHITECTURE}" ]; then
  docker build --platform "${MOOCHO_ARCHITECTURE}" -t morsor -t "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}" .
else
  docker build -t morsor -t "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}" .
fi
docker push "${MOOCHO_REGISTRY}:${MOOCHO_VERSION}"
