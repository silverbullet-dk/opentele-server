package org.opentele.server.provider.constants

class Constants {
    // Ought to be removed and instead be configurable in the system
    public static final String DEFAULT_PATIENT_ROLE = 'Patient'


    // Session variables
    public static final String SESSION_NAME = "name"
    public static final String SESSION_COMMENT = "comment"
    public static final String SESSION_COMMENT_ALL = "commentAll"
    public static final String SESSION_CPR = "cpr"
    public static final String SESSION_PATIENT_ID = "patientId"
    public static final String SESSION_PHONE = "phone"
    public static final String SESSION_GESTATIONAL_AGE = "gestationalAge"
    public static final String SESSION_RUNNING_CTG_MESSAGING = "runningCtgMessaging"
    public static final String SESSION_MOBILE_PHONE = "mobilePhone"
    public static final String SESSION_EMAIL = "email"
    public static final String SESSION_FIRST_RELEATIVE = "firstRelative"
    public static final String SEESION_DATARESPONSIBLE = "dataResponsible"
    public static final String SESSION_ACCESS_VALIDATED = "accessValidated"
    public static final String SESSION_BAD_LOGIN_COUNT = "badLoginCount"
    public static final String SESSION_PATIENT_GROUP_ID = "patientGroupId"


    //Periodic checks
	public static final int BLUE_ALARM_CHECK_INTERVAL = 2 //minutes
    public static final int BLUE_ALARM_CHECK_WINDOW = 30 //minutes
}
