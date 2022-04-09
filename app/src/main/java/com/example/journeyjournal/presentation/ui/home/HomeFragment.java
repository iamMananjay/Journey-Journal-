package com.example.journeyjournal.presentation.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journeyjournal.EditJourneyFragment;
import com.example.journeyjournal.JourneyRVAdapter;
import com.example.journeyjournal.JourneyRVModal;
import com.example.journeyjournal.R;
import com.example.journeyjournal.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements JourneyRVAdapter.JourneyClickInterface{
    private final static String tagName = "HomeFragment";

//    declare a variable
    private FragmentHomeBinding binding;
    private FloatingActionButton SwitchToEachJourney;
    private Button editJourney;
    private Button viewJourney;

   //variable declare for recycler view
    private RecyclerView journeyList;
    private ProgressBar listLoading;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<JourneyRVModal> journeyRVModalArrayList;
    private RelativeLayout homeFragmentRelativeLayout;
    private JourneyRVAdapter journeyRVAdapter;

//    onCreateView Function
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        call a function and pass root as argument
        initWidgets(root);
//        call for buttom action
        initButtonAction();
        getAllJourney();
        return root;
    }
    private  void  initWidgets(View root){
        //set layout id in variable
        SwitchToEachJourney = root.findViewById(R.id.Add_Journey);
        journeyList = root.findViewById(R.id.Journey_List);
        listLoading = root.findViewById(R.id.List_Loading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference("Journey");
        journeyRVModalArrayList = new ArrayList<>();
        journeyRVAdapter = new JourneyRVAdapter(journeyRVModalArrayList,requireActivity(),this);
        //VerticalWay
        journeyList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        journeyList.setAdapter(journeyRVAdapter);
    }
//    Add action to add journey
    private void initButtonAction(){
        SwitchToEachJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEachJourney(view);
            }
        });
    }

    private void getAllJourney()
    {
        listLoading.setVisibility(View.VISIBLE);
        journeyRVModalArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                listLoading.setVisibility(View.GONE);
                journeyRVModalArrayList.add(snapshot.getValue(JourneyRVModal.class));
                journeyRVAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                listLoading.setVisibility(View.GONE);
                journeyRVAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                listLoading.setVisibility(View.GONE);
//                journeyRVModalArrayList.remove(snapshot.getValue(JourneyRVModal.class));
                journeyRVAdapter.notifyDataSetChanged();
//                journeyList.setAdapter(journeyRVAdapter);
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                listLoading.setVisibility(View.GONE);
                journeyRVAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

//    switch to add journey from list of journey
    private void onEachJourney(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_addJourneyDetailFragment);
        Log.d(tagName, "onEachJourney: ok ");
    }
//    onDestroy function
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
//    Add action on each list click and navigate to each list view
    @Override
    public void onListJourneyClick(int position) {
        JourneyRVModal journeyRVModal = journeyRVModalArrayList.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("journeyrvmodal", journeyRVModal);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_home_to_viewEachJourneyFragment, bundle);
    }
//    Add action on edit bottom click
    @Override
    public void onEditJourneyClick(int position) {
        JourneyRVModal journeyRVModal = journeyRVModalArrayList.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("journeyrvmodal", journeyRVModal);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_home_to_editJourneyFragment, bundle);
    }
//    Add action on delete bottom click
    @Override
    public void onDeleteJourneyClick(int position){
        JourneyRVModal journeyRVModal = journeyRVModalArrayList.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("journeyrvmodal", journeyRVModal);
        databaseReference = firebaseDatabase.getReference("Journey").child(journeyRVModal.getJourneyId());
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void task) {
               journeyRVModalArrayList.remove(journeyRVModal);
                journeyRVAdapter.notifyDataSetChanged();
                Toast.makeText(requireActivity(), "Journey Delete Successfully", Toast.LENGTH_SHORT).show();

            }
        });
//        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_home_to_editJourneyFragment, bundle);
//        databaseReference.removeValue();
//        startActivity(new Intent(requireActivity(),HomeFragment.class));
    }
}