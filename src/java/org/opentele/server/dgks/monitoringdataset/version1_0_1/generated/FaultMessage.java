
package org.opentele.server.dgks.monitoringdataset.version1_0_1.generated;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.6.8
 * 2013-06-10T21:57:19.156+02:00
 * Generated source version: 2.6.8
 */

@WebFault(name = "Fault", targetNamespace = "urn:oio:medcom:chronicdataset:1.0.0")
public class FaultMessage extends Exception {
    
    private FaultType fault;

    public FaultMessage() {
        super();
    }
    
    public FaultMessage(String message) {
        super(message);
    }
    
    public FaultMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public FaultMessage(String message, FaultType fault) {
        super(message);
        this.fault = fault;
    }

    public FaultMessage(String message, FaultType fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public FaultType getFaultInfo() {
        return this.fault;
    }
}
