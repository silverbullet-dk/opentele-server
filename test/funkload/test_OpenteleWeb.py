# -*- coding: iso-8859-15 -*-
"""opentele_web FunkLoad test

$Id: $
"""
import unittest
from funkload.FunkLoadTestCase import FunkLoadTestCase
from webunit.utility import Upload
from funkload.utils import Data
#from funkload.utils import xmlrpc_get_credential

class OpenteleWeb(FunkLoadTestCase):
    """XXX

    This test use a configuration file OpenteleWeb.conf.
    """

    def setUp(self):
        """Setting up test."""
        self.logd("setUp")
        self.server_url = self.conf_get('main', 'url')
        # XXX here you can setup the credential access like this
        # credential_host = self.conf_get('credential', 'host')
        # credential_port = self.conf_getInt('credential', 'port')
        # self.login, self.password = xmlrpc_get_credential(credential_host,
        #                                                   credential_port,
        # XXX replace with a valid group
        #                                                   'members')
    def test_opentele_rest(self):
	server_url = self.server_url
        self.setBasicAuth('NancyAnn', 'NancyAnn')

        self.setHeader('Content-Type', 'application/json')
	self.get(server_url + "/opentele-server/rest/questionnaire/download/43")
	self.clearBasicAuth()
	self.clearHeaders()

    def test_opentele_web(self):
        # The description should be set in the configuration file
        server_url = self.server_url
        # begin of test ---------------------------------------------

        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0001.request
        #self.get(server_url + "/opentele-server/",
        #    description="Get /opentele-server/")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0008.request
        self.post(server_url + "/opentele-server/j_spring_security_check", params=[
            ['j_username', 'HelleAndersen'],
            ['j_password', 'HelleAndersen']],
            description="Post /opentele-server/j_spring_security_check")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0067.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0078.request
        self.get(server_url + "/opentele-server/patient/questionnaires/10",
            description="Get /opentele-server/patient/questionnaires/10")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0107.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0108.request
        self.get(server_url + "/opentele-server/patient/show/10",
            description="Get /opentele-server/patient/show/10")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0128.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0129.request
        self.get(server_url + "/opentele-server/patient/messages/10",
            description="Get /opentele-server/patient/messages/10")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0148.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0149.request
        self.get(server_url + "/opentele-server/patientNote/list/10",
            description="Get /opentele-server/patientNote/list/10")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0167.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0168.request
        self.get(server_url + "/opentele-server/patient/10/measurements",
            description="Get /opentele-server/patient/10/measurements")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0204.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0205.request
        self.get(server_url + "/opentele-server/patient/equipment/10",
            description="Get /opentele-server/patient/equipment/10")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0224.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0225.request
        self.get(server_url + "/opentele-server/monitoringPlan/show/10",
            description="Get /opentele-server/monitoringPlan/show/10")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0244.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0245.request
        self.get(server_url + "/opentele-server/",
            description="Get /opentele-server/")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0296.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0297.request
        self.get(server_url + "/opentele-server/patient/search",
            description="Get /opentele-server/patient/search")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0315.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0316.request
        self.post(server_url + "/opentele-server/patient/search", params=[
            ['ssn', ''],
            ['phone', ''],
            ['firstName', 'Linda'],
            ['lastName', ''],
            ['status', 'ACTIVE'],
            ['checked', ''],
            ['typeid', ''],
            ['_action_search', 'Find patient']],
            description="Post /opentele-server/patient/search")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0332.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0333.request
        self.get(server_url + "/opentele-server/patient/questionnaires/1",
            description="Get /opentele-server/patient/questionnaires/1")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0355.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0356.request
        self.get(server_url + "/opentele-server/patient/show/1",
            description="Get /opentele-server/patient/show/1")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0376.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0377.request
        self.get(server_url + "/opentele-server/patient/1/measurements",
            description="Get /opentele-server/patient/1/measurements")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0412.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0413.request
        self.get(server_url + "/opentele-server/patient/equipment/1",
            description="Get /opentele-server/patient/equipment/1")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0431.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0432.request
        self.get(server_url + "/opentele-server/",
            description="Get /opentele-server/")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0483.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0484.request
        self.get(server_url + "/opentele-server/auditLogEntry/list",
            description="Get /opentele-server/auditLogEntry/list")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0499.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0500.request
        self.get(server_url + "/opentele-server/role/list",
            description="Get /opentele-server/role/list")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0515.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0516.request
        self.get(server_url + "/opentele-server/patientGroup/list",
            description="Get /opentele-server/patientGroup/list")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0531.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0532.request
        self.get(server_url + "/opentele-server/monitorKit/list",
            description="Get /opentele-server/monitorKit/list")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0547.request
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0548.request
        #self.get(server_url + "/opentele-server/logout/index",
        #    description="Get /opentele-server/logout/index")
        # /var/folders/t3/32mqxjjs6pddb_yjpt_6clmr0000gn/T/tmp6eJa8l_funkload/watch0554.request

        # end of test -----------------------------------------------

    def tearDown(self):
        """Setting up test."""
        self.logd("tearDown.\n")



if __name__ in ('main', '__main__'):
    unittest.main()
