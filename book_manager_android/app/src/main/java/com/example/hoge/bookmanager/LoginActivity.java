package com.example.hoge.bookmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;


public class LoginActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText passwordEditText;
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button)findViewById(R.id.log_in_button);
        nameEditText = (EditText)findViewById(R.id.name_edit_text);
        passwordEditText = (EditText)findViewById(R.id.password_edit_text);
        globals = (Globals) this.getApplication();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEditText.setError(null);
                passwordEditText.setError(null);
                String editedName = nameEditText.getText().toString();
                String editedPassword = passwordEditText.getText().toString();
                MyOpenHelper helper = new MyOpenHelper(LoginActivity.this);
                SQLiteDatabase db = helper.getReadableDatabase();

                if (editedName.equals("")) {
                    nameEditText.setError("名前を入力してください。");
                }
                if (editedPassword.equals("")) {
                    passwordEditText.setError("パスワードを入力してください。");
                }
                if(!editedName.equals("") && !editedPassword.equals("")){
                    Cursor c = db.query("employees", new String[] {"password, administrator"}, "name = ?", new String[] {editedName}, null, null, null);
                    boolean hasNext = c.moveToFirst();
                    if(!hasNext){
                        passwordEditText.getEditableText().clear();
                        passwordEditText.setError("パスワードが間違っています。");
                        //管理者ID:master,PASS:master
                    }
                    while(hasNext) {
                        String password = c.getString(0);
                        String administrator = c.getString(1);
                        if(password.equals(editedPassword)){
                            c.close();
                            db.close();
                            globals.loginName = editedName;
                            globals.loginAdministrator = administrator.equals("あり");
                            nameEditText.getEditableText().clear();
                            passwordEditText.getEditableText().clear();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            //finish();
                        }
                        hasNext = c.moveToNext();
                    }
                }
            }
        });
    }
}