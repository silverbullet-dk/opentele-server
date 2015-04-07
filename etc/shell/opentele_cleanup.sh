#!/bin/bash
TIMESTAMP=`date +%Y%m%d_%H%M`
TOMCAT_ROOT=/cygdrive/c/kihdatamon/programs/tomcat7
LOGBACKUP_DIR=$TOMCAT_ROOT/logs/$TIMESTAMP/
WEBBACKUP_DIR=backup

if [ ! -d $TOMCAT_ROOT ]; then
  echo "Directory '$TOMCAT_ROOT' specified as Tomcat root does not exist."
  exit -1
fi

echo "Tomcat root dir: '$TOMCAT_ROOT'"
echo "Log files backup dir: $LOGBACKUP_DIR"
echo "Web apps backup dir: $WEBBACKUP_DIR"
echo ""
echo "Requested clean-up of: $@"

if [ ! -d $WEBBACKUP_DIR ]; then
  mkdir $WEBBACKUP_DIR
fi

CLEANUP_PERFORMED=false
for arg in "$@"; do
  if [ -d $TOMCAT_ROOT/webapps/$arg ]; then
    echo "Removing war file and expanded folder for '$arg'"
    CONTEXT=$arg
    WAR_FILE=$CONTEXT.war

    mv $TOMCAT_ROOT/webapps/$WAR_FILE $WEBBACKUP_DIR/$WAR_FILE.$TIMESTAMP
    rm -rf $TOMCAT_ROOT/webapps/$CONTEXT
    rm -rf $TOMCAT_ROOT/webapps/$WAR_FILE

    CLEANUP_PERFORMED=true
  else
    echo "'$arg' does not exist and will be ignored"
  fi
done

if [ $CLEANUP_PERFORMED = false ]; then
  echo "Nothing was cleaned. Check that input args matches web application context"
  exit -2
fi

# Move logfiles to backup dir
mkdir -p $LOGBACKUP_DIR
mv $TOMCAT_ROOT/logs/stacktrace*.log $LOGBACKUP_DIR
mv $TOMCAT_ROOT/logs/*log* $LOGBACKUP_DIR

# Remove temp files
rm -f $TOMCAT_ROOT/temp/*.*
rm -rf $TOMCAT_ROOT/work/Catalina/localhost
