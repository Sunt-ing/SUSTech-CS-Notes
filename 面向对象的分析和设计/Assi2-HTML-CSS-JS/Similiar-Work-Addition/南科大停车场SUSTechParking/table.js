var records=new Array();         //先声明一维

function DuplicateCheck(licenseNo,year,month,day,ahour,aminute){
    for(var i=0;i<records.length;i++){
        if(records[i][0]==licenseNo && records[i][1]==year && records[i][2]==month
        && records[i][3]==day && records[i][4]==ahour && records[i][5]==aminute){
            alert("You have added similar record.");
            return false;
        }
    }
    var i=records.length;
    records[i]=new Array();
    records[i][0]=licenseNo;
    records[i][1]=year;
    records[i][2]=month;
    records[i][3]=day;
    records[i][4]=ahour;
    records[i][5]=aminute;
    return true;
}

function dispSheet(){
    document.getElementById("table1").style.display="";//显示
}

function hideSheet(){
    document.getElementById("table1").style.display="none";//隐藏
}

function onClickAddFlight()	{
    let licenseNo	= document.querySelector('form	input[name="License Plate Number"]').value;
    let EntranceNo	= document.querySelector('form	input[name="Entrance Number"]').value;
    let StaffNo	= document.querySelector('form	input[name="Staff Number"]').value;

    let year	= document.getElementById("year").value;
    let month	= document.getElementById("month").value;
    let day	= document.getElementById("day").value;
    let dhour	= document.getElementById("dhour").value;
    let dminute	= document.getElementById("dminute").value;
    let ahour	= document.getElementById("ahour").value;
    let aminute	= document.getElementById("aminute").value;
    let StaffName = document.forms[0]["Staff Name"].value;
    let Status = document.forms[0]["Status"].value;

    if (!notNullTest(licenseNo,EntranceNo,month,day,StaffName,StaffNo)){
        return false;
    }
    if(Status=="Out" && ( (ahour>dhour) || ((ahour==dhour) && (aminute>=dminute)) )){
        alert("Do you want to chrono cross?");
        return false;
    }
    if (validateInput(licenseNo, EntranceNo, StaffNo) && DuplicateCheck(licenseNo,year,month,day,ahour,aminute) )	{
        addRow();
    }
}

function notNullTest(licenseNo,EntranceNo,month,day,StaffName,StaffNo){
    if(licenseNo==""){
        alert("Please fill license plate number.");
        return false;
    }

    if(EntranceNo==""){
        alert("Please fill entrance number.");
        return false;
    }

    if(StaffNo==""){
        alert("Please fill staff number.");
        return false;
    }

    if(month==""){
            alert("Please fill month.");
            return false;
    }

    if(day==""){
            alert("Please fill day.");
            return false;
    }

    if(StaffName==""){
            alert("Please fill StaffName.");
            return false;
    }

    if(StaffNo==""){
            alert("Please fill StaffNo.");
            return false;
    }
    return true;
}

function validateInput(licenseNo, EntranceNo, StaffNo)	{
    let licenseNoRegex	= new RegExp( /^[DF]?[A-Z0-9]{5}/ );
    let EntranceNoRegex	= new RegExp(/[1-7]/);
    let StaffNoRegex	= new RegExp(/[35][0-9]{7}/);

    if (!licenseNoRegex.test(licenseNo))	{
        alert("Invalid	license plate number.");
        return false;
    }

    if (!EntranceNoRegex.test(EntranceNo))	{
        alert("Invalid	entrance number.");
        return false;
    }

    if (!StaffNoRegex.test(StaffNo))	{
        alert("Invalid	staff number.");
        return false;
    }

    return true;
}


function initial()	{
    let year	= document.getElementById("year");
    year.innerHTML = "";
    year.options.add(new Option("--", null));
    for (let i	= 2000; i	<= 2020; i++)	{
        year.options.add(new Option(i, i));
    }

    let ahour	= document.getElementById("ahour");
    ahour.innerHTML = "";
    for (let i	= 0; i	<= 23; i++)	{
        ahour.options.add(new Option(i, i));
    }

    let aminute	= document.getElementById("aminute");
    aminute.innerHTML = "";
    for (let i	= 0; i	<= 59; i++)	{
        aminute.options.add(new Option(i, i));
    }

    let Status	= document.getElementById("Status");
    Status.innerHTML = "";
    Status.options.add(new Option("In"));
    Status.options.add(new Option("Out"));
}

function setMonth()	{
    let month	= document.getElementById("month");
    month.innerHTML = "";
    month.options.add(new Option("--", null));
    for (let i	= 1; i	<= 12; i++)	{
        month.options.add(new Option(i, i));
    }
}

function setDay()	{
    let year	= document.getElementById("year").value;
    let month	= document.getElementById("month").value;
    let day	= document.getElementById("day");
    let data	= new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    //	clear	the	items
    day.innerHTML = "";
    //	add	new	items
    day.options.add(new Option("--", null));
    for (let i	= 1; i	<= data[month	- 1]; i++)	{
        day.options.add(new Option(i, i));
    }
    if (((year	% 4 == 0 && year	% 100 != 0)	|| year	% 400 == 0)	&& month	== 2)	{
        day.options.add(new Option(29, 29));
    }
}

function setDepartmentTime() {
    let dhour	= document.getElementById("dhour");
    dhour.innerHTML = "";
    for (let i	= 0; i	<= 23; i++)	{
        dhour.options.add(new Option(i, i));
    }

    let dminute	= document.getElementById("dminute");
    dminute.innerHTML = "";
    for (let i	= 0; i	<= 59; i++)	{
        dminute.options.add(new Option(i, i));
    }
}


function addRow()	{
    let bodyObj	= document.getElementById("tbody");
    if (bodyObj	=== null)	{
        alert("Body	of	Table	not	Exist!");
        return;
    }
    let year	= document.getElementById("year").value;
    let month	= document.getElementById("month").value;
    let day	= document.getElementById("day").value;
    let dhour	= document.getElementById("dhour").value;
    let dminute	= document.getElementById("dminute").value;
    let ahour	= document.getElementById("ahour").value;
    let aminute	= document.getElementById("aminute").value;
    let LicenseNo = document.forms[0]["License Plate Number"].value;
    let EntranceNo = document.forms[0]["Entrance Number"].value;
    let StaffName = document.forms[0]["Staff Name"].value;
    let StaffNo = document.forms[0]["Staff Number"].value;
    let Status = document.forms[0]["Status"].value;

    let rowCount	= bodyObj.rows.length;
    let cellCount	= bodyObj.rows[0].cells.length;
    bodyObj.style.display = ""; //	display	the	tbody
    let newRow	= bodyObj.insertRow(rowCount++);

    let departTime="--";
    if (Status=="Out"){
        if (dhour<10){
            dhour="0"+dhour;
        }
        if (dminute<10){
            dminute="0"+dminute;
        }
        departTime=dhour + ":" + dminute;
    }


    newRow.insertCell(0).innerHTML = LicenseNo;
    newRow.insertCell(1).innerHTML = EntranceNo;
    newRow.insertCell(2).innerHTML = year	+ "." + month	+ "." + day;
    newRow.insertCell(3).innerHTML = ahour	+ ":" + aminute;
    newRow.insertCell(4).innerHTML = departTime;
    newRow.insertCell(5).innerHTML = StaffName;
    newRow.insertCell(6).innerHTML = StaffNo;
    newRow.insertCell(7).innerHTML = Status;
    newRow.insertCell(8).innerHTML = bodyObj.rows[0].cells[cellCount	- 1].innerHTML; //	copy	the	"delete"	button
    bodyObj.rows[0].style.display = "none"; //	hide	first	row
 }

function removeRow(inputobj)	{
    if (inputobj	== null)	return;
    let parentTD	= inputobj.parentNode;
    let parentTR	= parentTD.parentNode;
    let parentTBODY	= parentTR.parentNode;
    parentTBODY.removeChild(parentTR);
}




