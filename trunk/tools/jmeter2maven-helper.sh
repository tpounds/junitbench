#!/bin/bash

### Simple helper script for generating jmeter artifacts for internal maven repository

tar=$1
tar xzf $tar

artifact=`echo $tar | sed 's/\(.*\)-[0-9].[0-9].[0-9].tgz/\1/'`
version=`echo $tar | sed 's/.*-\([0-9].[0-9].[0-9]\).tgz/\1/'`

repo="`cd ~; pwd`/.m2/repository"
#svn co https://junitbench.googlecode.com/svn/repo/maven2

for jar in `ls $artifact-$version/lib/ext | grep .jar`; do
   groupId="org.apache.jmeter"
   artifactId="jmeter-`echo $jar | cut -d _ -f 2 | cut -d . -f 1`"
   mvn install:install-file -DgroupId=$groupId -DartifactId=$artifactId -Dversion=$version -DgeneratePom=true -DcreateChecksum=true -Dpackaging=jar -DlocalRepositoryId=local -DlocalRepositoryPath=$repo -Dfile=$artifact-$version/lib/ext/$jar
done

#svn add $repo/*
#svn ci -m "add jmeter $version maven artifacts" $repo
