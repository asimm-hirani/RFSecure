package webio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class RFSecureClient {

    static String urlSensor =
         "http://77a6cdd1.ngrok.io/sensor";
    static String urlMotion =
            "http://77a6cdd1.ngrok.io/motion";

    public static void logEvent(String sensorID, String keyID) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(urlSensor);
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("sensorID",sensorID));
            nvps.add(new BasicNameValuePair("keyID",keyID));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
            System.out.println(httpPost);
            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }
        } finally {
            httpclient.close();
        }
    }
    public static void logMovementEvent(String sensorID) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(urlMotion);
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("sensorID",sensorID));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
            System.out.print(httpclient);
            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
