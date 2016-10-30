package com.zenpets.doctors.creators;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zenpets.doctors.R;
import com.zenpets.doctors.utils.TypefaceSpan;
import com.zenpets.doctors.utils.adapters.DoctorPrefixAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DoctorCreatorActivity extends AppCompatActivity {

    /** THE FIREBASE USER INSTANCE **/
    FirebaseUser user;

    /** THE FIREBASE DATABASE REFERENCE INSTANCE **/
    DatabaseReference reference;

    /** A FIREBASE STORAGE REFERENCE **/
    StorageReference storageReference;

    /** THE LOGGED IN USER'S USER ID **/
    String USER_ID = null;

    /** DATA TYPES TO HOLD THE DOCTOR DETAILS **/
    String CLINIC_NAME = null;
    String DOCTOR_NAME = null;
    String DOCTOR_PREFIX = null;
    String DOCTOR_GENDER = "Male";
    String DOCTOR_SUMMARY = null;
    Uri DOCTOR_PROFILE_URI = null;
    private String FILE_NAME = null;

    /** THE URI'S **/
    private Uri imageUri;

    /** REQUEST CODE FOR SELECTING AN IMAGE **/
    private final int PICK_GALLERY_REQUEST = 1;
    private final int PICK_CAMERA_REQUEST = 2;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.spnPrefix) AppCompatSpinner spnPrefix;
    @BindView(R.id.edtDoctorsName) AppCompatEditText edtDoctorsName;
    @BindView(R.id.rgGender) RadioGroup rgGender;
    @BindView(R.id.edtDescription) AppCompatEditText edtDescription;
    @BindView(R.id.imgvwDoctorProfile) AppCompatImageView imgvwDoctorProfile;

    /** SELECT AN IMAGE OF THE MEDICINE **/
    @OnClick(R.id.imgvwDoctorProfile) void selectImage()    {
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorCreatorActivity.this, R.style.ZenPetsDialog);
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which)  {
                    case 0:
                        getGalleryImage();
                        break;
                    case 1:
                        getCameraImage();
                        break;
                    default:
                        break;
                }
            }
        }); builder.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_creator_activity);
        ButterKnife.bind(this);

        /** GET THE USER DETAILS **/
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            /** GET THE USER ID **/
            USER_ID = user.getUid();
//            Log.e("DOC USER ID", USER_ID);

            /** GET THE CLINIC DETAILS **/
            CLINIC_NAME = user.getDisplayName();
        } else {
            Toast.makeText(getApplicationContext(), "failed to get required information", Toast.LENGTH_SHORT).show();
            finish();
        }

        /** POPULATE THE PREFIX SPINNER **/
        String[] strServes = getResources().getStringArray(R.array.prefix);
        final List<String> arrPrefix;
        arrPrefix = Arrays.asList(strServes);
        spnPrefix.setAdapter(new DoctorPrefixAdapter(
                DoctorCreatorActivity.this,
                R.layout.custom_spinner_row,
                arrPrefix));

        /** CHANGE THE PREFIX **/
        spnPrefix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DOCTOR_PREFIX = arrPrefix.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /***** CONFIGURE THE ACTIONBAR *****/
        configAB();

        /** CHANGE THE GENDER **/
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)  {
                    case R.id.rdbtnMale:
                        DOCTOR_GENDER = "Male";
                        break;
                    case R.id.rdbtnFemale:
                        DOCTOR_GENDER = "Female";
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /** CREATE THE NEW DOCTOR'S PROFILE **/
    private void createDoctorProfile() {

        /** SHOW THE PROGRESS DIALOG WHILE UPLOADING THE IMAGE **/
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait while we add the Doctor's Profile to your Clinic....");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();

        /** INSTANTIATE THE STORAGE REFERENCE **/
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference refStorage = storageReference.child("Doctors").child(DOCTOR_PROFILE_URI.getLastPathSegment());
        refStorage.putFile(DOCTOR_PROFILE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadURL = taskSnapshot.getDownloadUrl();
                if (downloadURL != null)    {
                    reference = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference myRef = reference.child("Doctors").push();
                    myRef.child("clinicOwner").setValue(USER_ID);
                    myRef.child("doctorPrefix").setValue(DOCTOR_PREFIX);
                    myRef.child("doctorName").setValue(DOCTOR_NAME);
                    myRef.child("doctorGender").setValue(DOCTOR_GENDER);
                    myRef.child("doctorSummary").setValue(DOCTOR_SUMMARY);
                    myRef.child("doctorProfile").setValue(downloadURL.toString());

                    /** FINISH THE ACTIVITY **/
                    Toast.makeText(getApplicationContext(), "Successfully added the Doctor's Profile", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Image upload failed. Please try again", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });
    }

    /***** CHECK FOR ALL DOCTOR DETAILS  *****/
    private void checkDoctorDetails() {

        /** HIDE THE KEYBOARD **/
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtDoctorsName.getWindowToken(), 0);

        /** COLLECT THE NECESSARY DATA **/
        DOCTOR_NAME = edtDoctorsName.getText().toString().trim();
        DOCTOR_SUMMARY = edtDescription.getText().toString().trim();

        /** GENERATE THE FILE NAME **/
        if (!TextUtils.isEmpty(DOCTOR_NAME) && !TextUtils.isEmpty(CLINIC_NAME))    {
            FILE_NAME = DOCTOR_NAME.replaceAll(" ", "_").toLowerCase().trim() + "_" + CLINIC_NAME.replaceAll(" ", "_");
            Log.e("FILE NAME", FILE_NAME);
        }

        /** VALIDATE THE DETAILS **/
        if (TextUtils.isEmpty(DOCTOR_NAME)) {
            edtDoctorsName.setError("");
        } else if (TextUtils.isEmpty(DOCTOR_SUMMARY))   {
            edtDescription.setError("");
        } else if (DOCTOR_PROFILE_URI == null)  {
            Toast.makeText(getApplicationContext(), "Select the Doctor's profile picture", Toast.LENGTH_LONG).show();
        } else {
            /** CREATE THE NEW DOCTOR'S PROFILE **/
            createDoctorProfile();
        }
    }

    /***** CONFIGURE THE ACTIONBAR *****/
    @SuppressWarnings("ConstantConditions")
    private void configAB() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        String strTitle = "New Doctor";
//        String strTitle = getString(R.string.add_a_new_pet);
        SpannableString s = new SpannableString(strTitle);
        s.setSpan(new TypefaceSpan(getApplicationContext()), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setSubtitle(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(DoctorCreatorActivity.this);
        inflater.inflate(R.menu.generic_activity_save_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save:
                /***** CHECK FOR ALL DOCTOR DETAILS  *****/
                checkDoctorDetails();
                break;
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }
        return false;
    }

    /** FETCH AN IMAGE FROM THE GALLERY **/
    private void getGalleryImage() {
        Intent getGalleryImage = new Intent();
        getGalleryImage.setType("image/*");
        getGalleryImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(getGalleryImage, "Choose a picture"), PICK_GALLERY_REQUEST);
    }

    /** FETCH AN IMAGE FROM THE CAMERA **/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void getCameraImage() {
        Intent getCameraImage = new Intent("android.media.action.IMAGE_CAPTURE");
        File cameraFolder;
        if (Environment.getExternalStorageState().equals (Environment.MEDIA_MOUNTED))
            cameraFolder = new File(android.os.Environment.getExternalStorageDirectory(), "ZenPets/");
        else
            cameraFolder = getCacheDir();
        if(!cameraFolder.exists())
            cameraFolder.mkdirs();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault());
        String timeStamp = dateFormat.format(new Date());
        String imageFileName = "picture_" + timeStamp + ".jpg";

        File photo = new File(Environment.getExternalStorageDirectory(), "ZenPets/" + imageFileName);
        getCameraImage.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);

        startActivityForResult(getCameraImage, PICK_CAMERA_REQUEST);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap BMP_IMAGE;
        Uri targetURI;
        if (resultCode == RESULT_OK && requestCode == PICK_CAMERA_REQUEST)  {
            targetURI = imageUri;
            Bitmap bitmap = BitmapFactory.decodeFile(targetURI.getPath());
            BMP_IMAGE = resizeBitmap(bitmap);
            imgvwDoctorProfile.setImageBitmap(BMP_IMAGE);
            imgvwDoctorProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);

            /** STORE THE BITMAP AS A FILE AND USE THE FILE'S URI **/
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/ZenPets");
            myDir.mkdirs();
            String fName = "photo.jpg";
            File file = new File(myDir, fName);
            if (file.exists()) file.delete();

            try {
                FileOutputStream out = new FileOutputStream(file);
                BMP_IMAGE.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                /** GET THE FINAL DOCTOR PROFILE URI **/
                DOCTOR_PROFILE_URI = Uri.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == PICK_GALLERY_REQUEST) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                BMP_IMAGE = resizeBitmap(bitmap);
                imgvwDoctorProfile.setImageBitmap(BMP_IMAGE);
                imgvwDoctorProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);

                /** STORE THE BITMAP AS A FILE AND USE THE FILE'S URI **/
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/ZenPets");
                myDir.mkdirs();
                String fName = "photo.jpg";
                File file = new File(myDir, fName);
                if (file.exists()) file.delete();

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    BMP_IMAGE.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    /** GET THE FINAL DOCTOR PROFILE URI **/
                    DOCTOR_PROFILE_URI = Uri.fromFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** RESIZE THE BITMAP **/
    private Bitmap resizeBitmap(Bitmap image)   {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = 1024;
            height = (int) (width / bitmapRatio);
        } else {
            height = 768;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}