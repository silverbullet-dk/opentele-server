<%@ page import="grails.util.Environment; org.opentele.server.provider.constants.Constants; org.opentele.server.core.model.types.PermissionName" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6 ie"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7 ie"> <![endif]-->
<!--[if (gt IE 10)|!(IE)]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>
			<g:layoutTitle default="OpenTele" />
		</title>

		<meta name="viewport" content="width=device-width, initial-scale=1.0">

		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}">
        %{--New design--}%
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery-ui.custom.css')}" type="text/css"/>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'font-awesome.min.css')}"/>
        <g:javascript src="layout.engine.min.js"/>
        <g:javascript src="jquery.js" />
        <g:javascript src="jquery-ui/jquery-ui-1.10.1.min.js"/>
        <g:javascript src="underscore-min.js"/>
        <g:javascript src="jquery-ui/jquery.ui.datepicker-da.js"/>
        <g:javascript src="jquery.openteleDatePicker.js"/>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.optenteleDatePicker.css')}" type="text/css">
        <g:javascript src="firebugx.js"/>
        <g:javascript src="modernizr.js"/>
		<g:layoutHead />
		<r:layoutResources />
		<tooltip:resources />
    </head>
	<body>
		<div id="header">
			<div id="logo">
				<a href="${createLink(uri: '/')}">
					<img src="${resource(dir: 'images', file: 'logo_horizontal.png')}" alt="OpenTele"/>
				</a>
			</div>
            <div id="userinfo">
                <!-- Show 'logged in as..' -->
                <div id="loggedInAsLabel" class="nav">
                    <sec:ifLoggedIn>
                        <g:message code="loggedInAs" />
                        <sec:username />
                    </sec:ifLoggedIn>
                </div>
                <!-- Logud knap - uafhaengigt af rolle -->
                <div class="buttons nav">
                    <a class="buttons" style="float: right; width: 60px;" href="${createLink(controller: "logout", action:"index") }">
                        <g:message code="logOut" />
                    </a>
                </div>
                <!-- Edit user button -->
                <sec:ifAnyGranted roles="${PermissionName.CLINICIAN_CHANGE_PASSWORD}">
                    <div class="buttons nav">
                        <g:link class="buttons" style="float: right; width: 60px;" controller="password" action="change">
                            <g:message code="editUserProfile" />
                        </g:link>
                    </div>
                </sec:ifAnyGranted>
            </div>
        </div>

        <tmpl:/layouts/menu/leftmenu/>

    <!-- Page content -->
        <div id="content">
            <g:layoutBody />
        </div>

        <div id="footer">
            <div class="version">
                Version ${grailsApplication.metadata['app.version']} (${grails.util.Environment.getCurrent()?.getName()})
            </div>
        </div>

        <r:layoutResources />

        <!-- Set .selected class on active menu item-->
        <script type="text/javascript">
            $(function() {
                $('div#leftmenu li').each(function() {
                    var href = $(this).find('a').attr('href');
                    if (href.indexOf("?") > 0) {
                        href = href.substring(0, href.indexOf("?"))
                    }
                    if (href === window.location.pathname) {
                        $(this).addClass('selected');
                    }
                });
                $('body').on('mouseover','*[data-tooltip]', function() {
                    tooltip.show($(this).attr('data-tooltip'))
                }).on('mouseout', '*[data-tooltip]', function() {
                    tooltip.hide()
                });
                $(":input[type!='hidden'][type!='submit'][type!='button'][type!='file']:first").focus();
            })
        </script>
        <g:if test="${Environment.current == Environment.DEVELOPMENT}">
            <miniprofiler:javascript/>
        </g:if>
	</body>
</html>
