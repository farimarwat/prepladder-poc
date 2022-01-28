package com.mamba.prepladder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mamba.prepladder.Helper.CommonForVolley;
import com.mamba.prepladder.Helper.CryptLib;
import com.mamba.prepladder.Helper.GlobalHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = GlobalHelper.TAG;
    CryptLib mCryptLib;
    Context mContext;
    Map<String, String> mRequest;
    String mEnc_Request = "";
    TextView txt_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        txt_data = findViewById(R.id.txt_data);
        mCryptLib = new CryptLib();
        mRequest = new HashMap<>();
        mRequest.put("appName","prepladder");
        mRequest.put("platform","android");
        mRequest.put("version","168");
        mRequest.put("apiKey",CryptLib.APIKEY);
        mRequest.put("languageID","1");
        mRequest.put("email","noorgul007@gmail.com");
        mRequest.put("id","1763");
        mRequest.put("deviceManufacture","ZMobile");
        mRequest.put("uniqueDeviceID","87017a80e5e53024");
        mRequest.put("paper","0");
        mRequest.put("courseID","3");
        mRequest.put("subjectID","15");
        mRequest.put("uniqVideoID","0e23d628e140fea532e0");
        try{
            mEnc_Request = mCryptLib.encryptPlainTextWithRandomIV(new JSONObject(mRequest).toString(),CryptLib.KEY);
        }catch (Exception ex){
            Log.e(TAG,ex.toString());
        }

        //String url = "https://web.prepladder.com/v2/prepare/fetchPrepare";
        //String url = "https://web.prepladder.com/prepare/video/play";
        //String url = "https://web.prepladder.com/getTopicsOfSubjects";
        String url = "https://web.prepladder.com/v2/downloadVideo";
        //String url = "https://web.prepladder.com/v2/getNotes";
        String request = getResources().getString(R.string.request);
        Map<String,String> map = new HashMap<>();
        map.put("enc_request",mEnc_Request);
        JSONObject object = new JSONObject(map);
        String[] values = {url,object.toString()};

        LoadData loadData = new LoadData();
        loadData.execute(values);

        try{
            String decrypted = mCryptLib.decryptCipherTextWithRandomIV(request,CryptLib.KEY);
            //Log.e(TAG,decrypted);
        }catch (Exception ex){

        }
    }
    public static void longLog(String str) {
        if (str.length() > 4000) {
            Log.e(TAG, str.substring(0, 4000));
            longLog(str.substring(4000));
        } else
            Log.e(TAG, str);
    }


    public class LoadData extends AsyncTask<String,Void,String>{
        String str_url, str_data;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... values) {
            str_url = values[0];
            str_data = values[1];
             MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");
            try{
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, str_data);
                Request request = new Request.Builder()
                        .url(str_url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception ex){
                Log.e(TAG,ex.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null){
                String enc_response = "";
                try{
                    String decrypted =  "";
                    JSONObject object = new JSONObject(s);
                    if(object.has("enc_response")){
                        enc_response = object.getString("enc_response");
                        decrypted = mCryptLib.decryptCipherTextWithRandomIV(enc_response,CryptLib.KEY);
                        Log.e("prepladder",decrypted);
                        JSONObject obj = new JSONObject(decrypted);
                        /*JSONArray values = obj.getJSONArray("values");
                        JSONObject vitem = values.getJSONObject(0);
                        String newCdn = vitem.getString("newCdn");
                        String newCdnCkc = vitem.getString("newCdnCkc");
                        String bcAccount = vitem.getString("bcAccount");
                        String bcVideoId = vitem.getString("bcVideoID");
                        String bcPolicy = vitem.getString("bcPolicy");
                        String cdn = vitem.getString("cdn");
                        String cdnCkc = vitem.getString("cdnCkc");
                        String authValue = vitem.getString("authValue");
                        String authHeader = vitem.getString("authHeader");
                        newCdn = CommonForVolley.GetData(newCdn,CryptLib.APIKEY.substring(0,32));
                        newCdnCkc = CommonForVolley.GetData(newCdnCkc,CryptLib.APIKEY.substring(0,32));
                        bcAccount = CommonForVolley.GetData(bcAccount,CryptLib.APIKEY.substring(0,32));
                        bcVideoId = CommonForVolley.GetData(bcVideoId,CryptLib.APIKEY.substring(0,32));
                        bcPolicy = CommonForVolley.GetData(bcPolicy,CryptLib.APIKEY.substring(0,32));
                        cdn = CommonForVolley.GetData(cdn,CryptLib.APIKEY.substring(0,32));
                        cdnCkc = CommonForVolley.GetData(cdnCkc,CryptLib.APIKEY.substring(0,32));
                        authValue = CommonForVolley.GetData(authValue,CryptLib.APIKEY.substring(0,32));
                        authHeader = CommonForVolley.GetData(authHeader,CryptLib.APIKEY.substring(0,32));
                        JSONArray resolutions = obj.getJSONArray("videoResolutions");
                        Log.e(TAG,"NewCdn: "+newCdn);
                        Log.e(TAG,"NewCdnCkc: "+newCdnCkc);
                        Log.e(TAG,"bcAccount: "+bcAccount);
                        Log.e(TAG,"bcVideoId: "+bcVideoId);
                        Log.e(TAG,"bcPollicy: "+bcPolicy);
                        Log.e(TAG,"cdn: "+cdn);
                        Log.e(TAG,"cdnCkc: "+cdnCkc);
                        Log.e(TAG,"authValue: "+authValue);
                        Log.e(TAG,"authHeader: "+authHeader);
                        Log.e(TAG,"===========================");
                        for(int i = 0; i < resolutions.length(); i++){
                            JSONObject video = resolutions.getJSONObject(i);
                            String name = video.getString("name");
                            name = CommonForVolley.GetData(name,CryptLib.APIKEY.substring(0,32));
                            Log.e(TAG,"Name: "+name);
                            String value = video.getString("value");
                            value = CommonForVolley.GetData(value,CryptLib.APIKEY.substring(0,32));
                            Log.e(TAG,"Value: "+value);
                        }*/
                        printObject(obj);
                        txt_data.setText(decrypted);
                    }
                }catch (Exception ex){
                    Log.e(TAG,ex.toString());
                }
            } else {
                Log.e(TAG,"Response is null");
            }
        }
    }
    public void printObject(JSONObject object){
        JSONArray keys = object.names ();
        try{
            for (int i = 0; i < keys.length (); i++) {

                String key = keys.getString (i); // Here's your key
                String value = object.getString (key); // Here's your value
                if(value.contains("/") || value.contains("=")){
                    value = CommonForVolley.GetData(value,CryptLib.APIKEY.substring(0,32));
                }
                Log.e(TAG,key+": "+value);
            }
        }catch (Exception ex){
            Log.e(TAG,ex.toString());
        }
    }
}