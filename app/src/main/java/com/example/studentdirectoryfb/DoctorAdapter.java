package com.example.studentdirectoryfb;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DoctorAdapter extends FirebaseRecyclerAdapter<Doctor, DoctorAdapter.DoctorViewHolder> {

    public DoctorAdapter(@NonNull FirebaseRecyclerOptions<Doctor> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DoctorViewHolder holder, int i, @NonNull Doctor doctor) {
        holder.tvName.setText(doctor.getName());
        holder.tvDegree.setText(doctor.getDegree());
        holder.tvSpec.setText(doctor.getSpecialization());
        String key = getRef(i).getKey();


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v1) {
                AlertDialog.Builder add = new AlertDialog.Builder(v1.getContext());
                View v = LayoutInflater.from(v1.getContext())
                        .inflate(R.layout.add_doctor_form, null, false);
                add.setView(v);
                EditText etName = v.findViewById(R.id.etName);
                EditText etDegree = v.findViewById(R.id.etDegree);
                EditText etSpecialization = v.findViewById(R.id.etSpecialization);
                etName.setText(doctor.getName());
                etDegree.setText(doctor.getDegree());
                etSpecialization.setText(doctor.getSpecialization());

                add.setPositiveButton("Update", new DialogInterface.OnClickListener() {
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

                        assert key != null;
                        FirebaseDatabase.getInstance().getReference().child("Doctors")
                                .child(key)
                                .setValue(doctor)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(v1.getContext(), "Doctor updated....", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v1.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });

                add.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        assert key != null;
                        FirebaseDatabase.getInstance().getReference().child("Doctors")
                                .child(key)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(v1.getContext(), "Doctor deleted....", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v1.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                add.show();

                return false;
            }
        });

    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_doctor_item_design, parent, false);
        return new DoctorViewHolder(v);
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName, tvDegree, tvSpec;
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDegree = itemView.findViewById(R.id.tvDoctorDegree);
            tvName = itemView.findViewById(R.id.tvDoctorName);
            tvSpec = itemView.findViewById(R.id.tvDoctorSpecialization);
        }
    }
}
