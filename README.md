OpenTele Server
===============
[Grails](http://grails.org/)-based server which provides the back-end for the client and a clinical GUI.

How to build and run
--------------------
Make sure you have JDK 1.6 and Grails 2.1 installed and in your path.

First, clone the KIH Auditlog project (a separate project) and install the plug-in in your local repository.

After that, start the server in development mode by issuing

    grails run-app

from the command line. Run the unit, integration, and functional tests by issuing

    grails test-app

from the command line.

Test set-up
-----------
Have a look in [BootStrap.groovy](grails-app/conf/BootStrap.groovy) for all details on the set-up when
started in development mode. In short, a number of test clinicians are created (in method
``createCliniciansForTest``):

* Helle Andersen (user name ``HelleAndersen``, password ``HelleAndersen1``)
* Jens Hansen (user name: ``JensHansen``, password: ``JensHansen1``)
* Doktor Hansen (user name: ``DoktorHansen``, password: ``DoktorHansen1``)

And a number of test patients are created (in method ``createTestPatients``), amongst others:

* Nancy Ann Berggreen (user name ``NancyAnn``, password ``abcd1234``)
* Linda Hansen (user name ``Linda``, password ``abcd1234``)
* Erna Hansen (user name ``Erna``, password ``abcd1234``)
* Else Nielsen (user name ``Else``, password ``abcd1234``)

Included 3rd-party software
---------------------------
OpenTele Server includes the following 3rd-party software:

* [JQuery](http://jquery.com/), [license](licenses/jquery-license.txt)
* [JQPlot](http://www.jqplot.com/), [MIT License](licenses/jqplot-license.txt)
* [JQuery-UI](http://jqueryui.com/), [license](licenses/jquery-ui-license.txt)
* [JQuery-Popupwindow](http://swip.codylindley.com/popupWindowDemo.html)
* [Twitter Bootstrap](http://getbootstrap.com/), [Apache 2 License](licenses/twitter-bootstrap-license.txt). Includes [Glyphicons](http://glyphicons.com/).
* [jsPlumb](http://jsplumbtoolkit.com/home/jquery.html), [MIT License](licenses/jsplumb-license.txt)
* json2
* FirebugX
* [Knockout](http://knockoutjs.com/), [MIT License](http://www.opensource.org/licenses/mit-license.php)
* [Modernizr](http://modernizr.com/), [MIT License](licenses/modernizr-license.txt)
* [Underscore](http://underscorejs.org/), [MIT License](licenses/underscore-license.txt)
* [Font Awesome](http://fortawesome.github.io/Font-Awesome/), [MIT License](http://opensource.org/licenses/mit-license.html)

Please note
----------------------
This repository is maintained exclusively by Silverbullet. The official version maintained by 4S can be found [here](https://bitbucket.org/4s/opentele-server).