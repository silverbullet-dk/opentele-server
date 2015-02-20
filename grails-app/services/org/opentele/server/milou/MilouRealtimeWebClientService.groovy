package org.opentele.server.milou
import org.apache.http.HttpEntity
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.opentele.server.model.RealTimectg

class MilouRealtimeWebClientService {
    def grailsApplication

    boolean sendRealtimeCTGMeasurement(RealTimectg measurement) {

        HttpEntity entity = null;
        try {
            entity = new StringEntity(measurement.getXMLAsString(), "UTF-8")
        } catch (UnsupportedEncodingException e) {
            return false
        }

        HttpPost httpPost = new HttpPost(grailsApplication.config.milou.realtimectg.url);
        httpPost.setEntity(entity);
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("SOAPAction", measurement.soapAction);

        HttpClient httpClient = new DefaultHttpClient()

        try {
            def response = httpClient.execute(httpPost, new BasicResponseHandler())
        } catch (Exception ex) {
            log.warn("Failed to send realtimeCTG measurement: ${measurement.id} to ${grailsApplication.config.milou.realtimectg.url}", ex)
            return false
        }

        return true
    }
}
