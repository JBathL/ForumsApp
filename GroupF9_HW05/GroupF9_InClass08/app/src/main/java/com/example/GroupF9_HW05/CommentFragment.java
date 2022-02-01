package com.example.GroupF9_HW05;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;


public class CommentFragment extends Fragment {
    final String TAG = "comment";
    ArrayList<Comment> commentList;
    CommentsRecyclerViewAdapter adapter;
    EditText editTextComment;
    TextView tv_ForumTitle, tv_ForumName, tv_ForumDesc, commentNumberTV;
    private FirebaseAuth mAuth;
    private static final String ARG_ID = "ARG_ID";
    String id;


    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment newInstance(String id) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = (String) getArguments().getSerializable(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        editTextComment = view.findViewById(R.id.editTextComment);
        tv_ForumTitle = view.findViewById(R.id.tv_ForumTitle);
        tv_ForumName = view.findViewById(R.id.tv_ForumName);
        tv_ForumDesc = view.findViewById(R.id.tv_ForumDesc);
        commentNumberTV = view.findViewById(R.id.commentNumberTV);

        initRecycler(view);

        //POST COMMENT - onClick()
        view.findViewById(R.id.btn_commentPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postedComment = editTextComment.getText().toString();

                //alert if comment empty
                if (postedComment.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Comment is empty")
                            .setMessage("Please enter your comment")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                } else {
                    createComment();
                    Toast.makeText(getContext(), "Comment Posted", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //FORUM DATA - getForumData()
        getForum();
        getComments();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.title_Forum);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void initRecycler(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewComments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        commentList = new ArrayList<>();
        adapter = new CommentsRecyclerViewAdapter(commentList);
        recyclerView.setAdapter(adapter);
    }

    public void createComment() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Comment comment = new Comment();
        comment.setcText(editTextComment.getText().toString());
        comment.setDate(new Date());
        comment.setName(user.getDisplayName());
        comment.setuId(user.getUid());

        db.collection("ForumsN")
                .document(id)
                .collection("Comments")
                .add(comment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
//                        commentList.add(comment);

                        adapter.notifyDataSetChanged();
                    }

                });
    }


    //GET FORUM DATA - getForum()
    public void getForum() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("ForumsN")
                .document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        tv_ForumDesc.setText(value.getString("post"));
                        tv_ForumDesc.setText(value.getString("forumTitle"));
                        tv_ForumDesc.setText(value.getString("name"));
                    }
                });

    }

    public void getComments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //COMMENT DATA - getComments()
        db.collection("ForumsN")
                .document(id)
                .collection("Comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        commentList.clear();

                        for (QueryDocumentSnapshot document : value) {
                            Comment comment = document.toObject(Comment.class);
                            commentList.add(comment);

                            if (commentList.isEmpty()) {
                                commentNumberTV.setText(String.valueOf(0) + " comments");
                            }
                            if (commentList.size() == 1) {
                                commentNumberTV.setText(String.valueOf(commentList.size()) + " comment");
                            } else {
                                commentNumberTV.setText(String.valueOf(commentList.size()) + " comments");
                            }

                            //ALTERNATE WAYS TO MONITOR AND UPDATE USERS USE ABOVE
                            Log.d(TAG, "onComplete: Comment Doc Id : " + document.getId());
                            Log.d(TAG, "onComplete: Comment Doc Data : " + document.getData());
                        }
                        //lastly, notify the adapter that the data changed
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    private void delete(Comment comment) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ForumsN").document(id).collection("Comments")
                .document(comment.getDocId()) //getDocId = null for newly created comments, not null for older comments
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Click");
                        commentList.remove(comment);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed Click");
                        e.printStackTrace();
                    }
                });
    }

    public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.MyViewHolder> {
        ArrayList<Comment> comments;
        FirebaseAuth mAuth2 = FirebaseAuth.getInstance();
        FirebaseUser fbUser = mAuth2.getCurrentUser();
        String fbUserString = fbUser.getUid();

        public CommentsRecyclerViewAdapter(ArrayList<Comment> comments) {
            this.comments = comments;
        }

        @NonNull
        @Override
        public CommentsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull CommentsRecyclerViewAdapter.MyViewHolder holder, int position) {
            Comment comment = comments.get(position);
            holder.tv_CommentName.setText(comment.getName());
            holder.tv_CommentDesc.setText(comment.getcText());
            holder.tv_CommentDate.setText(String.valueOf(comment.getDate()));

            //TRASH - get unique value of user id
            if (fbUserString.equals(comment.getuId())) {
                holder.iv_Trash2.setVisibility(View.VISIBLE);
            } else {
                holder.iv_Trash2.setVisibility(View.INVISIBLE);
            }
            //TRASH - onClick()
            holder.iv_Trash2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(comment);
                }

            });

        }

        @Override
        public int getItemCount() {
            return this.comments.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_CommentName, tv_CommentDesc, tv_CommentDate;
            ImageView iv_Trash2;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_CommentName = itemView.findViewById(R.id.tv_CommentName);
                tv_CommentDesc = itemView.findViewById(R.id.tv_CommentStuff);
                tv_CommentDate = itemView.findViewById(R.id.tv_CommentDate);

                iv_Trash2 = itemView.findViewById(R.id.iv_Trash2);
            }
        }
    }
}