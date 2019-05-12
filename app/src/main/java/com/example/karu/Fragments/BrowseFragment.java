package com.example.karu.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karu.Adapters.ImageAdapter;
import com.example.karu.AditionalActivities.SearchActivity;
import com.example.karu.Model.Upload;
import com.example.karu.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BrowseFragment extends Fragment {

    private static final String TAG = "BrowseFragment";

    private TextView screenNameTextView;
    // ImagesActivity
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private Button mSearchButton;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_browse, container, false);

        mRecyclerView = v.findViewById(R.id.recycle_view_browse);
        mRecyclerView.setHasFixedSize(true);// not incease size, so we get max performance
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchButton = v.findViewById(R.id.fragment_browse_search_button);

        mProgressCircle = (ProgressBar) v.findViewById(R.id.fragement_browse_progress_circle);

        mUploads = new ArrayList<>(); // List is an interface
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchProductName();

            }
        });

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // loop through the uploads table using for loop
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // fill mUploads list with data from database uploads
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }

                // create adapter to put the list at
                mAdapter = new ImageAdapter(getActivity(), mUploads);

                //fill recyclerVIew with data
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // throw error message
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

        return v;
    }


    private void searchProductName() {
        /*Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);*/

        /*FirebaseRecyclerOptions<Upload> options = new FirebaseRecyclerOptions.Builder<Upload>()
                .setQuery(mDatabaseRef,Upload.class)
                .build();


        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Upload, ImageAdapter.ImageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position, @NonNull Upload model) {

            }

            @Override
            public ImageAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.image_item, parent, false);

                return new ImageAdapter.ImageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ImageAdapter.ImageViewHolder holder, int position, image_details model) {
                // Bind the image_details object to the BlogViewHolder
                // ...
            }
        };*/

    }
}

