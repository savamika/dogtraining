package com.healthcare.dogtraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.healthcare.dogtraining.API.Base_Url;
import com.healthcare.dogtraining.utils.PrefrenceManager;
import com.healthcare.dogtraining.utils.Session1;
import com.healthcare.dogtraining.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.healthcare.dogtraining.API.Base_Url.BaseUrl;
import static com.healthcare.dogtraining.API.Base_Url.Login;
import static com.healthcare.dogtraining.API.Base_Url.facebook_login;
import static com.healthcare.dogtraining.API.Base_Url.google_login;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    LinearLayout forgotpassword;
    Button signIn_signInButton;
    EditText et_emailmobile,et_password;
    private RequestQueue rQueue;
    Session1 session1;
    ImageView signIn_google,img_facebook;
    GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    String idToken,name,email,googleid_id;
    private static final String TAG = "MainActivity";
    private FirebaseAuth.AuthStateListener authStateListener;
    PrefrenceManager prefrenceManager;
    String FCM_ID;
    LoginButton loginButton;
    String fbid,fbname,fbemail,birthday,gender;
    RelativeLayout progress_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress_view= findViewById(R.id.progress_view);
        forgotpassword= findViewById(R.id.forgotpassword);
        signIn_signInButton= findViewById(R.id.signIn_signInButton);
        et_emailmobile= findViewById(R.id.et_emailmobile);
        et_password= findViewById(R.id.et_password);
        signIn_google= findViewById(R.id.signIn_google);
        img_facebook= findViewById(R.id.img_facebook);
        loginButton = findViewById(R.id.loginButton);
        session1=new Session1(LoginActivity.this);
        callbackManager = CallbackManager.Factory.create();
        prefrenceManager=new PrefrenceManager(LoginActivity.this);
        FCM_ID=  prefrenceManager.getTokenId(LoginActivity.this);

        firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();


        authStateListener = new FirebaseAuth.AuthStateListener() {
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

        img_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {@Override
                public void onSuccess(LoginResult loginResult) {

                    System.out.println("onSuccess");

                    String accessToken = loginResult.getAccessToken()
                            .getToken();
                    Log.i("accessToken", accessToken);

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {@Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {

                                Log.i("LoginActivity",
                                        response.toString());
                                try {
                                    fbid = object.getString("id");
                                    String profile_pic = "http://graph.facebook.com/" + fbid + "/picture?type=large";
//                                    try {
//                                        URL profile_pic = new URL(
//                                                "http://graph.facebook.com/" + id + "/picture?type=large");
//                                        Log.i("profile_pic",
//                                                profile_pic + "");
//
//                                    } catch (MalformedURLException e) {
//                                        e.printStackTrace();
//                                    }
                                    fbname = object.getString("name");

                                    fbemail = "";

                                    facebookLogin(fbid,fbname,fbemail);
                                    try {
                                        fbemail = object.getString("email");

                                        System.out.println("facebook======     "+fbemail+fbname+profile_pic);
                                    }catch (Exception e){}
//                                    gender = object.getString("gender");
//                                    birthday = object.getString("birthday");
                                    if (fbemail.isEmpty())

                                        System.out.println("facebook======     "+fbemail+fbname+profile_pic);
//                                    checkLoginToServer(email,name,profile_pic,"1");

                                  //
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields",
                            "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
//                    startActivity(new Intent(LoginActivity.this,LangChooseActivity.class));

                }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.d("FB_ERROR", "onError: "+exception.toString());
                       // Toast.makeText(LoginActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            findViewById(R.id.forgotpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetEmailActivity.class);
                startActivity(intent);
                finish();
                 }
                 });

              img_facebook.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   loginButton.performClick();
            }
        });



            findViewById(R.id.signIn_signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallLoginApi(FCM_ID);
                }
                });

            signIn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 100);
            }

         });

/*
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 100);
            }
        });*/

         }

    private void facebookLogin(String fbid, String fbname, String fbemail) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + facebook_login,

                    new Response.Listener<String>() {

                        @Override

                        public void onResponse(String response) {

                            try {

                                JSONObject obj = new JSONObject(response);

                                System.out.println("checkl"+response);

                                System.out.println("checkl"+obj);

                                String result = obj.getString("result");

                                String msg = obj.getString("msg");

                                JSONObject obj1 = obj.getJSONObject("data");

                                if (result.equals("true")) {
                                    String id = obj1.getString("id");
                                    String fullname = obj1.getString("fullname");
                                    String email = obj1.getString("email");
                                    String mobile = obj1.getString("mobile");

                                    session1.setLogin(true);
                                    session1.setUserId(id);
                                    session1.setMobile(mobile,email);
                                    session1.setUser_name(fullname);

                                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {

                                System.out.println("exe "+e.getMessage());

                                e.printStackTrace();

                            }

                        }

                    },

                    new Response.ErrorListener() {

                        @Override

                        public void onErrorResponse(VolleyError error) {

                            System.out.println("exe "+error.getMessage());

                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }) {

                @Override

                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email",fbemail);
                    params.put("name",fbname );
                    params.put("facebook_id",fbid );
                    params.put("phone_number","" );
                    params.put("fcm_id",FCM_ID);
                    System.out.println("dataaaaaaaaaa==========        "+params);







                    return params;

                }

            };



            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        }






    private void CallLoginApi(String FCM_ID) {
        progress_view.setVisibility(View.VISIBLE);
         final String userr = et_emailmobile.getText().toString();
         final String pswd = et_password.getText().toString();
                 if (userr.isEmpty()) {
                     et_emailmobile.setError("Usermobile or Email is required");
                     et_emailmobile.requestFocus();
                     return;
                 }
                 if (pswd.isEmpty()) {
                     et_password.setError("Password is required");
                     et_password.requestFocus();
                     return;
                 }
                 StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl + Login,
                         new Response.Listener<String>() {
                             @Override
                             public void onResponse(String response) {
                                 Log.e("login", "onResponse: "+response.toString() );
                                 rQueue.getCache().clear();
                                 try {
                                     JSONObject jsonObject= new JSONObject(response);
                                     String msg=jsonObject.getString("msg");
                                     System.out.println("<>login"+jsonObject);

                                     if (jsonObject.optString("result").equals("true")) {
                                         JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                         String id=jsonObject1.getString("id");
                                         String fullname=jsonObject1.getString("fullname");
                                         String email=jsonObject1.getString("email");
                                         String mobile=jsonObject1.getString("mobile");

                                         session1.setLogin(true);
                                         session1.setUserId(id);
                                         session1.setMobile(mobile,email);
                                         session1.setUser_name(fullname);


                                         Toast.makeText(LoginActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                                         Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                         startActivity(intent);
                                         finish();
                                         progress_view.setVisibility(View.INVISIBLE);

                                     } else {
                                         progress_view.setVisibility(View.INVISIBLE);
                                         Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

                                     }

                                 } catch (JSONException e) {
                                     progress_view.setVisibility(View.INVISIBLE);
                                     e.printStackTrace();
                                 }
                             }
                         },
                         new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError error) {
                                 progress_view.setVisibility(View.INVISIBLE);
                                 Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                             }
                         }) {
                     @Override
                     protected Map<String, String> getParams() {
                         Map<String, String> params = new HashMap<String, String>();
                         params.put("mobile_or_email", userr);
                         params.put("password", pswd);
                         params.put("fcm_id", FCM_ID);

                         System.out.print("allparams"+params);
                         return params;
                     }
                 };
                 rQueue = Volley.newRequestQueue(LoginActivity.this);
                 rQueue.add(stringRequest);
                 }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("<<><><====requestCode"+requestCode);

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        handleSignInResult(result);

        if (requestCode == RC_SIGN_IN) {

            System.out.println("<<><><====data"+data);


        }
    }


        private void handleSignInResult(GoogleSignInResult result) {
            System.out.println("<><><====googlesignin"+result);
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            idToken = account.getIdToken();
            name = account.getDisplayName();
            email = account.getEmail();
            googleid_id=account.getId();
            //  Log.e("email", "Login " + email);

            googleLogin(googleid_id,name,email);
             System.out.println("check user details " + idToken + "" + name + "" + email);
             System.out.println("<><><idToken"+idToken);
             System.out.println("<><><name"+name);
             System.out.println("<><><email"+email);
             System.out.println("<><><result"+googleid_id);

             AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
             firebaseAuthWithGoogle(credential);


        } else {

            System.out.println("<><><====googlesignin"+result);

           // Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
        }

    }

        private void googleLogin(String googleid_id, String name, String email) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://logicaltest.in/DogsTraining/Api/google_login",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject obj = new JSONObject(response);

                                System.out.println("checkl"+response);

                                System.out.println("checkl"+obj);

                                String result = obj.getString("result");

                                String msg = obj.getString("msg");

                                JSONObject obj1 = obj.getJSONObject("data");

                                if (result.equals("true")) {
                                    String id = obj1.getString("id");
                                    String fullname = obj1.getString("fullname");
                                    String email = obj1.getString("email");
                                    String mobile = obj1.getString("mobile");

                                    session1.setLogin(true);
                                    session1.setUserId(id);
                                    session1.setMobile(mobile,email);
                                    session1.setUser_name(fullname);

                                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LoginActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {

                                System.out.println("exe "+e.getMessage());
                                e.printStackTrace();

                            }

                        }

                    },

                    new Response.ErrorListener() {

                        @Override

                        public void onErrorResponse(VolleyError error) {

                            System.out.println("exe "+error.getMessage());

                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }) {

                @Override

                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email",email);
                    params.put("name",name );
                    params.put("google_id",googleid_id);
                    params.put("fcm_id",FCM_ID );
                    System.out.println("><>paramas"+params);
                    return params;

                }

            };



            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        }







    private void firebaseAuthWithGoogle(AuthCredential credential) {
            System.out.println("<><><====googlesignin"+credential);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "google login success",Toast.LENGTH_SHORT).show();

                            /*Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();*/

                        } else {

                            task.getException().printStackTrace();
                            System.out.println("<<><===="+task.getException().getMessage());
                            Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                        }
                        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authStateListener != null) {
            FirebaseAuth.getInstance().signOut();
        }
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }


       @Override
       public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("<><><>connectionResult"+connectionResult);
    }

/*
//google login
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 100) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{

            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            idToken = account.getIdToken();
            name = account.getDisplayName();
            email = account.getEmail();
            //  Log.e("email", "Login " + email);

            //googleLogin(idToken,name,email);
             System.out.println("check user details " + idToken + "" + name + "" + email);
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuthWithGoogle(credential);
        } else {

            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
        }

    }

    private void firebaseAuthWithGoogle(AuthCredential credential) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                            intent.putExtra("INTENT","1");
                            startActivity(intent);
                            finish();
                        } else {
                            task.getException().printStackTrace();
                            Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }); }
*/


}