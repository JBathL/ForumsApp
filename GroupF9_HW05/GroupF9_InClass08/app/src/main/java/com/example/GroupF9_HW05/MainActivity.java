package com.example.GroupF9_HW05;
//GroupF9_HW05
//Jacob Mack
//Jenna Bath
//GroupF9_HW05
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements CreateForumFragment.CreateListener, RegisterFragment.RegisterListener, LoginFragment.LoginListener , ForumsFragment.ForumListener {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //null = not logged in
        if (mAuth.getCurrentUser() == null) {
            //load loginfragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, new LoginFragment())
                    .commit();
        } else {
            //load MainFragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, new ForumsFragment())
                    .commit();
        }

    }

    @Override
    public void successfulCreate() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new ForumsFragment())
                .addToBackStack(null)
                .commit();
        //popToBackStack();
    }

    @Override
    public void cancelCreate() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new ForumsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void createAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new RegisterFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void forums() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new ForumsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goForums() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new ForumsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goCreateForum() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new CreateForumFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goComments(String forumID) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, CommentFragment.newInstance(forumID))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void logout() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }
}