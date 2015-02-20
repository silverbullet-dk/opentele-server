
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.6.8
 * 2013-06-10T21:57:19.110+02:00
 * Generated source version: 2.6.8
 * 
 */
public final class MonitoringDatasetPortType_MonitoringDatasetPort_Client {

    private static final QName SERVICE_NAME = new QName("urn:oio:medcom:monitoringdataset:1.0.1", "MonitoringDatasetService");

    private MonitoringDatasetPortType_MonitoringDatasetPort_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = MonitoringDatasetService.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        MonitoringDatasetService ss = new MonitoringDatasetService(wsdlURL, SERVICE_NAME);
        MonitoringDatasetPortType port = ss.getMonitoringDatasetPort();  
        
        {
        System.out.println("Invoking deleteMonitoringDataset...");
        DeleteMonitoringDatasetRequestMessage _deleteMonitoringDataset_parameter = null;
        try {
            DeleteMonitoringDatasetResponseMessage _deleteMonitoringDataset__return = port.deleteMonitoringDataset(_deleteMonitoringDataset_parameter);
            System.out.println("deleteMonitoringDataset.result=" + _deleteMonitoringDataset__return);

        } catch (FaultMessage e) { 
            System.out.println("Expected exception: FaultMessage has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking createMonitoringDataset...");
        CreateMonitoringDatasetRequestMessage _createMonitoringDataset_parameter = null;
        try {
            CreateMonitoringDatasetResponseMessage _createMonitoringDataset__return = port.createMonitoringDataset(_createMonitoringDataset_parameter);
            System.out.println("createMonitoringDataset.result=" + _createMonitoringDataset__return);

        } catch (FaultMessage e) { 
            System.out.println("Expected exception: FaultMessage has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking getMonitoringDataset...");
        GetMonitoringDatasetRequestMessage _getMonitoringDataset_parameter = null;
        try {
            GetMonitoringDatasetResponseMessage _getMonitoringDataset__return = port.getMonitoringDataset(_getMonitoringDataset_parameter);
            System.out.println("getMonitoringDataset.result=" + _getMonitoringDataset__return);

        } catch (FaultMessage e) { 
            System.out.println("Expected exception: FaultMessage has occurred.");
            System.out.println(e.toString());
        }
            }

        System.exit(0);
    }

}
