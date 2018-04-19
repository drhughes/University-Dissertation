<cfif IsDefined("SESSION.staff") IS TRUE><!--- check if the SESSION.staff var is true --->
<!--- is it is get the studentid param from URL, this stops students passing a studenid as a parameter --->
<cfif #SESSION.staff# eq "staff">
	<cfset SESSION.studentid=URL.student_id>
</cfif>
</cfif>

<html>
<head>
<title>Timetable</title>
</head>

<!--- External JavaScript containing functions for the timetable application --->
<script type="text/javascript" src="script.js"></script>
<!--- External CSS containing styling for the webpage --->
<link rel="stylesheet" type="text/css" href="timetableStyle.css">

<script type="text/javascript">

function resize() {
    var w_newWidth,w_newHeight;
    var w_maxWidth=1600, w_maxHeight=1200;
    if (navigator.appName.indexOf("Microsoft") != -1)
    {
        w_newWidth=document.body.clientWidth;
        w_newHeight=document.body.clientHeight;
    }else{
        var netscapeScrollWidth=15;
        w_newWidth=window.innerWidth-netscapeScrollWidth;
        w_newHeight=window.innerHeight-netscapeScrollWidth;
    }
    if (w_newWidth>w_maxWidth)
        w_newWidth=w_maxWidth;
    if (w_newHeight>w_maxHeight)
        w_newHeight=w_maxHeight;
		
    document.timetable.setSize(w_newWidth,w_newHeight);
    window.scroll(0,0);
}
   window.onResize = resize;
   window.onLoad = resize;

<!--- sets the print variable in the timetable to true --->
function setPrint(){
	document.timetable.setPrint();
}
</script>

<body bgcolor="#cccccc" onLoad="resize()" onResize="resize()">
<table border="0" width="100%">
<tr>
<td align="left"><a href="" onClick="setPrint()" class="link">Print Timetable</a></td>
<td align="right"><a href="" onClick="JavaScript:window.close()" class="link">Close Window</a></td>
</tr>
</table>

<table border="0" width="100%">
<td align="center">
	<cfapplet appletSource="timetable" name="timetable" studentid=#SESSION.studentid# staff=#SESSION.staff#> 
</td>
</table>		

</body>
</html>
