package com.zenpets.doctors.creators;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
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
import com.zenpets.doctors.utils.adapters.CountriesAdapter;
import com.zenpets.doctors.utils.adapters.StatesAdapter;
import com.zenpets.doctors.utils.helpers.CountryArrayCreator;
import com.zenpets.doctors.utils.helpers.LocationPickerActivity;
import com.zenpets.doctors.utils.helpers.StatesArrayCreator;
import com.zenpets.doctors.utils.models.CountryData;
import com.zenpets.doctors.utils.models.StatesData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ClinicCreatorActivity extends AppCompatActivity {

    /** A FIREBASE USER INSTANCE **/
    FirebaseUser user;

    /** A FIREBASE DATABASE REFERENCE INSTANCE **/
    DatabaseReference reference;

    /** A FIREBASE STORAGE REFERENCE **/
    StorageReference storageReference;

    /** THE ACCOUNT USER ID **/
    String USER_ID = null;

    /** THE REQUEST CODES **/
    private int REQUEST_LOCATION = 1;
    private int REQUEST_GALLERY = 2;
    private int REQUEST_CAMERA = 3;

    /****** DATA TYPES FOR PROFILE DETAILS *****/
    String CLINIC_NAME = null;
    String CONTACT_PERSON = null;
    String PHONE_NUMBER = null;
    String POSTAL_ADDRESS = null;
    String CITY = null;
    String STATE = null;
    String PIN_CODE = null;
    String LANDMARK = null;
    String COUNTRY_NAME = null;
    String CURRENCY_SYMBOL = null;
    Double CLINIC_LATITUDE;
    Double CLINIC_LONGITUDE;
    String FILE_NAME = null;
    private Uri LOGO_URI = null;

    /** COUNTRY ARRAY LIST, ADAPTER AND HELPER **/
    ArrayList<CountryData> arrCountries = new ArrayList<>();
    CountryArrayCreator countryCreator;

    /** COUNTRY ARRAY LIST, ADAPTER AND HELPER **/
    ArrayList<StatesData> arrStates = new ArrayList<>();
    StatesArrayCreator stateCreator;
    StatesAdapter adapter;

    /** THE URI'S **/
    Uri imageUri;
    Uri targetURI;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.inputClinicName) TextInputLayout inputClinicName;
    @BindView(R.id.edtClinicName) AppCompatEditText edtClinicName;
    @BindView(R.id.inputContactPerson) TextInputLayout inputContactPerson;
    @BindView(R.id.edtContactPerson) AppCompatEditText edtContactPerson;
    @BindView(R.id.inputPhone) TextInputLayout inputPhone;
    @BindView(R.id.edtPhone) AppCompatEditText edtPhone;
    @BindView(R.id.inputPostalAddress) TextInputLayout inputPostalAddress;
    @BindView(R.id.edtPostalAddress) AppCompatEditText edtPostalAddress;
    @BindView(R.id.inputCity) TextInputLayout inputCity;
    @BindView(R.id.edtCity) AppCompatEditText edtCity;
    @BindView(R.id.spnState) AppCompatSpinner spnState;
    @BindView(R.id.inputPinCode) TextInputLayout inputPinCode;
    @BindView(R.id.edtPinCode) AppCompatEditText edtPinCode;
    @BindView(R.id.inputLandmark) TextInputLayout inputLandmark;
    @BindView(R.id.edtLandmark) AppCompatEditText edtLandmark;
    @BindView(R.id.txtCountryName) AppCompatTextView txtCountryName;
    @BindView(R.id.txtCurrencySymbol) AppCompatTextView txtCurrencySymbol;
    @BindView(R.id.txtLocation) AppCompatTextView txtLocation;
    @BindView(R.id.imgvwLogo) AppCompatImageView imgvwLogo;

    /** SELECT THE LOCATION ON THE MAP **/
    @OnClick(R.id.txtLocationPicker) void locationPicker()  {
        Intent intent = new Intent(this, LocationPickerActivity.class);
        startActivityForResult(intent, REQUEST_LOCATION);
    }

    /** PICK AN IMAGE **/
    @OnClick(R.id.imgvwLogo) void pickLogo()    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClinicCreatorActivity.this, R.style.ZenPetsDialog);
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

    @OnClick(R.id.fralaCountrySelector) void selectCountry()    {
        new MaterialDialog.Builder(ClinicCreatorActivity.this)
                .title("Select your country")
                .theme(Theme.LIGHT)
                .typeface("HelveticaNeueLTW1G-MdCn.otf", "HelveticaNeueLTW1G-Cn.otf")
                .adapter(new CountriesAdapter(ClinicCreatorActivity.this, arrCountries),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                COUNTRY_NAME = arrCountries.get(which).getCountryName();
                                txtCountryName.setText(COUNTRY_NAME);
                                CURRENCY_SYMBOL = arrCountries.get(which).getCurrencySymbol();
                                txtCurrencySymbol.setText(arrCountries.get(which).getCurrencySymbol());
                                Bitmap bmpFlag = arrCountries.get(which).getCountryFlag();
                                Drawable d = new BitmapDrawable(getResources(), bmpFlag);
                                txtCountryName.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                                dialog.dismiss();
                            }
                        })
                .show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinic_creator_activity);
        ButterKnife.bind(this);

        /** GET THE USER DETAILS **/
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            USER_ID = user.getUid();
            CLINIC_NAME = user.getDisplayName();

            /** SET THE CLINIC NAME **/
            edtClinicName.setText(CLINIC_NAME);
        } else {
            Toast.makeText(getApplicationContext(), "Failed to get required information", Toast.LENGTH_LONG).show();
            finish();
        }

        /** GET THE LIST OF STATES **/
        stateCreator = new StatesArrayCreator(ClinicCreatorActivity.this);
        arrStates = stateCreator.generateStatesArray();

        /** GET THE LIST OF COUNTRIES **/
        countryCreator = new CountryArrayCreator(ClinicCreatorActivity.this);
        arrCountries = countryCreator.generateCountryArray();
        Log.e("COUNTRIES SIZE", String.valueOf(arrCountries.size()));

        /** INSTANTIATE THE STATES ADAPTER **/
        adapter = new StatesAdapter(ClinicCreatorActivity.this, arrStates);

        /** SET THE ADAPTER TO THE SPINNER **/
        spnState.setAdapter(adapter);

        /** SELECT THE STATE **/
        spnState.setOnItemSelectedListener(selectState);

        /***** CONFIGURE THE ACTIONBAR *****/
        configAB();
    }

    /** CREATE THE CLINIC PROFILE **/
    private void createClinicProfile() {

        /** SHOW THE PROGRESS DIALOG WHILE UPLOADING THE IMAGE **/
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait while we add your Clinic details....");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();

        /** UPLOAD THE CLINIC LOGO **/
        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference refStorage = storageReference.child("Clinics").child(FILE_NAME);
        refStorage.putFile(LOGO_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadURL = taskSnapshot.getDownloadUrl();
                if (downloadURL != null)    {

                    /** CREATE A RECORD IN THE DATABASE **/
                    reference = FirebaseDatabase.getInstance().getReference().child("Clinics").push();
                    reference.child("clinicOwner").setValue(USER_ID);
                    reference.child("clinicSubscription").setValue("Free");
                    reference.child("clinicName").setValue(CLINIC_NAME);
                    reference.child("clinicContactPerson").setValue(CONTACT_PERSON);
                    reference.child("clinicPhone").setValue(PHONE_NUMBER);
                    reference.child("clinicAddress").setValue(POSTAL_ADDRESS);
                    reference.child("clinicCity").setValue(CITY);
                    reference.child("clinicState").setValue(STATE);
                    reference.child("clinicPinCode").setValue(PIN_CODE);
                    reference.child("clinicLandmark").setValue(LANDMARK);
                    reference.child("clinicCountry").setValue(COUNTRY_NAME);
                    reference.child("clinicCurrency").setValue(CURRENCY_SYMBOL);
                    reference.child("clinicLatitude").setValue(CLINIC_LATITUDE);
                    reference.child("clinicLongitude").setValue(CLINIC_LONGITUDE);
                    reference.child("clinicLogo").setValue(downloadURL.toString());

                    /** DISMISS THE DIALOG **/
                    dialog.dismiss();

                    /** FINISH THE ACTIVITY **/
                    Toast.makeText(getApplicationContext(), "Successfully added your Clinic Profile", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Image upload failed. Please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /***** CHECK FOR ALL CLINIC DETAILS  *****/
    private void checkClinicDetails() {

        /** HIDE THE KEYBOARD **/
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtClinicName.getWindowToken(), 0);

        /** COLLECT ALL THE DATA **/
        CLINIC_NAME = edtClinicName.getText().toString().trim();
        CONTACT_PERSON = edtContactPerson.getText().toString().trim();
        PHONE_NUMBER = edtPhone.getText().toString().trim();
        POSTAL_ADDRESS = edtPostalAddress.getText().toString().trim();
        CITY = edtCity.getText().toString().trim();
        PIN_CODE = edtPinCode.getText().toString().trim();
        LANDMARK = edtLandmark.getText().toString().trim();

        /** GENERATE THE FILE NAME **/
        if (!TextUtils.isEmpty(CLINIC_NAME) && !TextUtils.isEmpty(USER_ID))    {
            FILE_NAME = CLINIC_NAME.replaceAll(" ", "_").toLowerCase().trim() + "_" + USER_ID;
            Log.e("FILE NAME", FILE_NAME);
        } else {
            FILE_NAME = null;
        }

        /** VALIDATE THE DATA **/
        if (TextUtils.isEmpty(CLINIC_NAME)) {
            inputClinicName.setError(getString(R.string.clinic_name_empty));
            inputContactPerson.setErrorEnabled(false);
            inputPhone.setErrorEnabled(false);
            inputPostalAddress.setErrorEnabled(false);
            inputCity.setErrorEnabled(false);
            inputPinCode.setErrorEnabled(false);
        } else if (TextUtils.isEmpty(CONTACT_PERSON))   {
            inputContactPerson.setError(getString(R.string.clinic_contact_person_empty));
            inputClinicName.setErrorEnabled(false);
            inputPhone.setErrorEnabled(false);
            inputPostalAddress.setErrorEnabled(false);
            inputCity.setErrorEnabled(false);
            inputPinCode.setErrorEnabled(false);
        } else if (TextUtils.isEmpty(PHONE_NUMBER)) {
            inputPhone.setError(getString(R.string.clinic_phone_empty));
            inputContactPerson.setErrorEnabled(false);
            inputClinicName.setErrorEnabled(false);
            inputPostalAddress.setErrorEnabled(false);
            inputCity.setErrorEnabled(false);
            inputPinCode.setErrorEnabled(false);
        } else if (TextUtils.isEmpty(POSTAL_ADDRESS))   {
            inputPostalAddress.setError(getString(R.string.clinic_postal_address_empty));
            inputContactPerson.setErrorEnabled(false);
            inputPhone.setErrorEnabled(false);
            inputClinicName.setErrorEnabled(false);
            inputCity.setErrorEnabled(false);
            inputPinCode.setErrorEnabled(false);
        } else if (TextUtils.isEmpty(CITY)) {
            inputCity.setError(getString(R.string.clinic_city_empty));
            inputContactPerson.setErrorEnabled(false);
            inputPhone.setErrorEnabled(false);
            inputPostalAddress.setErrorEnabled(false);
            inputClinicName.setErrorEnabled(false);
            inputPinCode.setErrorEnabled(false);
        } else if (TextUtils.isEmpty(PIN_CODE)) {
            inputPinCode.setError(getString(R.string.clinic_pin_code_empty));
            inputContactPerson.setErrorEnabled(false);
            inputPhone.setErrorEnabled(false);
            inputPostalAddress.setErrorEnabled(false);
            inputCity.setErrorEnabled(false);
            inputClinicName.setErrorEnabled(false);
        } else if (TextUtils.isEmpty(COUNTRY_NAME) || TextUtils.isEmpty(CURRENCY_SYMBOL))   {
            Toast.makeText(getApplicationContext(), "Please select the default Country and Currency", Toast.LENGTH_LONG).show();
        } else if (CLINIC_LONGITUDE == null || CLINIC_LATITUDE == null) {
            Toast.makeText(getApplicationContext(), "Please select / mark your Location on the Map", Toast.LENGTH_LONG).show();
        } else if (LOGO_URI == null)    {
            Toast.makeText(getApplicationContext(), getString(R.string.clinic_logo_empty), Toast.LENGTH_LONG).show();
        } else {
            inputClinicName.setErrorEnabled(false);
            inputContactPerson.setErrorEnabled(false);
            inputPhone.setErrorEnabled(false);
            inputPostalAddress.setErrorEnabled(false);
            inputCity.setErrorEnabled(false);
            inputPinCode.setErrorEnabled(false);

            /** CREATE THE CLINIC PROFILE **/
            createClinicProfile();
        }
    }

    /***** CONFIGURE THE ACTIONBAR *****/
    @SuppressWarnings("ConstantConditions")
    private void configAB() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        String strTitle = "Clinic Details";
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
        MenuInflater inflater = new MenuInflater(ClinicCreatorActivity.this);
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
                /***** CHECK FOR ALL CLINIC DETAILS  *****/
                checkClinicDetails();
                break;
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }
        return false;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** A BITMAP INSTANCE **/
        Bitmap BMP_IMAGE;

        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA)  {
            targetURI = imageUri;
            Bitmap bitmap = BitmapFactory.decodeFile(targetURI.getPath());
            BMP_IMAGE = resizeBitmap(bitmap);
            imgvwLogo.setImageBitmap(BMP_IMAGE);
            imgvwLogo.setScaleType(ImageView.ScaleType.CENTER_CROP);

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

                /** GET THE FINAL PROFILE URI **/
                LOGO_URI = Uri.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY) {
            Uri uri = data.getData();
            targetURI = uri;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                BMP_IMAGE = resizeBitmap(bitmap);
                imgvwLogo.setImageBitmap(BMP_IMAGE);
                imgvwLogo.setScaleType(ImageView.ScaleType.CENTER_CROP);

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

                    /** GET THE FINAL PROFILE URI **/
                    LOGO_URI = Uri.fromFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_LOCATION)    {
            Bundle bundle = data.getExtras();
            CLINIC_LATITUDE = bundle.getDouble("LATITUDE");
            CLINIC_LONGITUDE = bundle.getDouble("LONGITUDE");

            /** GET THE APPROXIMATE ADDRESS FOR DISPLAY **/
            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(CLINIC_LATITUDE, CLINIC_LONGITUDE, 1);
                String address = addresses.get(0).getAddressLine(0);
                if (!TextUtils.isEmpty(address))    {
                    txtLocation.setText(address);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** FETCH AN IMAGE FROM THE GALLERY **/
    private void getGalleryImage() {
        Intent getGalleryImage = new Intent();
        getGalleryImage.setType("image/*");
        getGalleryImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(getGalleryImage, "Choose a picture"), REQUEST_GALLERY);
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

        startActivityForResult(getCameraImage, REQUEST_CAMERA);
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

    /** SELECT A STATE **/
    private final AdapterView.OnItemSelectedListener selectState = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            STATE = arrStates.get(position).getStateName();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}