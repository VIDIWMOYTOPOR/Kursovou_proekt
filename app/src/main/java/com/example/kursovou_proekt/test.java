package com.example.kursovou_proekt;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.kursovou_proekt.Retrofit.INodeJS;
import com.example.kursovou_proekt.Retrofit.RetrofitClient;
import com.example.kursovou_proekt.database.DatabaseHelper;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class test extends AppCompatActivity {


//    public void openMainActivity(View view){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edt_email,edt_password;
    Button btn_register,btn_login;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);

        SQLiteDatabase db = DatabaseHelper.getDatabase(getApplicationContext());



        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        //view
        btn_login = findViewById(R.id.login_button);
        btn_register = findViewById(R.id.register_button);

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        //Event
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(edt_email.getText().toString(), edt_password.getText().toString());
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(edt_email.getText().toString(),edt_password.getText().toString());
            }
        });
    }

    private void registerUser(String email, String password) {
        View enter_name_view = LayoutInflater.from(this).inflate(R.layout.enter_name_layout,null);
        new MaterialStyledDialog.Builder(this)
                .setTitle("Register")
                .setDescription("One more step!")
                .setCustomView(enter_name_view)
                .setIcon(R.drawable.ic_user)
                .setNegativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("Register")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText edt_name = (EditText)enter_name_view.findViewById(R.id.edt_name);

                        compositeDisposable.add(myAPI.registerUser(email,edt_name.getText().toString(),password)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        Toast.makeText(test.this,""+s,Toast.LENGTH_LONG).show();
                                    }
                                }));
                    }
                }).show();
    }

    private void loginUser(String email, String password) {
        compositeDisposable.add(myAPI.loginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s.contains("encrypted_password")) {
                            Toast.makeText(test.this, "Login Success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(test.this, MainActivity.class);
                            intent.putExtra("email", email); // Добавляем email в Intent
                            startActivity(intent);

                        }
                        else
                            Toast.makeText(test.this,""+s,Toast.LENGTH_LONG).show();
                    }
                })
        );

    }

}
