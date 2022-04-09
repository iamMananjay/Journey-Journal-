package com.example.journeyjournal.presentation;

import android.os.Bundle;
import android.view.View;

import com.example.journeyjournal.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.journeyjournal.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {
//    Declare a Variable
    private ActivityDashboardBinding binding;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    private NavController navController;
//    OnCreate callback function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        call a function
        initView();
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_dashboard);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
        setUpToolBarWithNavigationComponent();
        showViewsBasedOnNavigationArguments();
    }
    private void initView(){
       // initialize the layout in variable
        toolbar = findViewById(R.id.toolbar_dashboard);
        bottomNavigationView = findViewById(R.id.nav_view);
    }
    private  void setUpToolBarWithNavigationComponent(){
        if(navHostFragment !=null){
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    navController.getGraph())
                    .build();
            NavigationUI.setupWithNavController(
                    toolbar, navController, appBarConfiguration);
        }
    }
//    function to switch the different fragment as click on Bottom Navigation
    private void showViewsBasedOnNavigationArguments(){
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(
                    @NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(arguments == null){
                    return;
                }
                showToolbarBasedOnArguments(arguments);
                showBottomNavigationViewBasedOnArguments(arguments);
            }
        });

    }
//    function to show or hide toolbar
    private  void showToolbarBasedOnArguments(Bundle arguments){
        boolean showToolBar = arguments.getBoolean(
                getString(R.string.show_Toolbar),
                false
        );
        if(showToolBar){
            toolbar.setVisibility(View.VISIBLE);

        }
        else{
            toolbar.setVisibility(View.GONE);
        }
    }
//    function to show or hide BottomNavigation
    private void showBottomNavigationViewBasedOnArguments(Bundle arguments){
        boolean showBottomNavigationView = arguments.getBoolean(
                getString(R.string.show_Navigation),false
        );
        if(showBottomNavigationView){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        else{
            bottomNavigationView.setVisibility(View.GONE);
        }

    }
}