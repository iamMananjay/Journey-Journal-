package com.example.journeyjournal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.journeyjournal.databinding.FragmentEditJourneyBinding;
import com.example.journeyjournal.databinding.FragmentViewEachJourneyBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewEachJourneyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewEachJourneyFragment extends Fragment {
//    declare a variable
    private JourneyRVModal journeyRVModal;
    private FragmentViewEachJourneyBinding binding;
    private TextView PlaceName;
    private TextView PlaceDescription;
    private ImageView journeyImage;
    private ImageView viewBackIcon;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewEachJourneyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewEachJourneyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewEachJourneyFragment newInstance(String param1, String param2) {
        ViewEachJourneyFragment fragment = new ViewEachJourneyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
//    OnCreate Call back function
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            journeyRVModal=getArguments().getParcelable("journeyrvmodal");
        }
    }
//    OnCreateView function
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewEachJourneyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        call a function by passing argument as root
        initView(root);
        initBackAction();
        return root;
    };

    public void initView(View view){
//        get layout id in variable
        PlaceName = view.findViewById(R.id.city_name);
        PlaceDescription = view.findViewById(R.id.city_description_section);
        journeyImage= view.findViewById(R.id.each_place_image_section);
        viewBackIcon= view.findViewById(R.id.back_icon);
        firebaseDatabase=FirebaseDatabase.getInstance();
//        check the condition whether the value is not null if that then set the value in variable
        if(journeyRVModal !=null){
//            data set  from firebase
            PlaceName.setText(journeyRVModal.getPlaceName());
            PlaceDescription.setText(journeyRVModal.getPlaceDescription());
            Picasso.get().load(journeyRVModal.getJourneyImage()).into(journeyImage);
        }
    };

    private void initBackAction(){
        viewBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick(view);
            }
        });
    }

    private void  onBackClick(View view){
        Navigation.findNavController(view).navigate(R.id.action_viewEachJourneyFragment_to_navigation_home);
    }
}