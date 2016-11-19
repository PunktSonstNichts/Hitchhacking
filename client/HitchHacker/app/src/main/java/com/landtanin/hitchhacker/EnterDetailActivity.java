package com.landtanin.hitchhacker;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.landtanin.hitchhacker.databinding.ActivityEnterDetailBinding;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.common.net.HttpHeaders.USER_AGENT;

public class EnterDetailActivity extends AppCompatActivity {

    ActivityEnterDetailBinding binding;

    private String strUserName, strPassWord;

    private String resultServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_enter_detail);

        strUserName = String.valueOf(binding.editTxtUsrName.getText());
        strPassWord = String.valueOf(binding.editTxtPassWord.getText());

        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connectDatabase();

//                new AsyncTask<Void, Void, String>() {
//
//                    @Override
//                    protected String doInBackground(Void... voids) {
//
//                        try {
//                            sendPost();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        return null;
//                    }
//
//
//                }.execute();

                Toast.makeText(EnterDetailActivity.this, "onClick", Toast.LENGTH_SHORT).show();

            }
        });

    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "http://10.232.29.59/Hitchhacking/Hitchhacking/server/accessUsers.php";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "anything";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Post parameters : " + urlParameters);
//        System.out.println("Response Code : " + responseCode);

        Log.d("1","Sending 'POST' request to URL : " + url);
        Log.d("2","Post parameters : " + urlParameters);
        Log.d("3","Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
//        System.out.println(response.toString());
        Log.d("RESULT", response.toString());

    }

    private void connectDatabase() {

//        int getSendKey = getIntent().getExtras().getInt(KeyStore.SELECT_FOOD_SEND_KEY);

        final HttpUrl myurl = HttpUrl.parse(JSONObtained.getAbsoluteUrl("accessUsers.php")).newBuilder().build();
//                addQueryParameter("id_typefood", String.valueOf(getSendKey)).build();

        // TODO; create JSON string
//        JSONObject userInfoJSONStr = new JSONObject();
//        FormBody formBody; // ??????

        final RequestBody formBody = new FormBody.Builder()
                .add("f", "loginUser")
                .add("email", strUserName)
                .add("password", strPassWord).build();

//        try {
//            userInfoJSONStr.put("id", 1);
//            userInfoJSONStr.put("email", strUserName);
//            userInfoJSONStr.put("password", strPassWord);
//            userInfoJSONStr.put("api","1f512e4b98c0077be4b1365c64b182f8");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

//                    Response response = JSONObtained.getInstance().newCall(JSONObtained.getRequest(myurl)).execute();
                Response response = null;

                try {
                    response = JSONObtained.getInstance().newCall(JSONObtained.postRequest(myurl, formBody)).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Log.d("CONNECTDATABASE", String.valueOf(response));

                return null;
            }

            @Override
            protected void onPostExecute(String s) {

//                recyclerAdapter.notifyDataSetChanged();

                super.onPostExecute(s);
            }

        }.execute();

    }
}
