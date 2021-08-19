package com.supriya.detectface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView imgCaptured;
    Button btnCamera,btnReset;
    FirebaseVisionImage visionImage;
    FirebaseVisionFaceDetector faceDetector;
    private final static int IMG_ID = 9;
    String text="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        imgCaptured = findViewById(R.id.imgCaptured);
        btnCamera = findViewById(R.id.captureImg);
        btnReset = findViewById(R.id.reset);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) !=null){
                    startActivityForResult(intent,IMG_ID);
                }
                else {
                    Toast.makeText(MainActivity.this,"Something Went Wrong !!!",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCaptured.setImageResource(R.drawable.img2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_ID && resultCode==RESULT_OK){
            Bundle info = data.getExtras();
            Bitmap bitmap = (Bitmap)info.get("data");
            recongnizeFace(bitmap);
            imgCaptured.setImageDrawable(new BitmapDrawable(getResources(),bitmap));
        }
    }

    private void recongnizeFace(Bitmap bitmap){
        FirebaseVisionFaceDetectorOptions detectorOptions = new FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build();
        try {
            visionImage = FirebaseVisionImage.fromBitmap(bitmap);
            faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(detectorOptions);
        }catch (Exception e){
            e.printStackTrace();
        }

        faceDetector.detectInImage(visionImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                int no = 1;
                for (FirebaseVisionFace firebaseVisionFace: firebaseVisionFaces){
                    text = text.concat("\nFACE NUMBER "+no+": ")
                            .concat("\nSmile:" + firebaseVisionFace.getSmilingProbability()*100 + "%")
                            .concat("\nLeft eye open: "+ firebaseVisionFace.getLeftEyeOpenProbability()*100 + "%")
                            .concat("\nRight eye open: "+ firebaseVisionFace.getRightEyeOpenProbability()*100+"%");
                    no++;
                }
                if (firebaseVisionFaces.size()==0){
                    Toast.makeText(MainActivity.this,"NO FACE DETECTED",Toast.LENGTH_SHORT).show();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString(Detection.result,text);
                    DialogFragment dialogFragment = new ResultActivity();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.setCancelable(true);
                    dialogFragment.show(getSupportFragmentManager(),Detection.resultDialog);

                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Bug Occurred !!!",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

