package com.example.journeyjournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JourneyRVAdapter extends RecyclerView.Adapter<JourneyRVAdapter.ViewHolder> {
//    declare a variable
    private ArrayList<JourneyRVModal> journeyRVModalArrayList;
    private Context context;
    int lastPos = -1;
    private JourneyClickInterface journeyClickInterface;

//  set value in variable which pass as parameter to the function
    public JourneyRVAdapter(ArrayList<JourneyRVModal> journeyRVModalArrayList, Context context, JourneyClickInterface journeyClickInterface) {
        this.journeyRVModalArrayList = journeyRVModalArrayList;
        this.context = context;
        this.journeyClickInterface = journeyClickInterface;
    }
    @NonNull
    @Override
    public JourneyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        link the layout file
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_fact_item,parent,false
        );
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull JourneyRVAdapter.ViewHolder holder, int position) {
        JourneyRVModal journeyRVModal = journeyRVModalArrayList.get(position);
//        AddingValueInLayoutFromModal
//        set a value in recycler view list
        holder.journeyName.setText(journeyRVModal.getPlaceName());
        holder.journeyDescription.setText(journeyRVModal.getPlaceDescription());
        if(!journeyRVModal.getJourneyImage().isEmpty()){
            Picasso.get().load(journeyRVModal.getJourneyImage()).into(holder.getJourneyImage());
        }
        setAnimation(holder.itemView,position);
       //add action on list click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                journeyClickInterface.onListJourneyClick(position);
            }
        });
        // add action on Edit click
        holder.journeyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                journeyClickInterface.onEditJourneyClick(position);
            }
        });
        // add action on Delete click
        holder.journeyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                journeyClickInterface.onDeleteJourneyClick(position);
            }
        });
    }
//    function for animation to show list
    private void setAnimation(View itemView,int position ){
        if(position>lastPos){
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos=position;
        }
    }
    @Override
    public int getItemCount() {
       //SizeOfTheArrayList
        return journeyRVModalArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
//        declare a variable
        private TextView journeyName;
        private TextView journeyDescription;
//        private ImageView journeyImg;
        private Button journeyEdit;
        private Button journeyDelete;
        private ImageView journeyImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            set the layout id in variable
            journeyName = itemView.findViewById(R.id.Place_Name);
            journeyDescription = itemView.findViewById(R.id.Place_description);
//            journeyImg = itemView.findViewById(R.id.Journey_Image);
            journeyEdit = itemView.findViewById(R.id.view_edit_fragment_btn);
            journeyDelete = itemView.findViewById(R.id.delete_fragment_btn);
            journeyImage = itemView.findViewById(R.id.Journey_Image);
        }
//        return a Variable after id of layout declare
        public Button getJourneyDelete() {
            return journeyDelete;
        }
//        return a Variable after id of layout declare
        public TextView getJourneyName() {
            return journeyName;
        }
//        return a Variable after id of layout declare
        public Button getJourneyEdit() {
            return journeyEdit;
        }
//        return a Variable after id of layout declare
        public TextView getJourneyDescription() {
            return journeyDescription;
        }
//        return a Variable after id of layout declare
//        public ImageView getJourneyImg() {
//            return journeyImg;
//        }
//        return a Variable after id of layout declare
        public ImageView getJourneyImage() {
            return journeyImage;
        }
    }
    public interface JourneyClickInterface{
//        call a function where position as argument is pass
        void onListJourneyClick(int position);
        void onEditJourneyClick(int position);
        void onDeleteJourneyClick(int position);
    }
}
