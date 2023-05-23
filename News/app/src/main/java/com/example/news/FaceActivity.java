package com.example.news;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.news.databinding.ActFaceBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.util.List;


public class FaceActivity extends Activity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActFaceBinding binding = ActFaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .build();

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.face);

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);

        Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {

                                        Log.d("FACES", faces.toString());

                                        Point p = new Point();
                                        Display display = getWindowManager().getDefaultDisplay();
                                        display.getSize(p);

                                        for (FirebaseVisionFace face : faces) {

                                            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                            // nose available):
                                            FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
                                            float lex = leftEye.getPosition().getX();
                                            float ley = leftEye.getPosition().getY();

                                            FirebaseVisionFaceLandmark leftCheek = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK);
                                            float lcx = leftCheek.getPosition().getX();
                                            float lcy = leftCheek.getPosition().getY();

                                            FirebaseVisionFaceLandmark rightCheek = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK);
                                            float rex = rightCheek.getPosition().getX();
                                            float rey = rightCheek.getPosition().getY();

                                            ImageView imageLE = new ImageView(context);
                                            imageLE.setImageResource(R.drawable.mung);
                                            imageLE.setX(p.x * lex / bitmap.getWidth() - 100);
                                            imageLE.setY(p.y * lex / bitmap.getHeight() - 100);
                                            imageLE.setLayoutParams(new RelativeLayout.LayoutParams(200, 200));
                                            binding.RelativeLayoutMain.addView(imageLE);

                                            ImageView imageLC = new ImageView(context);
                                            imageLC.setImageResource(R.drawable.left_cat);
                                            imageLC.setX(p.x * lcx / bitmap.getWidth() - 100);
                                            imageLC.setY(p.y * lcx / bitmap.getHeight() - 100);
                                            imageLC.setLayoutParams(new RelativeLayout.LayoutParams(200, 200));
                                            binding.RelativeLayoutMain.addView(imageLC);

                                            ImageView imageRC = new ImageView(context);
                                            imageRC.setImageResource(R.drawable.right_cat);
                                            imageRC.setX(p.x * rex / bitmap.getWidth() - 100);
                                            imageRC.setY(p.y * rex / bitmap.getHeight() - 100);
                                            imageRC.setLayoutParams(new RelativeLayout.LayoutParams(200, 200));
                                            binding.RelativeLayoutMain.addView(imageRC);
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
    }
}
