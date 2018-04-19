<html>
<head>
<title>Student Lookup</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!--- External JavaScript containing functions for the timetable application --->
<script type="text/javascript" src="script.js"></script>
<!--- External CSS containing styling for the webpage --->
<link rel="stylesheet" type="text/css" href="timetableStyle.css">

</head>

<body onUnload="JavaScript:closePopUpWindow()">

<table align="center" width="780px" border="0" cellspacing="2" cellpadding="2">
<tr>
	<td colspan="4" align="center">
	<img src="infoBanner.jpg" align="center">
	</td>
</tr>

<tr bgcolor="#408080">
	<td colspan="4">
		<font class="tableMain">   Student Lookup</font>
	</td>
</tr>

<tr>
<td align="left" colspan="3">
<a href="Halesowen College - Intranet_Staff.cfm" class="link">Click here to return to Intranet Homepage</a>
</td>
</tr>
<tr><td><br></td></tr>
<tr><!---label--->
	<td width="20%"><cfform name="idsearch" action = "Timetable_Search.cfm" preservedata="Yes">
		<font class="textboxLabel">...by student ID</font>
	</td>
	<td width="50%"><!--- idInput text box --->
		<cfinput type="text" name="idInput" maxlength="11" width="100" height="25" font="Arial, Helvetica, sans-serif"></cfinput>
		<font class="textboxLabelInfo">eg. HUG04012030</font>		
	</td>
	<td width="10%"></td>
	<td rowspan="2"><!--- sorting text info --->
		<font class="resultName">Sort results:</font> 
		<br>
		<font class="textboxLabelInfo">Use the options below to sort the results in the order you require:</font>
		<br>
		<br>
		<font class="textboxLabel">Ascending</font>		
		<cfinput type="radio" name="sortOrder" value="ASC" checked="yes" ><!--- radio button ASC --->
		<br>
		<font class="textboxLabel">Descending</font> 
		<cfinput type="radio" name="sortOrder" value="DESC"><!--- radio button DESC --->
		
		<br><br>
		
		<cfselect name="sortColumn"><!--- select drop down --->
			<option value="student_id" <cfif IsDefined("form.sortColumn") AND form.sortColumn EQ "student_id">selected<cfelse>selected</cfif>>Student ID</option>
			<option value="forename" <cfif IsDefined("form.sortColumn") AND form.sortColumn EQ "forename">selected</cfif>>Forename</option>
			<option value="surname" <cfif IsDefined("form.sortColumn") AND form.sortColumn EQ "surname">selected</cfif>>Surname</option>
		</cfselect>
		</td>
</tr>

<tr>
	<td><!--- label --->
	<font class="textboxLabel">...by student name</font> 
	
	</td>
	<td>
	<br>
	<br><!--- forenameInput --->
	<cfinput type="text" name="forenameInput" maxlength="20" width="100" height="25" font="Arial, Helvetica, sans-serif"></cfinput>
	<font class="textboxLabelInfo">Forename</font> 
	<br>
	<!--- surnameInput --->
	<cfinput type="text" name="surnameInput" maxlength="20" width="100" height="25" font="Arial, Helvetica, sans-serif"></cfinput>
	<font class="textboxLabelInfo">Surname</font> 
	<br>
	<br><!--- text info --->
	<font class="textboxLabelInfo">Include middle names / doubled barrelled names in the surname box.
	The search will not find any matching Students if they are entered into the forename box</font>
	</td>
</tr>

<tr>
	<td align="center" colspan="3">	<!--- submit button / hidden input to say the form has been filled in --->
	<input type="submit" name="submit" value="Search" align="middle">
		<cfinput type="hidden" name="oncethrough" value="Yes"></td>
</tr>
</cfform>
</table>


<cfif IsDefined("form.oncethrough") IS "Yes">
	<!--- trim whitespace from inputs --->
	<cfif form.idInput IS NOT ""><cfset form.idInput = trim(form.idInput)></cfif>
	<cfif form.forenameInput IS NOT ""><cfset form.forenameInput = trim(forenameInput)></cfif>
	<cfif form.surnameInput IS NOT ""><cfset form.surnameInput = trim(surnameInput)></cfif>
	
	<!--- check to see if more than one field is filled in --->
	<cfif form.idInput IS NOT "" AND form.forenameInput IS NOT "">
		<cfset invalidInput = "true">
	</cfif>
	<cfif form.idInput IS NOT "" AND form.surnameInput IS NOT "">
		<cfset invalidInput = "true">
	</cfif>
	
	<!--- only do searching when invalidInput = false --->
	<cfif IsDefined("invalidInput") IS "false">
		<cfif form.idInput IS NOT "" AND Len(form.idInput) LE 11><!--- check that the id is no more than 11 characters long --->
			<cfset input = form.idInput>
			<cfquery name="dataSet" datasource="cfoffline_celcat">
				<!--- stored procedure search for ids containing form.idInput --->				
				FYP.timetable_studentSearch_like '#form.idInput#', '#form.sortOrder#', '#form.sortColumn#'
			</cfquery>
		</cfif>
		<cfif form.forenameInput IS NOT "" OR form.surnameInput IS NOT "">
			<cfset input = form.forenameInput &' ' & form.surnameInput>
			<cfquery name="dataSet" datasource="cfoffline_celcat">
			<!--- Query below also returns the names with a matching soundex code, phonetically matching (sounds similar, spelt differently) --->
	
				FYP.timetable_studentSearch_name '#form.forenameInput#', '#form.surnameInput#', '#form.sortOrder#', '#form.sortColumn#'
			</cfquery>
		</cfif>
	
	</cfif>
</cfif>
<!--- If a dataSet exists from a query above then code below will display the results --->
<cfif IsDefined('dataSet') IS TRUE>
<br>
	<table align="center" width="780px" border="0">
		<tr>
			<td class="tableMain">Search Results for '<cfoutput>#input#</cfoutput>'...</td>
		</tr>
		<cfif #dataSet.recordCount# GT 0><!--- If the recordCount is greater than 0, definately results to display --->		
			<tr>
			<td class="textboxLabel" height="40px" valign="top">
				Find your student below and click on the student's ID to view their timetable. Alternatively search again above.
			</td>
			</tr>
			
			<tr>
			<td>
				<table width="500px" border="0" cellpadding="2" cellspacing="2">
					<tr>
					<td class="tableResults">Name</td>
					<td class="tableResults">Student ID</td>
					</tr>
						<cfoutput query="dataSet">
						<tr>
							<td>
							<!--- Before displaying the data, surnames need to be put into lowercase 
							with uppercase first letter, they are stored uppercase in the database --->
							<cfset surname = #dataSet.surname#><!--- gets surname from resultSet --->
							<cfset surnameLen = len(surname)><!---gets length of the surname for the processing to uppercase first letter --->
							
							<font class="resultName">#dataSet.forename# #Ucase(left(surname, 1))##Lcase(right(surname, surnameLen - 1))#</font>
							</td>
							
							<cfset SID="#dataSet.student_id#">
							<cfset SESSION.staff = "staff">
							<td>
							<!--- calls javascript method to popup the timetable in new window --->
							<a href="JavaScript:popUpWindow('Timetable_App.cfm?student_id=#SID#')" class="link">#dataSet.student_id#</a>
							</td>
						</tr>
						</cfoutput>
				</table>
			</td> 
			</tr>
		</table>
					
	<cfelse>
		<tr class="resultName">
			<td>No matching records found</td>
	  	</tr>
	</table>
	</cfif>
	<cfelseif IsDefined("invalidInput") IS "true"><!--- if invalidInput then show error message --->
	<table align="center" width="780px" border="0">
		<tr bgcolor="#FF3333">
			<td><font class="tableMainError">Invalid Input in Search Fields!</font></td>
		</tr>
		<tr>
		<td><br><font class="resultName">Please only search by either Student ID or by Student name. Not by both. 
		<br>
		<br>Please search again above.</font></td>
		</tr>
		</table>
</cfif>
</body>
</html>
