import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class RFSecureClient {

     static String url =
         "http://www.rfsecure.org/api/";
    String username;
    String password;

    public int loginUser() {

        //Instantiate an HttpClient
        HttpClient client = new HttpClient();

        //Instantiate a POST HTTP method
        PostMethod method = new PostMethod(url);
        method.setRequestHeader("Content-type",
                "text/xml; charset=ISO-8859-1");

        //Define name-value pairs to set into the QueryString
        NameValuePair nvp1= new NameValuePair("username",username);
        NameValuePair nvp2= new NameValuePair("password",password);

        method.setQueryString(new NameValuePair[]{nvp1,nvp2});

        try{
            int statusCode = client.executeMethod(method);

            System.out.println("Status Code = "+statusCode);
            System.out.println("QueryString>>> "+method.getQueryString());
            System.out.println("Status Text>>>"
                  +HttpStatus.getStatusText(statusCode));

            //Get data as a String
            return Integer.parseInt(method.getResponseBodyAsString());

            //release connection
            method.releaseConnection();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public int checkInVisitor(String fName,
        String lName, String id, String keyID) {

        HttpClient client = new HttpClient();

        //Instantiate a POST HTTP method
        PostMethod method = new PostMethod(url);
        method.setRequestHeader("Content-type",
                "text/xml; charset=ISO-8859-1");

        //Define name-value pairs to set into the QueryString
        NameValuePair nvp1= new NameValuePair("username",username);
        NameValuePair nvp2= new NameValuePair("password",password);

        method.setQueryString(new NameValuePair[]{nvp1,nvp2});

        try{
            int statusCode = client.executeMethod(method);

            System.out.println("Status Code = "+statusCode);
            System.out.println("QueryString>>> "+method.getQueryString());
            System.out.println("Status Text>>>"
                  +HttpStatus.getStatusText(statusCode));

            //Get data as a String
            return Integer.parseInt(method.getResponseBodyAsString());

            //release connection
            method.releaseConnection();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
