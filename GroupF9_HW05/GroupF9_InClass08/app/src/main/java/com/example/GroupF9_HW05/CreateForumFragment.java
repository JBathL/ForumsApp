package com.example.GroupF9_HW05;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;


public class CreateForumFragment extends Fragment {
    private FirebaseAuth mAuth;
    ArrayList<User> users = new ArrayList<>();
    final String TAG = "demo";
    public CreateListener listener;


    public CreateForumFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    EditText et_Title, et_ForumDesc, et_Name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_forum, container, false);

        et_Title = view.findViewById(R.id.et_Title);
        et_ForumDesc = view.findViewById(R.id.et_ForumDesc);

        //CREATE COMMENT - onClick()
        view.findViewById(R.id.btn_SubmitCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_Title.getText().toString();
                String description = et_ForumDesc.getText().toString();

                if (title.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Forum title empty")
                            .setMessage("Please enter a title")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which){

                                }
                            });
                    builder.create().show();

                } else if (description.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Forum Description empty")
                            .setMessage("Please enter a description")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which){

                                }
                            });
                    builder.create().show();

                } else if (description.isEmpty() && title.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Description and title empty")
                            .setMessage("Please enter a description and title")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which){

                                }
                            });
                    builder.create().show();

                } else if (!description.isEmpty() && !title.isEmpty() ){
                    createForum();
                }
            }
        }); //end of submit button

        view.findViewById(R.id.tv_CancelCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cancelCreate();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle(R.string.title_Create);
    }

    public void createForum(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Forum forum = new Forum();
        forum.setForumTitle(et_Title.getText().toString());
        forum.setPost(et_ForumDesc.getText().toString());
        forum.setDate(new Date());
        forum.setName(user.getDisplayName());
        forum.setuID(mAuth.getCurrentUser().getUid());

        db.collection("ForumsN")
                .add(forum)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                    @Override
                    public void onSuccess(DocumentReference documentReference){
                        listener.successfulCreate();
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof CreateListener) {
            listener = (CreateListener) context;
        } else {
            //"Must implement CitiesList Interface"
            throw new RuntimeException(String.valueOf(R.string.rtCreateForum));
        }

    }

    public interface CreateListener {
        void successfulCreate();
        void cancelCreate();
    }

}