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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    final String TAG = "demo";
    public LoginListener listener;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    EditText et_Email, et_Pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        et_Email = view.findViewById(R.id.et_Email);
        et_Pass = view.findViewById(R.id.et_Pass);

        view.findViewById(R.id.btn_Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_Email.getText().toString();
                String password = et_Pass.getText().toString();
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

                } else if (email.isEmpty() && password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Email and password are empty")
                            .setMessage("Please enter an email and password")
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

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //safe to transition if log current thread is the same
                                    Log.d(TAG, "onComplete: Thread ID: " + Thread.currentThread().getId());
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: Logged in Successfully");
                                        //user is stored as a global variable in mAuth
                                        Log.d(TAG, "onComplete: mAuth Current User: " + mAuth.getCurrentUser().getUid());

                                        listener.forums();

                                    } else {

                                        Log.d(TAG, "onComplete: Error !!");
                                        Log.d(TAG, "onComplete: " + task.getException().getMessage());

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Login failed")
                                                .setMessage("Try again later.")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which){

                                                    }
                                                });
                                        builder.create().show();
                                    }
                                }
                            });
                }

            }
        });

        view.findViewById(R.id.tv_CreateAcct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.createAccount();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.title_Login);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof LoginListener) {
            listener = (LoginListener) context;
        } else {
            //"Must implement CitiesList Interface"
            throw new RuntimeException(String.valueOf(R.string.rtLogin));
        }

    }


    public interface LoginListener {
        void createAccount();
        void forums();
    }
}