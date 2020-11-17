package id.ac.unud1805551038.projectprogmob;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import id.ac.unud1805551038.projectprogmob.Fragments.SignInFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignInFragment()).commit();
    }
}
