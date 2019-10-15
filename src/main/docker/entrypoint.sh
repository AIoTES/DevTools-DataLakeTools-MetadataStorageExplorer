#!/bin/sh

if [[ -n ${BASE_PATH} ]]; then
  JAVA_OPTS="${JAVA_OPTS} -Dserver.context-path=${BASE_PATH}"
  echo "Notice: BASE_PATH set to '${BASE_PATH}'"
fi

echo "The application will start in ${SIMLIFE_SLEEP}s..." && sleep ${SIMLIFE_SLEEP}
exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar "${HOME}/app.war" "$@"
