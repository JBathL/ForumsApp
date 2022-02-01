package com.example.GroupF9_HW05;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterFragment extends Fragment {
    private FirebaseAuth mAuth;
    final String TAG = "demo";
    public RegisterListener listener;



    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    EditText et_Name, et_Password, et_EmailReg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        et_Name = view.findViewById(R.id.et_Name);
        et_Password = view.findViewById(R.id.et_Pass);
        et_EmailReg = view.findViewById(R.id.et_Email);

        view.findViewById(R.id.btn_SubmitCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_EmailReg.getText().toString();
                String password = et_Password.getText().toString();
                String name = et_Name.getText().toString();
                //check if main thread or not
                Log.d(TAG, "onClick: Thread ID: " + Thread.currentThread().getId());

                if (email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Email is empty")
                            .setMessage("Please enter an email")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which){

                                }
                            });
                    builder.create().show();
                } else if (password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Password is empty")
                            .setMessage("Please enter a password")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which){

                                }
                            });
                    builder.create().show();
                } else if (name.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Name is empty")
                            .setMessage("Please enter a name")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which){

                                }
                            });
                    builder.create().show();
                }
                else if (email.isEmpty() && password.isEmpty() && name.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Email, password, and name are empty")
                            .setMessage("Please enter an email, password, and name")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which){

                                }
                            });
                    builder.create().show();
                }
                //if else
                else {
                    mAuth = FirebaseAuth.getInstance();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser fireUser = mAuth.getCurrentUser();

                                        UserProfileChangeRequest setDisplayName = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name).build();

                                        fireUser.updateProfile(setDisplayName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });

                                        User user = new User();
                                        user.setEmail(email);
                                        user.setName(name);
                                        user.setPassword(password);
                                        user.setuID(mAuth.getCurrentUser().getUid());
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("Users")
                                                .add(user)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        listener.goForums();
                                                    }
                                                });
                                        Log.d(TAG, "onComplete: Logged in Successfully");
                                        //user is stored as a global variable in mAuth
                                        Log.d(TAG, "onComplete: mAuth Current User: " + mAuth.getCurrentUser().getUid());


                                    } else {
                                        Log.d(TAG, "onComplete: Error !!");
                                        Log.d(TAG, "onComplete: " + task.getException().getMessage());

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle(task.getException().getMessage())
                                                .setMessage("Please try again")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which){
                                                        Log.d(TAG, "onClick: gets here");
                                                    }
                                                });
                                        builder.create().show();
                                    }

                                }
                            });

                }

            }
        });

        view.findViewById(R.id.tv_CancelCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.goLogin();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle(R.string.title_Register);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof RegisterListener) {
            listener = (RegisterListener) context;
        } else {
            //"Must implement CitiesList Interface"
            throw new RuntimeException(String.valueOf(R.string.rtRegister));
        }

    }

    public interface RegisterListener {
        void goLogin();
        void goForums();
    }
}