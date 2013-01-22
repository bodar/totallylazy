#!/bin/sh

version=94
artifact=jcompilo
group=com/googlecode/${artifact}
repo=repo.bodar.com.s3.amazonaws.com
dir=lib/
jar=${dir}${artifact}.jar
url=http://${repo}/${group}/${artifact}/${version}/${artifact}-${version}.jar

if [ $1 = "-u" ]; then 
	rm ${jar}
	shift 1
fi

if [ ! -f ${jar} ]; then
	mkdir -p ${dir} 
	wget ${url} -O ${jar} || curl -o ${jar} ${url}
fi
exec java -jar ${jar} $*