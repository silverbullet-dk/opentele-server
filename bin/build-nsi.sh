#!/bin/bash
set -x
#
#  -*- Sh -*- 
# 
# build-nsi.sh - Build NSIS installer file from parameters. 
# 
# 

env >> /tmp/env.out
echo `basename $0` >> /tmp/env.out

environment=$1
branch=$2

export NSIS=/pack/nsis/bin/makensis
export PROJECT_ROOT="$(dirname $0)/.."

NSIS_OPT=""

export WAR_FILE_NAME=ROOT-${environment}-${branch}.war
export INSTALLER_FILE_NAME=ROOT-${environment}-${branch}
export APP_VERSION=${branch}

case $environment in
    winbuild)
	echo "For win integ test"
        export TOMCAT_DIR="C:\kihdatamon\programs\Tomcat 7.0_KIHdatamon"
        export SERVICE_NAME=KIHdatamon
        ;;
    datamon_test)
	echo "For datamon_test"
        export TOMCAT_DIR="C:\kihdatamon\programs\tomcat-7.0.30"
        export SERVICE_NAME=KIH-Datamonitorering
        ;;
    staging_midt)
	echo "For stag_midt"
        export TOMCAT_DIR="C:\kihdatamon\programs\tomcat7"
        export SERVICE_NAME=OpenTele-RM
        ;;
    staging_nord)
	echo "For stag_nord"
        export TOMCAT_DIR="C:\kihdatamon\programs\tomcat7"
        export SERVICE_NAME=KIH_STG_RN_Tomcat
        ;;
    staging_hovedstaden)
	echo "For stag_hovedstaden"
        export TOMCAT_DIR="C:\kihdatamon\programs\tomcat7"
        export SERVICE_NAME=OpenTele
        ;;
    production_midt)
	echo "For prod_midt"
        export TOMCAT_DIR="C:\kihdatamon\programs\tomcat7"
        export SERVICE_NAME=OpenTele
        ;;
    production_nord)
	echo "For prod_nord"
        export TOMCAT_DIR="C:\kihdatamon\programs\tomcat7"
        export SERVICE_NAME=OpenTele
        ;;
    production_hovedstaden)
	echo "For prod_hovedstaden"
        export TOMCAT_DIR="C:\kihdatamon\programs\tomcat7"
        export SERVICE_NAME=OpenTele
        ;;
esac 

echo "Executing command"
$NSIS -DTOMCAT_DIR="${TOMCAT_DIR}" -DWAR_FILE_NAME="${PROJECT_ROOT}"/target/${WAR_FILE_NAME} -DSERVICE_NAME=${SERVICE_NAME} -DAPP_VERSION=${APP_VERSION}  -DINSTALLER_FILE_NAME=${INSTALLER_FILE_NAME} "$PROJECT_ROOT"/src/nsi/deployOpenTele.nsi

