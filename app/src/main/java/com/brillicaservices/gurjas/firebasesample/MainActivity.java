package com.brillicaservices.gurjas.firebasesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends
        AppCompatActivity
        implements
        RecyclerAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getName();
    DatabaseReference databaseReference;

    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<StudentModel>
            studentModelArrayList =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar =
                findViewById(R.id.
                        recycler_progress_bar);
        recyclerView = findViewById(
                R.id.recycler_view);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager
                (linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new
                RecyclerAdapter(
                        studentModelArrayList,
                this);

        recyclerView.setAdapter(recyclerAdapter);

        databaseReference = FirebaseDatabase.
                getInstance().getReference();

        databaseReference.
                child("Students").
                addValueEventListener
                        (new ValueEventListener() {

            @Override
            public void onDataChange
                    (DataSnapshot dataSnapshot) {

                Log.d(TAG ,"Child count: " + dataSnapshot);

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                    Log.d(TAG, "Student Name is: " + snapshot.child("name").getValue());
//                    Log.d(TAG, "Student College is: " + snapshot.child("college").getValue());
//                    Log.d(TAG, "Student Address is: " + snapshot.child("address").getValue());
//                    Log.d(TAG, "Student Fees is: " + snapshot.child("fees").getValue());

                    String studentName = String.valueOf(snapshot.child("name").getValue());
                    String collegeName = String.valueOf(snapshot.child("college").getValue());
                    String address = String.valueOf(snapshot.child("address").getValue());
                    int fees = Integer.parseInt(snapshot.child("fees").getValue().toString());

                    studentModelArrayList.add
                            (new StudentModel(studentName, address, collegeName, fees));
                }

                recyclerAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled
                    (DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onListItemClickListener(int clickedItemIndex) {
        Toast.makeText(getApplicationContext(), studentModelArrayList.get(clickedItemIndex).name, Toast.LENGTH_SHORT).show();
    }
}
