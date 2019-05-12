package com.example.karu.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karu.HomeActivity;
import com.example.karu.Model.Upload;
import com.example.karu.NumericKeyBoardTransformationMethod;
import com.example.karu.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FilenameFilter;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends Fragment {

    private static final String TAG = "CameraFragment";

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button buttonChooseImage,buttonUpload;

    private EditText editTextFileName;
    private EditText editTextCategory;
    private EditText editTextRentRate;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    //to navigate between fragments

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);

        // access getContentResolver()

        buttonChooseImage = (Button)v.findViewById(R.id.button_choose_image);
        buttonUpload = (Button)v.findViewById(R.id.button_upload);

        editTextFileName = (EditText)v.findViewById(R.id.edit_text_file_name);
        editTextCategory = (EditText)v.findViewById(R.id.fragment_browse_editText_category);
        editTextRentRate = (EditText)v.findViewById(R.id.fragment_browse_editText_rentRate_week);


        editTextRentRate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        editTextRentRate.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        mImageView = (ImageView)v.findViewById(R.id.image_view_camera_fragment);
        mProgressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.INVISIBLE);

        BottomNavigationView bottomNav = v.findViewById(R.id.bottom_navigation);

        // storage reference variable named uploads inside firebase
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");



        //textViewCameraFragment.setVisibility(View.INVISIBLE);
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // check if users click upload multiple times
                if(mUploadTask != null && mUploadTask.isInProgress())
                {
                    Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    uploadFile();
                }
            }
        });

        return v;
    }

    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check image request, check if user successfully picked image, check if we get back the uri of image
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            mImageUri = data.getData();
            // in fragment use getActivity() instead of this
            Picasso.with(getActivity()).load(mImageUri).into(mImageView);

        }
    }


    // this method return the file extenstion of file we picked
    private String getFileExtension(Uri uri)
    {
        // this is how to used getContentResolver() inside of a fragment class
        Context applicationContext  = HomeActivity.getContextOfApplication();

        // to use getContentResolver() inside a fragment
        ContentResolver cR = applicationContext.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }



    private void uploadFile()
    {
        final String FileNameText = editTextFileName.getText().toString().trim();
        final String CategoryText = editTextCategory.getText().toString().trim();
        final String RentRateText = editTextRentRate.getText().toString().trim();
        final String UserID = FirebaseAuth.getInstance().getUid();
        final boolean isLike = false;
        final boolean isListed = false;

        if(FileNameText.isEmpty())
        {
            Toast.makeText(getActivity(), "Please put file name", Toast.LENGTH_SHORT).show();
            editTextFileName.setError("Name is required");
            editTextFileName.requestFocus();
            return;
        }
        if(CategoryText.isEmpty())
        {
            Toast.makeText(getActivity(), "Please put item category", Toast.LENGTH_SHORT).show();
            editTextCategory.setError("Category is required");
            editTextCategory.requestFocus();
            return;
        }
        if(RentRateText.isEmpty())
        {
            Toast.makeText(getActivity(), "Please put rental rate", Toast.LENGTH_SHORT).show();
            editTextRentRate.setError("Rental rate is required");
            editTextRentRate.requestFocus();
            return;
        }
        // check image uri not null and upload it
        if(mImageUri != null)
        {
            // to get a unique name for out item just asign it to time in milliseconds
            // point to "uploads" node to firebase storage references
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+
                    "." + getFileExtension(mImageUri));

            final int RentRateInteger = Integer.parseInt(RentRateText);


            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // after successfull, progress bar is set to zero
                            // mProgressBar.setProgress(0);
                            // delay the progress bar for user to experience its has been uploaded 100%
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            }, 500);// deleay progress bar for 5 seconds

                            Toast.makeText(getActivity(), "Upload Successfull", Toast.LENGTH_LONG).show();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Upload upload = new Upload(FileNameText, url, CategoryText,RentRateInteger, isLike, isListed);
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // just toast error message
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // simply to give the current progress of uploading file in percent
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);

                        }
                    });

        }
        else
        {
            Toast.makeText(getActivity(), "No File Selected", Toast.LENGTH_SHORT).show();
        }
/*        if (mImageUri != null)
        {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(editTextFileName.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        }
        else
            {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }*/
    }
/*    private void uploadFile()
    {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(editTextFileName.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }*/



 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(getActivity()).load(mImageUri).into(mImageView);
        }
    }*/

    // this method will be called if we pick out file





}
