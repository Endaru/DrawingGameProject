package com.example.ellilim.drawinggameproject;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ellilim.drawinggameproject.canvas.CanvasView;

public class DrawingActivity extends AppCompatActivity {

    private CanvasView mCustomCanvas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        mCustomCanvas = (CanvasView) findViewById(R.id.canvas);
    }
}
