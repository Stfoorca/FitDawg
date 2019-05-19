package com.example.fitdawg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.ContentResolver;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class Tab2Fragment extends Fragment{
    private static final String TAG = "Tab2Fragment";
    public Uri selectedImage;
    private Button logoutBtn, addButton, profileChangeButton;
    private TextView tab2username, tab2email, tab2gender, tab2age, tab2height, tab2bmi, tab2weight;
    public ImageView tab2profileImage;
    public ProfileActivity profileActivity;
    private FirebaseUser mUser;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private final int GALLERY_REQUEST_CODE = 9165;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab2_fragment, container, false);

        profileActivity = (ProfileActivity) getActivity();

        tab2username = (TextView)view.findViewById(R.id.username);
        tab2email = (TextView) view.findViewById(R.id.email);
        tab2gender = (TextView)view.findViewById(R.id.gender);
        tab2age = (TextView)view.findViewById(R.id.age);
        tab2profileImage = (ImageView)view.findViewById(R.id.profileImage);
        tab2height = (TextView)view.findViewById(R.id.height);
        tab2bmi = (TextView)view.findViewById(R.id.bmi);
        tab2weight = (TextView) view.findViewById(R.id.weight);

        mUser = profileActivity.mAuth.getCurrentUser();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        addButton = (Button) view.findViewById(R.id.addButton);
        logoutBtn = (Button) view.findViewById(R.id.logout);
        profileChangeButton = view.findViewById(R.id.changeProfileButton);

        tab2profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AddActivity.class));
            }
        });

        profileChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), ProfileChangeActivity.class));
            }
        });

        return view;
    }

    public void UpdateUserProfile(User user){

        tab2username.setText(user.name);
        tab2email.setText("Email:   " + user.email);

        tab2gender.setText("Gender:     " + user.gender);


        tab2age.setText("Age:   " + Integer.toString(Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(user.year)));
        tab2height.setText("Height:     " + user.height);
        tab2weight.setText("Weight:     " + user.weight);
        if(user.weight!="0")
            tab2bmi.setText("BMI:   " + String.format(Locale.UK, "%.2f",(Float.parseFloat(user.weight)/(Float.parseFloat(user.height)*Float.parseFloat(user.height))*100*100)));
        else tab2bmi.setText("Brak pomiaru wagi");
        Uri test = mUser.getPhotoUrl();
        Picasso.get().load(mUser.getPhotoUrl()).into(tab2profileImage);
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            if(requestCode == GALLERY_REQUEST_CODE) {
                //data.getData returns the content URI for the selected Image
                selectedImage = data.getData();


                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                // Get the cursor
                Cursor cursor = profileActivity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                //Get the column index of MediaStore.Images.Media.DATA
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //Gets the String value in the column
                String imgDecodableString = cursor.getString(columnIndex);
                Log.d("-----------------------",imgDecodableString);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                //imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));


                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(selectedImage).build();
                mUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Picasso.get().load(selectedImage).into(tab2profileImage);
                            uploadImage(selectedImage);
                            Picasso.get().load(mUser.getPhotoUrl()).into(tab2profileImage);

                        }

                    }

                });

            }

    }

    private void uploadImage(Uri filePath) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(profileActivity);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageRef.child("images/"+ mUser.getUid());
            UploadTask uploadTask = ref.putFile(filePath);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                        mUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                }

                            }

                        });
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    progressDialog.dismiss();
                    Toast.makeText(profileActivity, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(profileActivity, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
