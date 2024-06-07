package com.example.studentdirectoryfb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabAdd;
    RecyclerView rvDoctors;

    DatabaseReference reference;
    DoctorAdapter adapter;

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDoctor();
            }
        });



    }

    private void addNewDoctor()
    {
        AlertDialog.Builder add = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this)
                .inflate(R.layout.add_doctor_form, null, false);
        add.setView(v);
        EditText etName = v.findViewById(R.id.etName);
        EditText etDegree = v.findViewById(R.id.etDegree);
        EditText etSpecialization = v.findViewById(R.id.etSpecialization);

        add.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString().trim();
                String degree = etDegree.getText().toString().trim();
                String specialization = etSpecialization.getText().toString().trim();

                if(name.isEmpty())
                {
                    etName.setError("Name can't be empty");
                    return;
                }

                if(degree.isEmpty())
                {
                    etDegree.setError("Degree can't be empty");
                    return;
                }

                if(specialization.isEmpty())
                {
                    etSpecialization.setError("Specialization can't be empty");
                    return;
                }

                HashMap<String, Object> doctor = new HashMap<>();
                doctor.put("name", name);
                doctor.put("degree", degree);
                doctor.put("specialization", specialization);

                reference.child("Doctors")
                        .push()
                        .setValue(doctor)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Doctor Added....", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        add.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        add.show();

    }

    private void init()
    {
        fabAdd = findViewById(R.id.fabAdd);
        reference = FirebaseDatabase.getInstance().getReference();
        rvDoctors = findViewById(R.id.rvDoctors);

        FirebaseRecyclerOptions<Doctor> options
                = new FirebaseRecyclerOptions.Builder<Doctor>()
                .setQuery(reference.child("Doctors"), Doctor.class)
                .build();

        adapter = new DoctorAdapter(options);
        rvDoctors.setHasFixedSize(true);
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));
        rvDoctors.setAdapter(adapter);


    }
}