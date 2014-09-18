
package com.example.temp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView tv;
    EditText et1, et2, et3;
    Button btn;
    private HttpClient httpclient;
    private HttpPost httppost;
    private HttpGet httpget;
    private String _url = "http://krass.cu.cc";
    private String params = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                params += "";
                PostRequest pr = new PostRequest(_url, params);
                pr.execute();
                try {
                    Show(pr.get());
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

    }

    private void Show(String answer) {
        if (answer == null)
            answer = "";
        tv.setText(answer);
    }

    private class PostRequest extends AsyncTask<Void, Void, String> {

        String url, params;

        PostRequest(String url, String params) {

            this.url = url;
            this.params = params;
        }

        protected String doInBackground(Void... par) {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(this.url);
            httpget = new HttpGet(this.url);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();

            String[] params_array = this.params.split("[&]");
            String[] param;

            // CYSendUtil.i(this, "url = " + this.url);
            for (String elem : params_array) {
                param = elem.split("[=]");
                if (param == null || param.length == 0) {
                    continue;
                }
                if (param.length == 1) {
                    // parameters.add(new BasicNameValuePair(param[0], ""));
                    continue;
                }
                if (param.length == 2) {
                    String key = param[0];
                    String value = param[1];
                    try {
                        key = java.net.URLDecoder.decode(key, "UTF-8");
                        value = java.net.URLDecoder.decode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    parameters.add(new BasicNameValuePair(key, value));
                    // CYSendUtil.i(this, key + " = " + value);
                }
            }

            try {
                httppost.setEntity(new UrlEncodedFormEntity(parameters));
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            HttpResponse response = null;
            try {
                response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String responseString = new String();
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                try {
                    responseString = EntityUtils.toString(responseEntity);
                    if (responseString == null || responseString.equals("")) {
                        HttpResponse response2 = httpclient.execute(httpget);
                        HttpEntity httpEntity = response2.getEntity();
                        String line = EntityUtils.toString(httpEntity, "UTF-8");
                        Log.d("APILog", line);

                    }

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            // String body = CYApi.readHttpResponse(response);

            return responseString;
        }
    }
}
