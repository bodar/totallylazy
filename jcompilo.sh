#!/bin/sh

version=93
name=jcompilo
dir=lib/
jar=${dir}${name}.jar
url=http://${name}.googlecode.com/files/${name}-${version}.jar

while getopts "u" opt; do
  case $opt in
    u) 
      rm ${jar}
      shift $(optind)
      ;;
  esac
done

if [ ! -f ${jar} ]; then
	mkdir -p ${dir} 
	wget ${url} -O ${jar}
fi
exec java -jar ${jar} $*
