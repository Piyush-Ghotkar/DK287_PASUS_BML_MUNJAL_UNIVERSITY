package com.example.healdon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MOCA_5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_o_c_a_5);

        ImageButton nextbtn=findViewById(R.id.finish);
        final TextView time=findViewById(R.id.time);
        final ImageView numbers=findViewById(R.id.frw_img);
        final TextView anstxt=findViewById(R.id.ans_text);
        anstxt.setVisibility(View.INVISIBLE);

        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                time.setText("00:"+((millisUntilFinished / 1000)));
            }

            public void onFinish() {
                time.setText("Enter the numbers in reverse order");
                numbers.setVisibility(View.INVISIBLE);
                anstxt.setVisibility(View.VISIBLE);

            }
        }.start();

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MOCA_Score mark_obj=new MOCA_Score();

                if(anstxt.getText().toString().equals("247")){
                    Log.d("test","matched");
                    mark_obj.que4=1;
                }else {
                    mark_obj.que4=0;
                }
                Log.d("test","que1="+mark_obj.que1+" que2="+mark_obj.que2+ " que3="+mark_obj.que3 + " que4="+mark_obj.que4);

                Intent intent = new Intent(MOCA_5.this, MOCA_6.class);
                startActivity(intent);
            }
        });
    }
}