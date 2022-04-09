package com.example.journeyjournal.presentation.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.journeyjournal.R;
import com.example.journeyjournal.databinding.FragmentNotificationsBinding;
import com.example.journeyjournal.presentation.login.LoginActivity;

public class NotificationsFragment extends Fragment {
    private Button logout;
    private ImageView profileBackIcon;

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        call a function and pass argument as root
        initViews(root);
        initBackAction();
//        call a function for logout bottom
        initLogoutAction();
        return root;
    }
    private void initViews(View root){
//        set layout id in variable
        logout = root.findViewById(R.id.logout_btn);
        profileBackIcon= root.findViewById(R.id.back_arrow);
    }
//    Add action on logout bottom click
    private  void  initLogoutAction() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutClicked(view);
            }
        });
    }
    private  void  onLogoutClicked(View view){
        Intent intent = new Intent(requireActivity(),LoginActivity.class);
        startActivity(intent);
    }
    private void initBackAction(){
        profileBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick(view);
            }
        });
    }

    private void  onBackClick(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_navigation_home);
    }
//    call back function on destroy view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}