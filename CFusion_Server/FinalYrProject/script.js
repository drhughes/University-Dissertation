<!--- External JavaScript containing functions for the timetable application --->

var windowName //stores the popup window, used when closePopUpWindow is called

<!--- opens a new window with the parameter filename as its destination --->
function popUpWindow(filename){
var destination = filename//the file that will be opened in the popup
//Resizeable, location disabled (does not work in firefox) set width and height
windowName = window.open(destination, "windowName", "location=no,resizable=no,width=850,height=550");
windowName.focus()//brings the popup to the front incase it opens behinds any other windows
windowName.moveTo(50, 50);//moves the popup to the coordinates supplied as parameters
}

<!--- when a window is closed using this function in its onClick() it also closed the popup window saved in windowName --->
function closePopUpWindow(){
	if(windowName && !windowName.closed){
		windowName.close();
	}
}

<!--- takes an inputstring and returns it with the first letter uppercase and the rest lowercase --->
function stringFirstUppercase(inputString) { 
//set everything to lowercase
inputString.value = inputString.value.toLowerCase();
//set the first character of inputString.value to uppercase
inputString.value.substr(0, 1).toUpperCase() +
inputString.value.substr(1);
return inputString;
}

