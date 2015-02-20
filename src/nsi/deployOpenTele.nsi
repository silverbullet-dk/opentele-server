#########################################################################################
#	Script will build a windows installer with one WAR file (see below).		#
#	As part of the installation the tomcat server process is restarted (stop/start)	#
#	or startet (if not already running).						#
#											#
#	Build installer with NSIS's makensis using the following args:			#
#	./makensis -DTOMCAT_DIR="<path to tomcat root>" 	 			#
#		   -DWAR_FILE_NAME="<path/name of WAR file to deploy>"			#
#		   -DSERVICE_NAME="<name of tomcat service>"				#
#		   -DAPP_VERSION="<banch/version tag>"					#
#		   -DINSTALLER_FILE_NAME="<name of the generated installer file>"	#
#											#
#	Script by Silverbullet A/S for the OpenTele project.				#
#########################################################################################

#Setup
CRCCheck off
name "OpenTele"
caption "Silverbullet OpenTele WAR Deploy"
outfile "${INSTALLER_FILE_NAME}.exe" 

section

#Make sure the user really wants this
messageBox MB_YESNO "Are you sure you want to deploy version ${APP_VERSION} to this server? This will take a while.." IDNO EXIT

#Include new WAR file
setOutPath "${TOMCAT_DIR}\webapps"

DetailPrint "Starting deploy.."
DetailPrint "Assuming tomcat\webapps is in: ${TOMCAT_DIR}\webapps"

#Inspect the Tomcat server service status
SimpleSC::GetServiceStatus "${SERVICE_NAME}" ; get service status
Pop $0 ; Get return errorcode (!=0) otherwise success (0)
Pop $1 ; Get the status of the service (see below)
DetailPrint "${SERVICE_NAME} service getStatus call errorcode was: $0. Service status was: $1"

#Did the call return okay?
IntCmp $0 0 0 +4 +4 
      IntCmp $1 4 0 +2 +2
            Goto SERVICE_RUNNING ; If service is up ($1 == 4), goto kill it
      	    Goto SERVICE_DOWN ; If service is down, goto deploy

#Else ; Service status call exit code != 0 (!= success)
messageBox MB_OK "Could not get status of the Tomcat service!"
DetailPrint "The call to SimpleSC::GetServiceStatus failed - exiting"
Goto EXIT

SERVICE_RUNNING:
DetailPrint "Attempting to stop tomcat service (${SERVICE_NAME})"
SimpleSC::StopService "${SERVICE_NAME}" 0 30 ; Kill the service and don't wait for file release, timeout 30 secs
Pop $0 ; Get return code
DetailPrint "SimpleSC::StopService returned $0"
IntCmp $0 0 0 +2 +2 ; If success move on to serivce_down, if not exit while crying..
Goto SERVICE_DOWN
Goto EXIT_WHILE_CRYING

SERVICE_DOWN:
DetailPrint "Tomcat service (${SERVICE_NAME}) is down!"

DetailPrint "Deleting old WAR and ROOT dir.."
Delete "${TOMCAT_DIR}+\webapps\opentele-server.war"
IfErrors 0 +2
DetailPrint 'There was an error trying to delete the file!'

RMDir /r "${TOMCAT_DIR}+\webapps\ROOT"
IfErrors 0 +2
DetailPrint 'There was an error trying to delete the file!'

DetailPrint "Copying in new WAR file.."
File /oname=ROOT.war "${WAR_FILE_NAME}"

DetailPrint "Attempting to start tomcat service (${SERVICE_NAME}) again.."
SimpleSC::StartService "${SERVICE_NAME}" 0 30 ; Get the service back up
Pop $0
DetailPrint "SimpleSC::StartService returned: $0"
IntCmp $0 0 0 +2 +2 ; If success move on to exit, if not notify user and exit
Goto EXIT
messageBox MB_OK "Failed to start service ${SERVICE_NAME}"
Goto EXIT

EXIT_WHILE_CRYING:
messageBox MB_OK "Something went wrong :-/"
DetailPrint "Commence crying.. "

EXIT:
sectionEnd
