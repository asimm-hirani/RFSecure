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

     static String url =
         "http://www.rfsecure.org/api/";
    String username;
    String password;

    public int loginUser() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("username", username));
            nvps.add(new BasicNameValuePair("password", password));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

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

    public int checkInVisitor(String fName,
        String lName, String id, String keyID) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("username", username));
            nvps.add(new BasicNameValuePair("password", password));
            nvps.add(new BasicNameValuePair("firstName",fName));
            nvps.add(new BasicNameValuePair("lName",lName));
            nvps.add(new BasicNameValuePair("id",id));
            nvps.add(new BasicNameValuePair("keyID",keyID));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

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
