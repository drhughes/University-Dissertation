<!--- hug08074842 dee07068776 ben07071174--->

<cfquery name="accessInfo" datasource="cfoffline_celcat">
FYP.getLectureEventsByStudentIDFaster '#URL.studentid#'
</cfquery>
<!--- <cfdump var="#accessInfo#"> --->
<!--- produces XML document --->
<cfxml variable="TimetableXML">
   <Student student_id="<cfoutput>#URL.studentid#</cfoutput>" 
   firstname="<cfoutput>#XMLFormat(accessInfo.forename)#</cfoutput>" 
   lastname="<cfoutput>#XMLFormat(accessInfo.surname)#</cfoutput>">
   <!--- #XMLFORMAT(string) is used to escape special characters such as (&) --->
      <cfloop query="accessInfo">
         <Lecture title="<cfoutput>#XMLFormat(accessInfo.lecture)#</cfoutput>"
		 		weeks="<cfoutput>#XMLFormat(accessInfo.weeks)#</cfoutput>">
            <DOW><cfoutput>#XMLFormat(accessInfo.Day_Of_Week)#</cfoutput></DOW>
			<START><cfoutput>#XMLFormat(accessInfo.START_TIME)#</cfoutput></START>
			<END><cfoutput>#XMLFormat(accessInfo.END_TIME)#</cfoutput></END>
			<ROOM><cfoutput>#XMLFormat(accessInfo.ROOMNAME)#</cfoutput></ROOM>
			<staff><cfoutput>#XMLFormat(accessInfo.staffname)#</cfoutput></staff>
         </Lecture>
      </cfloop>
   </Student>
</cfxml>
<cfset XMLText=ToString(TimetableXML)>
<cfoutput>#XMLText#</cfoutput>