
<html>
<head>
<title>Untitled Document</title>

</head>

<body>
<cfif IsDefined("SESSION.staff") IS TRUE>
<cfif #SESSION.staff# neq "">
<cfset tmp=#StructDelete(SESSION, "staff")#>
<cfoutput>SESSION.staff deleted successfully</cfoutput>
</cfif>
<cfelse>
<cfoutput>no SESSION variable for staff set</cfoutput>
</cfif>
<p>
<cfif IsDefined("SESSION.studentid") IS TRUE>
<cfif #SESSION.studentid# neq "">
<cfset tmp=#StructDelete(SESSION,"studentid")#>
<cfoutput>SESSION.studentid deleted successfully</cfoutput>
</cfif>
<cfelse>
<cfoutput>no SESSION variable for student set</cfoutput>
</cfif>

</body>
</html>
