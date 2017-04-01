package webio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RFSecureClient {
    public static final String apiURL = "www.myrfsecure.com/api/"

    private String username;
    private String password;

    private boolean isAuth = false;

    public RFSecureClient(String username, String password) {
        this.username = username;
        this.password = password;
    }



    public int loginUser(String username, String password) {
        this.username = username;
        this.password = password;


        String urlParm = "username="+username+",password="+password;

        URL obj = new URL(apiURL);
        HttpResponse response = HttpRequest
          .create(obj)
          .body(fromString(urlParm))
          .POST().response();

        return Integer.parseInt(response.toString());

    }


    public void addVisitor(String fName, String lName, String id, String token) {

    }

}
