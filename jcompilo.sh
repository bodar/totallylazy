#!/bin/sh
set -e


JAVA_VERSION=${JAVA_VERSION-8}
JAVA_OPTS="-server -XX:+TieredCompilation -Djava.net.useSystemProxies=true ${JAVA_OPTS}"
BUILD_NUMBER=${BUILD_NUMBER-dev.build}
version=1.11
artifact=jcompilo
dir=lib/
jar=${dir}${artifact}.jar
url=https://github.com/bodar/${artifact}/releases/download/${version}/${artifact}-${version}.jar
remote_sh=${url}.sh

type -t setjava > /dev/null && setjava -q ${JAVA_VERSION} || if [ -n "${JAVA_HOME}" ]; then PATH=${JAVA_HOME}/bin:${PATH}; fi

if [ "$1" = "update" ]; then
	rm -f ${jar}
fi

if [ ! -f ${jar} ]; then
	mkdir -p ${dir}
	wget -O ${jar} ${url} || curl -o ${jar} ${url}
fi
exec java -showversion -Dbuild.number=${BUILD_NUMBER} ${JAVA_OPTS} -jar ${jar} $*
