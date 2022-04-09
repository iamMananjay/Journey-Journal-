package com.example.journeyjournal;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.provider.MediaStore;
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

import com.example.journeyjournal.databinding.FragmentAddJourneyDetailBinding;
import com.example.journeyjournal.databinding.FragmentHomeBinding;
import com.example.journeyjournal.presentation.DashboardActivity;
import com.example.journeyjournal.presentation.Registration.RegistrationActivity;
import com.example.journeyjournal.presentation.login.LoginActivity;
import com.example.journeyjournal.presentation.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddJourneyDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddJourneyDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int GALLERY_REQUEST_CODE = 105;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //declare a variable
    private EditText PlaceName;
    private EditText PlaceDescription;
    private Button AddJourneyButton;
    private ProgressBar LoadingAddJourney;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String journeyId;
    private FragmentAddJourneyDetailBinding binding;
    private ImageView selectedImage;
    private ImageView cameraBtn;
    private ImageView galleryBtn;
    private ImageView addBackIcon;
    private String fireStoreImageUri;
    String currentPhotoPath;
    StorageReference storageReference;

//    call a empty constructor
    public AddJourneyDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddJourneyDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddJourneyDetailFragment newInstance(String param1, String param2) {
        AddJourneyDetailFragment fragment = new AddJourneyDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void  initView(View view){
//        set the layout id in variable
        PlaceName = view.findViewById(R.id.add_placeName);
        PlaceDescription = view.findViewById(R.id.add_place_Description);
        selectedImage =view.findViewById(R.id.addEachImageJourney);
        cameraBtn=view.findViewById(R.id.choose_image);
        galleryBtn=view.findViewById(R.id.upload_image);
        addBackIcon=view.findViewById(R.id.back_icon);
        AddJourneyButton = view.findViewById(R.id.Add_Journey_Btn);
        LoadingAddJourney = view.findViewById(R.id.Loading_Add_Product);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Journey");
        storageReference= FirebaseStorage.getInstance().getReference();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        initView(inflater.inflate(R.layout.fragment_add_journey_detail, container, false));
         binding = FragmentAddJourneyDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        call a  function and pass root as argument
        initView(root);
        initAddJourneyBtn();
        initCameraBtn();
        initGalleryBtn();
        initBackAction();
        return root;
    }
//    Add a action on click camera bottom
    public void initCameraBtn(){
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
            }
        });
    }
//    call a function while camera bottom is click and ask for permission
    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),new String[] {Manifest.permission.CAMERA},CAMERA_PERM_CODE);
        }
        else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[ ] grantResults){
//        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                openCamera();
                dispatchTakePictureIntent();
            }
        }else{
            Toast.makeText(requireActivity(), "Camera Permission is Required to use Camera", Toast.LENGTH_SHORT).show();
        }
    }

//    private void openCamera() {
//        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(camera, CAMERA_REQUEST_CODE);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
//                this.sendBroadcast(mediaScanIntent);
                requireActivity().sendBroadcast(mediaScanIntent);
                uploadImageToFirebase(f.getName(),contentUri);

            }
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            selectedImage.setImageBitmap(image);
        }
        if (requestCode == GALLERY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                Uri contentUri = data.getData();
                String timeStamp = new  SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." +getFileExt(contentUri);
                selectedImage.setImageURI(contentUri);
                uploadImageToFirebase(imageFileName,contentUri);
            }
        }
    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
//        StorageReference image = databaseReference.child("images/" +  name);
        StorageReference image =storageReference.child("images/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        fireStoreImageUri = uri.toString();
                        Picasso.get().load(uri).into(selectedImage);

                    }
                });
                Toast.makeText(requireActivity(), "Image Is Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //images/image.jpg
    private String getFileExt(Uri contentUri) {
//        ContentResolver c = getContentResolver();
        ContentResolver c = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));

    };
    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return  image;

    };
//    static final int REQUEST_TAKE_PHOTO = 1;
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();

            }catch (IOException ex){
//                ...
            }
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(requireActivity(),"com.example.android.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePictureIntent,CAMERA_REQUEST_CODE);
//                onActivityResult(takePictureIntent,CAMERA_REQUEST_CODE);
//                getActivity().startActivityForResult(takePictureIntent,CAMERA_REQUEST_CODE);
            }

        }
    }
//    Add a action on upload bottom click
    public void initGalleryBtn(){
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });
    }
//    Add a action add journey bottom click
    public void initAddJourneyBtn(){
        AddJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickView) {
                LoadingAddJourney.setVisibility(View.VISIBLE);
                String placeNameField = PlaceName.getText().toString().trim();
                String placeDescriptionField = PlaceDescription.getText().toString().trim();
                journeyId = placeNameField;
                JourneyRVModal journeyRVModal = new JourneyRVModal(placeNameField,placeDescriptionField,journeyId,fireStoreImageUri);
                LoadingAddJourney.setVisibility(View.GONE);
                databaseReference.child(journeyId).setValue(journeyRVModal).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireActivity(),"Journey Add Successfully",Toast.LENGTH_SHORT).show();
//                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addJourneyDetailFragment_to_navigation_home);
                            NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                                    .getSupportFragmentManager()
                                    .findFragmentById(R.id.nav_host_fragment_activity_dashboard);
                            NavController navController = NavHostFragment.findNavController(navHostFragment);
                            if (navController.getCurrentDestination().getId() == R.id.addJourneyDetailFragment) {
                                navController.navigate(R.id.action_addJourneyDetailFragment_to_navigation_home);
                            }
                        } else {
                            Toast.makeText(requireActivity(), "Journey Not Added...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
    private void initBackAction(){
        addBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick(view);
            }
        });
    }
    private void  onBackClick(View view){
        Navigation.findNavController(view).navigate(R.id.action_addJourneyDetailFragment_to_navigation_home);
    }
}