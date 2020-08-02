package com.example.healdon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {

    TextView signout_btn,nearbydoc_btn;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_more, container, false);

        signout_btn=view.findViewById(R.id.signout_txt);

        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_out_fun();
            }
        });


        return view;
    }

    public void sign_out_fun(){
        FirebaseAuth.getInstance().signOut();

        SharedPreferences settings = this.getActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        settings.edit().clear().commit();

        Intent i=new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }
}
