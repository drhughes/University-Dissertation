<!--- timetable_app relevent below, allows SESSIONS to be used
This enables the studentid and staff boolean to be stored and used without the user knowing --->
<cfapplication name="timetable_app" sessionmanagement="yes" sessiontimeout="#CreateTimeSpan(0, 0, 20, 0)#">
