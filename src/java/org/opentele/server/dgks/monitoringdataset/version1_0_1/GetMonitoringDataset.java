package org.opentele.server.dgks.monitoringdataset.version1_0_1;

import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.GetMonitoringDatasetRequestMessage;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
"getMonitoringDatasetRequestMessage"
})
@XmlRootElement(name = "GetMonitoringDataset", namespace = "urn:oio:medcom:monitoringdataset:1.0.1")
public class GetMonitoringDataset {
	
    @XmlElement(name = "GetMonitoringDatasetRequestMessage", namespace = "urn:oio:medcom:monitoringdataset:1.0.1", required = true)
    protected GetMonitoringDatasetRequestMessage getMonitoringDatasetRequestMessage;
	
    public GetMonitoringDatasetRequestMessage getGetMonitoringDatasetRequestMessage() {
        return getMonitoringDatasetRequestMessage;
    }
	
    public void setGetMonitoringDatasetRequestMessage(GetMonitoringDatasetRequestMessage m) {
        this.getMonitoringDatasetRequestMessage = m;
    }
	
}
