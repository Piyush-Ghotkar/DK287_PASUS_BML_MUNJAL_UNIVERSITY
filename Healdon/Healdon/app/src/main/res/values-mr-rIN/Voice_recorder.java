package com.example.healdon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Voice_recorder extends AppCompatActivity {

    private MediaRecorder mRecorder;
    private String mFileName=null;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    Save_del_var save_obj=new Save_del_var();
    String u_id;
    Button language_btn;
    TextView lang_txt;

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;


    boolean x= true;
    String english_statement,hindi_statement;

    //TODO set stopwatach for audio recording, when clicked on language,

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recorder);

        language_btn=findViewById(R.id.language);
        lang_txt=findViewById(R.id.text);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        mStorage= FirebaseStorage.getInstance().getReference();
        mProgress=new ProgressDialog(this);

        u_id=save_obj.u_id;

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName+="/recorded_audio.mp3";





        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(Voice_recorder.this, "Bing!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // If we don't have permissions, ask user for permissions
        if (ActivityCompat.checkSelfPermission(Voice_recorder.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            int REQUEST_MICROPHONE = 1;
            ActivityCompat.requestPermissions(
                    Voice_recorder.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE
            );
        }


        english_statement = "An amazing thing happens when you get honest with \n" +
                "yourself and start doing what you love, what makes you \n" +
                "happy. You begin to live in each moment\n";

        lang_txt.setText(english_statement);

        hindi_statement = "चलता रहूँगा मै पथ पर\n" +
                "चलने में माहिर बन जाउंगा\n" +
                "या तो मंज़िल मिल जायेगी\n" +
                "या मुसाफिर बन जाउंगा\n" +
                "\n";


        language_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_text();
            }
        });

    }

    private void change_text() {
        if(x==true)
        {
            language_btn.setText("Hindi");
            lang_txt.setText(hindi_statement);
            x=false;
        }

        else
        {
            language_btn.setText("English");
            lang_txt.setText(english_statement);
            x=true;
        }

    }

    public void start_rec(View view) throws IOException {
//        wavRecorder.startRecording();



        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }





        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Log","prepare() failed");
        }

        mRecorder.start();
        Toast toast = Toast.makeText(Voice_recorder.this,
                "Recording Started",
                Toast.LENGTH_SHORT);

        toast.show();
    }

    public void stop_rec(View view) {
        //wavRecorder.stopRecording();


        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;


        mRecorder.stop();
        mRecorder.release();
        mRecorder =null;

        Toast toast = Toast.makeText(Voice_recorder.this,
                "Recording Stopped",
                Toast.LENGTH_SHORT);

        toast.show();
        upload_audio();
    }

    private void upload_audio() {
        mProgress.setMessage("Uploading Audio...");
        mProgress.show();


        String audio_file="555";
        StorageReference filepath=mStorage.child("audio").child(audio_file+".mp3");
        Uri uri=Uri.fromFile(new File(mFileName));

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.dismiss();
                Toast toast = Toast.makeText(Voice_recorder.this,
                        "Uploading Finished",
                        Toast.LENGTH_SHORT);
                toast.show();


                //TODO call game_prediction api & voice_prediction api while setting progressbar
                call_API();

            }

        });
    }

    private void call_API() {
        mProgress.setMessage("Processing");
        mProgress.show();
        //call_game_model();
        try {
            call_voice_model();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void call_game_model() {

//        String url="https://titanic-flask-model-08.herokuapp.com";
//        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        OkHttpClient client = new OkHttpClient();

    }

    private void call_voice_model() throws IOException{
        String url="https://voice-analysis-classifi-randfo.herokuapp.com/   ";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        String lang="1";
//        String postBody="{\n" +
//                "    \"filename\": \"Utkarsh5470\",\n" +
//                "    \"language\": \"1\",\n" +
//                "    \"u_id\": \"Utkarsh547\",\n" +
//                "}";

        String audio_file="555";
        String postBody="{\n" +
                "    \"filename\": "+audio_file+",\n" +
                "    \"language\": "+lang+",\n" +
                "}";

//        RequestBody formBody =new FormBody.Builder()
//                .add("filename",u_id)
//                .add("language","1")
//                .add("u_id",u_id)
//                .build();
//
        JSONObject student1 = new JSONObject();
        try {
            student1.put("filename", "555");
            student1.put("language", "1");
            //student1.put("u_id", "Utkarsh5470");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // put your json here
        RequestBody body = RequestBody.create(JSON, student1.toString());

        //RequestBody body = RequestBody.create(JSON, postBody);
        Log.d("tag1","postBody json:" +body);

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
                Log.d("TAG1",response.body().string());
                mProgress.dismiss();
            }
        });
    }


}