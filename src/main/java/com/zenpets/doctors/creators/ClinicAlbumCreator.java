package com.zenpets.doctors.creators;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zenpets.doctors.R;
import com.zenpets.doctors.utils.TypefaceSpan;
import com.zenpets.doctors.utils.adapters.ClinicAlbumCreatorAdapter;
import com.zenpets.doctors.utils.models.ClinicAlbumCreatorData;
import com.zenpets.doctors.utils.models.ClinicsData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ClinicAlbumCreator extends AppCompatActivity {

    /** THE FIREBASE USER INSTANCE **/
    FirebaseUser user;

    /** THE FIREBASE DATABASE REFERENCE AND QUERY INSTANCE **/
    DatabaseReference refClinic;
    DatabaseReference refAlbums;
    Query qryClinic;

    /** THE FIREBASE STORAGE REFERENCE **/
    StorageReference storageReference;

    /** THE LOGGED IN USER'S USER ID **/
    String USER_ID = null;

    /** THE CLINIC ID AND NAME **/
    String CLINIC_ID = null;
    String CLINIC_NAME = null;

    /** THE ADAPTER AND ARRAY LIST FOR THE CLINIC ALBUMS **/
    ClinicAlbumCreatorAdapter adapter;
    ArrayList<ClinicAlbumCreatorData> arrAlbums = new ArrayList<>();

    /** THE PROGRESS DIALOG **/
    ProgressDialog dialog;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.linlaHeaderProgress) LinearLayout linlaHeaderProgress;
    @BindView(R.id.gridClinicImages) RecyclerView gridClinicImages;
    @BindView(R.id.linlaEmpty) LinearLayout linlaEmpty;

    /** SELECT CLINIC IMAGES **/
    @OnClick(R.id.linlaEmpty) void selectImages()    {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 8);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinic_album_creator_list);
        ButterKnife.bind(this);

        /***** CONFIGURE THE ACTIONBAR *****/
        configAB();

        /** CONFIGURE THE RECYCLER VIEW **/
        configRecycler();

        /** GET CLINIC DETAILS AND CHECK IF IMAGES EXIST **/
        getClinicData();
    }

    /** UPLOAD THE CLINIC IMAGES**/
    private void uploadClinicImages() {

        for (int i = 0; i < arrAlbums.size(); i++) {

            /** SHOW THE PROGRESS DIALOG WHILE UPLOADING THE IMAGE **/
            dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait while we upload your Clinic Album Images....");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();

            Bitmap bitmap = arrAlbums.get(i).getBmpClinicImage();

            /** STORE THE BITMAP AS A FILE AND USE THE FILE'S URI **/
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/ZenPets/Clinic Images");
            myDir.mkdirs();
            final String imageNumber = String.valueOf(i + 1);
            String fName = "photo" + imageNumber + ".jpg";
//            Log.e("FILE NAME", fName);
            File file = new File(myDir, fName);
            if (file.exists()) file.delete();

            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                /** GET THE FINAL CLINIC IMAGE URI **/
                Uri uri = Uri.fromFile(file);
//                Log.e("URI", String.valueOf(uri));

                String FILE_NAME = CLINIC_NAME.replaceAll(" ", "_").toLowerCase().trim() + "_" + USER_ID + "_" + fName;
                storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference refStorage = storageReference.child("Clinic Images").child(FILE_NAME);
                refStorage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadURL = taskSnapshot.getDownloadUrl();
                        if (downloadURL != null) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Clinics").child(CLINIC_ID).child("Images").push();
                            reference.child("clinicImage").setValue(downloadURL.toString());
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

            /** DISMISS THE DIALOG **/
            dialog.dismiss();
        }

        /** FINISH THE ACTIVITY **/
        Toast.makeText(getApplicationContext(), "Successfully added your Clinic's Images", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            /** CLEAR THE ARRAY LIST **/
            arrAlbums.clear();

            /** GET THE ARRAY LIST OF IMAGES RETURNED BY THE INTENT **/
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

            /** A NEW INSTANCE OF THE CLINIC ALBUMS MODEL **/
            ClinicAlbumCreatorData albums;
            for (int i = 0, l = images.size(); i < l; i++) {
                /***** INSTANTIATE THE ClinicAlbumCreatorData INSTANCE "albums" *****/
                albums = new ClinicAlbumCreatorData();

                /** GET THE IMAGE PATH **/
                String strPath = images.get(i).path;
                File filePath = new File(strPath);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath());
                Bitmap bmpImage = resizeBitmap(bitmap);
                albums.setBmpClinicImage(bmpImage);

                /** SET THE IMAGE NUMBER **/
                String strNumber = String.valueOf(i + 1);
                albums.setTxtImageNumber(strNumber);

                /** ADD THE COLLECTED DATA TO THE ARRAY LIST **/
                arrAlbums.add(albums);
            }

            /** INSTANTIATE THE ADAPTER **/
            adapter = new ClinicAlbumCreatorAdapter(ClinicAlbumCreator.this, arrAlbums);

            /** SET THE ADAPTER TO THE RECYCLER VIEW **/
            gridClinicImages.setAdapter(adapter);
            gridClinicImages.setVisibility(View.VISIBLE);
            linlaEmpty.setVisibility(View.GONE);
        }
    }

    /** GET CLINIC DETAILS AND CHECK IF IMAGES EXIST **/
    private void getClinicData() {

        /** GET THE USER DETAILS **/
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            /** GET THE USER ID **/
            USER_ID = user.getUid();

            /** GET THE CLINIC ID **/
            refClinic = FirebaseDatabase.getInstance().getReference().child("Clinics");
            qryClinic = refClinic.orderByChild("clinicOwner").equalTo(USER_ID);
            qryClinic.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            ClinicsData data = child.getValue(ClinicsData.class);
                            CLINIC_ID = child.getKey();
                            CLINIC_NAME = data.getClinicName();

                            /** CHECK IF THE CLINIC HAS IMAGES ON RECORD **/
                            refAlbums = FirebaseDatabase.getInstance().getReference().child("Clinics").child(CLINIC_ID).child("Images");
                            refAlbums.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {
                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                            refAlbums.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    /** SHOW OR HIDE THE EMPTY LAYOUT **/
                                    emptyShowOrHide(dataSnapshot);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    /** RESIZE THE BITMAP **/
    private Bitmap resizeBitmap(Bitmap image)   {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = 800;
            height = (int) (width / bitmapRatio);
        } else {
            height = 800;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /** SHOW OR HIDE THE EMPTY LAYOUT **/
    private void emptyShowOrHide(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            /** SHOW THE RECYCLER VIEW AND HIDE THE EMPTY LAYOUT **/
            gridClinicImages.setVisibility(View.VISIBLE);
            linlaEmpty.setVisibility(View.GONE);
        } else {
            /** HIDE THE RECYCLER VIEW AND SHOW THE EMPTY LAYOUT **/
            gridClinicImages.setVisibility(View.GONE);
            linlaEmpty.setVisibility(View.VISIBLE);
        }
    }

    /** CONFIGURE THE RECYCLER VIEW **/
    private void configRecycler() {
        int intOrientation = getResources().getConfiguration().orientation;
        gridClinicImages.setHasFixedSize(true);
        GridLayoutManager glm = null;
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet)   {
            if (intOrientation == 1)	{
                glm = new GridLayoutManager(this, 2);
            } else if (intOrientation == 2) {
                glm = new GridLayoutManager(this, 4);
            }
        } else {
            if (intOrientation == 1)    {
                glm = new GridLayoutManager(this, 2);
            } else if (intOrientation == 2) {
                glm = new GridLayoutManager(this, 4);
            }
        }
        gridClinicImages.setLayoutManager(glm);
    }

    /***** CONFIGURE THE ACTIONBAR *****/
    @SuppressWarnings("ConstantConditions")
    private void configAB() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        String strTitle = "Add Clinic Images";
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
        MenuInflater inflater = new MenuInflater(ClinicAlbumCreator.this);
        inflater.inflate(R.menu.activity_clinic_image_uploader, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menuUpload:
                /** CHECK THE ARRAY SIZE **/
                if (arrAlbums.size() == 0)  {
                    Toast.makeText(getApplicationContext(), getString(R.string.clinic_images_empty), Toast.LENGTH_LONG).show();
                } else {
                    /** UPLOAD THE CLINIC IMAGES**/
                    uploadClinicImages();
                }
                break;
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}