#!/bin/bash

TIMESTAMP=`date +%Y%m%d_%H%M`
TOMCAT_ROOT=/cygdrive/c/kihdatamon/programs/tomcat-7.0.30
LOGBACKUP_DIR=$TOMCAT_ROOT/logs/$TIMESTAMP/

CONTEXT_ROOT=ROOT
WARFILE=$CONTEXT_ROOT.war

echo $LOGDIR

# Move logfiles to backup dir
mkdir -p $LOGBACKUP_DIR
mv $TOMCAT_ROOT/stacktrace.log $LOGBACKUP_DIR
mv $TOMCAT_ROOT/logs/*log* $LOGBACKUP_DIR

# Remove temp files
rm -rf $TOMCAT_ROOT/temp/*.*
rm -rf $TOMCAT_ROOT/work/Catalina/localhost/_

# Previous war-file + expanded version
mv $TOMCAT_ROOT/webapps/$WARFILE ~/backup/$WARFILE.$TIMESTAMP
rm -rf $TOMCAT_ROOT/webapps/$CONTEXT_ROOT
rm -rf $TOMCAT_ROOT/webapps/$WARFILE