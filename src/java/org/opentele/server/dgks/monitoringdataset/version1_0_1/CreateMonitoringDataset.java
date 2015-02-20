package org.opentele.server.dgks.monitoringdataset.version1_0_1;

import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.CreateMonitoringDatasetRequestMessage;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "createMonitoringDatasetRequestMessage"
})
@XmlRootElement(name = "CreateMonitoringDataset", namespace = "urn:oio:medcom:monitoringdataset:1.0.1")
public class CreateMonitoringDataset {

    @XmlElement(name = "CreateMonitoringDatasetRequestMessage", namespace = "urn:oio:medcom:monitoringdataset:1.0.1", required = true)
    protected CreateMonitoringDatasetRequestMessage createMonitoringDatasetRequestMessage;

    public CreateMonitoringDatasetRequestMessage getCreateMonitoringDatasetRequestMessage() {
        return createMonitoringDatasetRequestMessage;
    }

    public void setCreateMonitoringDatasetRequestMessage(CreateMonitoringDatasetRequestMessage m) {
        this.createMonitoringDatasetRequestMessage = m;
    }
}
