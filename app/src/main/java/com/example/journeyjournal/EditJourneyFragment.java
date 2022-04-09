package com.example.journeyjournal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.journeyjournal.databinding.FragmentAddJourneyDetailBinding;
import com.example.journeyjournal.databinding.FragmentEditJourneyBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditJourneyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditJourneyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    declare a variable
    private EditText PlaceName;
    private EditText PlaceDescription;
    private Button updateJourneyButton;
    private ImageView editImageView;
    private ProgressBar LoadingAddJourney;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String journeyId;
    private ImageView editBackIcon;
    private JourneyRVModal journeyRVModal;
    private FragmentEditJourneyBinding binding;

//    call a empty constructor
    public EditJourneyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditJourneyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditJourneyFragment newInstance(String param1, String param2) {
        EditJourneyFragment fragment = new EditJourneyFragment();
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
          journeyRVModal=getArguments().getParcelable("journeyrvmodal");
        }
    }
    public void initView(View view){
//        set the layout id in variable
        PlaceName = view.findViewById(R.id.add_placeName);
        PlaceDescription = view.findViewById(R.id.add_place_Description);
        updateJourneyButton = view.findViewById(R.id.update_Journey_Btn);
        LoadingAddJourney = view.findViewById(R.id.Loading_Add_Product);
        editBackIcon=view.findViewById(R.id.back_icon);
        editImageView = view.findViewById(R.id.addEachImageJourney);
        firebaseDatabase=FirebaseDatabase.getInstance();
//        journeyRVModal = getIntent().getParcelableExtra("Journey");
//        check a condition whether the journeyrvmodal is not
        if(journeyRVModal !=null){
//            if not null set the value in variable
            PlaceName.setText(journeyRVModal.getPlaceName());
            PlaceDescription.setText(journeyRVModal.getPlaceDescription());
//            editImageView.setText()
            Picasso.get().load(journeyRVModal.getJourneyImage()).into(editImageView);
                    journeyId = journeyRVModal.getJourneyId();
        }
        databaseReference = firebaseDatabase.getReference("Journey").child(journeyId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_edit_journey, container, false);
        binding = FragmentEditJourneyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initView(root);
        initBackAction();
        initUpdateJourneyBtn();
        return root;
    }

    public void initUpdateJourneyBtn(){
        updateJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingAddJourney.setVisibility(View.VISIBLE);
                String placeNameField = PlaceName.getText().toString().trim();
                String placeDescriptionField = PlaceDescription.getText().toString().trim();
                //getImage
                //map the value
                Map<String,Object> map =new HashMap<>();
                map.put("placeName",placeNameField);
                map.put("placeDescription",placeDescriptionField);
                map.put("journeyId",journeyId);

                databaseReference.addValueEventListener(new ValueEventListener() {
//                    if any data change while edit onDataChange function call and update the value
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        LoadingAddJourney.setVisibility(View.GONE);
                        databaseReference.updateChildren(map);
                        Toast.makeText(requireActivity(),"Journey Update Successfully",Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(v);
                        if (navController.getCurrentDestination().getId() == R.id.editJourneyFragment) {
                            navController.navigate(R.id.action_editJourneyFragment_to_navigation_home);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireActivity(),"Fail to Update Journey",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void initBackAction(){
        editBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick(view);
            }
        });
    }

    private void  onBackClick(View view){
        Navigation.findNavController(view).navigate(R.id.action_editJourneyFragment_to_navigation_home);
    }
}