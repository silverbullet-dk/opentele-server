<script type="text/javascript">

	function printdiv(printarea) {
		var headstr = "<html><head><title></title></head><body><div>";
		var patientInfo = $("div#namebox")[0].innerHTML;
		var footstr = "</div></body></html>";
		var newstr = $(printarea)[0].innerHTML;
		var oldstr = document.body.innerHTML;
		document.body.innerHTML = headstr+patientInfo+newstr+footstr;
        $("body div")[0].setAttribute("class", "content")
		$(".buttons")[0].setAttribute("style", "visibility:hidden;")
		window.print();
		$(".buttons")[0].setAttribute("style", "visibility:visible")
		document.body.innerHTML = oldstr;
		return false
	}
	
</script>


<g:actionSubmit class="print" value="Print" onclick="printdiv('.scaffold-show');return false;" />
