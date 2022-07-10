package com.lux.ex071cameraapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv=findViewById(R.id.iv);

        findViewById(R.id.btn).setOnClickListener(view -> {
            //camera 앱을 실행시키는 Intent 생성
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //결과를 받기 위해 액티비티를 실행시켜주는 객체에게 실행 요청
            resultLauncher.launch(intent);
        });
    }
    ActivityResultLauncher<Intent> resultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode()==RESULT_OK){

                //M 버전 부터 앱으로 실행한 카메라 앱으로 촬영한 사진을 곧바로
                //파일로 자동 저장하지 않고 결과를 단순 Bitmap(이미지 객체)을 줌.
                //모든 디바이스가 이런 특성을 가지진 않음.

                //결과를 가져온 택배기사 (Intent) 소환
                Intent intent=result.getData();
                Uri uri =intent.getData();

                if (uri==null){ //uri가 없으면 == 파일로 저장되어 있지 않다면 -> Bitmap으로 결과받음 [요즘 대부분]

                    Toast.makeText(MainActivity.this, "Bitmap으로 받음..", Toast.LENGTH_SHORT).show();

                    //Intent의 추가데이터로 Bitmap 객체가 전달되어 옴
                    Bundle bundle=intent.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    Glide.with(MainActivity.this).load(bitmap).into(iv);

                    //bitmap 객체는 촬영된 사진의 썸네일 이미지임.
                    //그러다보니 사진을 크게 보면 화질이 매우 나쁨. 그래서 실제 카메라앱을 사용할떄는
                    //파일로 저장되도록 미리 작업을 해놓고 실행해야 함.

                }else{ //uri가 있으면 == 파일로 저장이 되어 있다면
                    Toast.makeText(MainActivity.this, "uri로 받았다.", Toast.LENGTH_SHORT).show();
                    Glide.with(MainActivity.this).load(uri).into(iv);
                }
            }
        }
    });
}