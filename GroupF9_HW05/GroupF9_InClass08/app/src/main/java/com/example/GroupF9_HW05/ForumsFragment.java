package com.example.GroupF9_HW05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ForumsFragment extends Fragment {
    private FirebaseAuth mAuth;
    final String TAG = "demo";
    public ForumListener listener;
    ArrayList<Forum> forumList = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();
    ForumsRecyclerViewAdapter adapter = new ForumsRecyclerViewAdapter(forumList);


    public ForumsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    RecyclerView recyclerView;

    //need recycler adapter
    //need arraylist of forum
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forums, container, false);

        // recyclerView = view.findViewById(R.id.recyclerView);
        view.findViewById(R.id.btn_Logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                listener.logout();
            }
        });
        view.findViewById(R.id.btn_NewForum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.goCreateForum();
            }
        });


        //get forum data
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("ForumsN").get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Forum forum = document.toObject(Forum.class);
                                    forumList.add(forum);
                                }
                            } else {
                                Toast.makeText(getContext(), "There are no forums at this time", Toast.LENGTH_LONG).show();
                            }

                            //set up recyclerview
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setHasFixedSize(true);
                                    ForumsRecyclerViewAdapter adapter = new ForumsRecyclerViewAdapter(forumList);
                                    recyclerView.setAdapter(adapter);

                                    db.collection("ForumsN")
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    //clear and then repopulate to not add the whole list each change of forum post
                                                    forumList.clear();
                                                    //uses value instead of queryDocumentSnapshot
                                                    for (QueryDocumentSnapshot document : value) {
                                                        //but you need getters and setters in User class if casting document to object of User.class

                                                        Forum forum = document.toObject(Forum.class);
                                                        forumList.add(forum);

                                                        //ALTERNATE WAYS TO MONITOR AND UPDATE USERS USE ABOVE
                                                        Log.d(TAG, "onComplete: Document Id : " + document.getId());
                                                        Log.d(TAG, "onComplete: Document Data : " + document.getData());
                                                    }
                                                    //lastly, notify the adapter that the data changed
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                } //end of run
                            });

                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return view;
    } //end onCreateView


    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle(R.string.title_Forums);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ForumListener) {
            listener = (ForumListener) context;
        } else {
            //"Must implement CitiesList Interface"
            throw new RuntimeException(String.valueOf(R.string.rtForums));
        }

    }

    public void delete(Forum forum){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //forum.getDocId() == not null, has values
        db.collection("ForumsN").document(forum.getDocId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public interface ForumListener {
        void goCreateForum();
        void goComments(String forumID);
        void logout();
    }

    public interface ForumsRecyclerViewClickListener {
        public void forumsRecyclerViewListClicked(View v, int position);
    }

    public class ForumsRecyclerViewAdapter extends RecyclerView.Adapter<ForumsRecyclerViewAdapter.MyViewHolder> {
        //need arraylist of forum
        ArrayList<Forum> forums;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = mAuth.getCurrentUser();
        String fbUserString = fbUser.getUid();

        public ForumsRecyclerViewAdapter(ArrayList<Forum> forums) {
            this.forums = forums;
        }


        @NonNull
        @Override
        public ForumsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ForumsRecyclerViewAdapter.MyViewHolder holder, int position) {
            Forum forum = forums.get(position);
            holder.tv_Title.setText(forum.getForumTitle());
            holder.tv_Name.setText(forum.getName());
            holder.tv_PostDesc.setText(forum.getPost());
            holder.tv_Date.setText(String.valueOf(forum.getDate()));

            //NUM LIKES SET - need string.value of since size is already a string but causes crashes
            holder.tv_numLikes.setText(forum.getLikeHashMap().size() + " Likes |");

            //TRASH - get unique value of user id
            if (fbUserString.equals(forum.getuID())) {
                holder.iv_Trash.setVisibility(View.VISIBLE);
            } else {
                holder.iv_Trash.setVisibility(View.INVISIBLE);
            }
            //TRASH - onClick()
            holder.iv_Trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(forum);
                }
            });
            //FORUM / COMMENT - onClick()
            holder.forumContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.goComments(forum.getDocId());
                }
            });

            //LIKE - get unique value of user id
            if (forum.likeHashMap.containsKey(fbUserString)) {
                holder.iv_likeCheck.setImageResource(R.drawable.like_favorite);
            } else {
                holder.iv_likeCheck.setImageResource(R.drawable.like_not_favorite);
            }

            //LIKE CHECK - onClick()
            holder.iv_likeCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //NEED TO UPDATE DATABASE AFTER UPDATING HASHMAP
                    //if map contains user, remove the filled heart, else add
                    if (forum.likeHashMap.containsKey(fbUserString)) {
                        //remove like
                        forum.likeHashMap.remove(fbUserString);
                    } else {
                        //add like
                        forum.likeHashMap.put(fbUserString, true);
                    }
                    //DB INSTANCE - update firebase then the remove will work
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    //LikeRef - Need to update likeHashMap in real time
                    DocumentReference likeRef = db.collection("ForumsN").document(forum.getDocId());
                    likeRef
                            .update("likeHashMap", forum.getLikeHashMap())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) { Log.d(TAG, "onSuccess: YES"); }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) { Log.d(TAG, "onFailure: NO"); }
                            });
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.forums.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_Title, tv_Name, tv_PostDesc, tv_Date, tv_numLikes;
            ImageView iv_Trash, iv_likeCheck;
            ConstraintLayout forumContainer;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                forumContainer = itemView.findViewById(R.id.forumContainer);

                tv_Title = itemView.findViewById(R.id.tv_PostTitle);
                tv_Name = itemView.findViewById(R.id.tv_Name);
                tv_PostDesc = itemView.findViewById(R.id.tv_PostDesc);
                tv_Date = itemView.findViewById(R.id.tv_Date);
                tv_numLikes = itemView.findViewById(R.id.tv_numLikes);

                iv_Trash = itemView.findViewById(R.id.iv_Trash);
                iv_likeCheck = itemView.findViewById(R.id.iv_Likes);
            }
        }


    }
}