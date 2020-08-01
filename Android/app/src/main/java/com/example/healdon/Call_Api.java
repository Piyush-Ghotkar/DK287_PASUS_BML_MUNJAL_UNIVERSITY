package com.example.apitest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String url="https://titanic-flask-model-08.herokuapp.com";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textview =(TextView) findViewById(R.id.txtview);
        Button button =findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    void postRequest() throws IOException {
        OkHttpClient client = new OkHttpClient();

        String pclass="1",age="25",sibsp="1",fare="500";

        String postBody="{\n" +
                "    \"Pclass\": "+pclass+",\n" +
                "    \"Age\": "+age+",\n" +
                "    \"SibSp\": "+sibsp+",\n" +
                "    \"Fare\": "+fare+"\n" +
                "}";

        RequestBody body = RequestBody.create(JSON, postBody);
        Log.d("tag","postBody json:" +postBody);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(body);
        Request request = builder
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAG",response.body().string());
            }
        });
    }
}
