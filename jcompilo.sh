#!/bin/sh

JAVA_OPTS=${JAVA_OPTS}
BUILD_NUMBER=${BUILD_NUMBER-dev.build}
version=137
artifact=jcompilo
group=com/googlecode/${artifact}
repo=repo.bodar.com
dir=lib/
jar=${dir}${artifact}.jar
url=http://${repo}/${group}/${artifact}/${version}/${artifact}-${version}
remote_jar=${url}.jar
remote_sh=${url}.sh

if [ "$1" = "update" ]; then
	rm ${jar}
	shift 1
fi

if [ ! -f ${jar} ]; then
	mkdir -p ${dir} 
	wget -O ${jar} ${remote_jar} || curl -o ${jar} ${remote_jar}
	wget -O $0 ${remote_sh} || curl -o $0 ${remote_sh}
fi
exec java -showversion -Dbuild.number=${BUILD_NUMBER} ${JAVA_OPTS} -jar ${jar} $*
