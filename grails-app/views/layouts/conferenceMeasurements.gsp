<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->

<html lang="en" class="no-js">
<!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>
			<g:layoutTitle default="Grails" />
		</title>
		
        <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" type="text/css">

        <style type="text/css">
            body {
                width: 710px; <!-- Override fixed witdh of parent style-->
                height: 100%;
                padding-bottom: 5px;
                margin: auto;
            }

            span {
                display: block;
                color: #397391;
                text-align:center;
            }

            #patient_cpr {
                font-weight: 100;
                font-size: 0.8em;
            }

            table {
                margin-right: auto;
                margin-left: auto;
                margin-top: 10px;
                margin-bottom: 10px;

                border:#93b8ce 1px solid;
                border-collapse:collapse;

                font-weight: 300;
                font-size: 1em;
            }

            table th {
                background: #f1f1f1;

                border-bottom:1px solid #e0e0e0;
                padding:21px 25px 22px 25px;

                color: #397391;
                font-weight: 300;
                font-size: 1.15em;
                text-align: center;
            }

            table td {
                border-bottom:1px solid #e0e0e0;
                padding:11px 5px 12px 5px;

                color: #397391;
                text-align:center;
            }

            tr:nth-child(even) {
                background: #f1f1f1;
            }

            tr:nth-child(odd) {
                background-: #fafafa;
            }

            table tr:hover {
                background: #d0e3ee;
            }

            table td span {
                display: inline-block;
                float: right;
                /*IE7 hack*/
                float: none !ie;
            }

            table td span div {
                display: block;
                /*IE7 hack*/
                width: 100% !ie;
                float: right;
                clear: right;
            }

            table td input #included {
                margin-left: auto;
                margin-right: auto;
            }

            table tfoot td {
                background: #d0e3ee;
                text-align: right;
                border-bottom: #93b8ce 1px solid;
            }

            input.error {
                background: red
            }

            input.warning {
                background: yellow
            }
        </style>
        
        <g:javascript src="jquery-1.9.1.min.js" />

        <r:script>
            // Make sure that user is never logged out when entering measurement drafts
            setInterval(function() { $.ajax({ url: "${g.createLink(controller: 'conferenceMeasurements', action: 'keepAlive')}" }); }, 30000);
        </r:script>

		<g:layoutHead />
		<r:layoutResources />
		<tooltip:resources />
	</head>
	<body>
		<g:layoutBody />
        <r:layoutResources />
	</body>
</html>
