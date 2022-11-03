package com.example.doginternet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initAction();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        loadImage();
    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
    }

    private void loadImage() {
        progressBar.setVisibility(View.VISIBLE);
        viewModel.loadDogImage();
        viewModel.getDogImage().observe(this, new Observer<DogImage>() {
            @Override
            public void onChanged(DogImage dogImage) {
                Glide.with(MainActivity.this).load(dogImage.getMessage()).into(imageView);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initAction() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImage();
            }
        });
    }

}