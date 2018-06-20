package com.nulp.rostykboiko.aicalendar.login;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nulp.rostykboiko.aicalendar.R;
import com.nulp.rostykboiko.aicalendar.main.MainActivity;
import com.nulp.rostykboiko.aicalendar.utils.GoogleAccountAdapter;
import com.nulp.rostykboiko.aicalendar.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth;
    private AuthCredential credential;
    private SessionManager session;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBarDim(true);

        session = new SessionManager(getApplicationContext());

        ButterKnife.bind(this);

        googleAuth();
        initFireBaseListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void setStatusBarDim(boolean dim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(dim ? Color.TRANSPARENT :
                    ContextCompat.getColor(this, getThemedResId(R.attr.colorPrimaryDark)));
        }
    }

    private int getThemedResId(@AttrRes int attr) {
        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{attr});
        int resId = typedArray.getResourceId(0, 0);
        typedArray.recycle();
        return resId;
    }

    private void handleSignInResult(GoogleSignInResult result) {
        GoogleSignInAccount acct = result.getSignInAccount();
        if (acct != null && acct.getId() != null) {
            credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential);
            getUserData(acct);
        }
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void getUserData(GoogleSignInAccount acct) {
        GoogleAccountAdapter accountAdapter = new GoogleAccountAdapter();
        accountAdapter.setAccountID(acct.getId());
        accountAdapter.setUserName(acct.getDisplayName());
        accountAdapter.setUserEmail(acct.getEmail());
        accountAdapter.setProfileIcon(acct.getPhotoUrl().toString());
        accountAdapter.setDeviceToken(FirebaseInstanceId.getInstance().getToken());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userList = database.getReference("User");

        userList.child(acct.getId()).child("Email").setValue(acct.getEmail());
        userList.child(acct.getId()).child("Name").setValue(acct.getDisplayName());
        userList.child(acct.getId()).child("accountID").setValue(acct.getId());
        userList.child(acct.getId()).child("ProfileIconUrl").setValue(acct.getPhotoUrl().toString());
        userList.child(acct.getId()).child("deviceToken")
                .setValue(FirebaseInstanceId.getInstance().getToken());

        System.out.println("mAuth.getCurrentUser()" + mAuth.getCurrentUser());

        session.createLoginSession(
                acct.getDisplayName(),
                acct.getEmail(),
                acct.getId(),
                FirebaseInstanceId.getInstance().getToken(),
                acct.getPhotoUrl().toString());
    }

    private void initFireBaseListener() {
        mAuth = FirebaseAuth.getInstance();
        new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @OnClick(R.id.button)
    public void signIn(){
        Log.w(TAG, "Google sign button");
        System.out.print("LoginActivity - Google sign button");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void googleAuth(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("354392581884-2q1f20u839qqlu2mep8k6oq59ibj2nov.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    handleSignInResult(result);
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


    private void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    private void updateUI(UserInfo currentUser){

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
