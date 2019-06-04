package com.example.hoge.bookmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.widget.CheckBox;

public class AddEmployeeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        MyOpenHelper helper = new MyOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();

        Button submitEmployeeButton = (Button)findViewById(R.id.submit_employee_button);
        final CheckBox administratorCheckBox = (CheckBox)findViewById(R.id.administrator_checkBox);
        administratorCheckBox.setChecked(false);
        final EditText employeeEditText = (EditText)findViewById(R.id.employee_edit);
        final EditText passwordEditText = (EditText)findViewById(R.id.password_edit);

        submitEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String employeeName = employeeEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (employeeName.equals("") && !password.equals("")) {
                    Toast.makeText(AddEmployeeActivity.this, "名前を入力してください。",
                            Toast.LENGTH_SHORT).show();
                }
                else if (!employeeName.equals("") && password.equals("")) {
                    Toast.makeText(AddEmployeeActivity.this, "パスワードを入力してください。",
                            Toast.LENGTH_SHORT).show();
                }
                else if (employeeName.equals("") && password.equals("")) {
                    Toast.makeText(AddEmployeeActivity.this, "名前,パスワードを入力してください。",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean checked = administratorCheckBox.isChecked();
                    ContentValues cv = new ContentValues();

                    if(checked){
                        cv.put("administrator", "あり");
                    }
                    else{
                        cv.put("administrator", "なし");
                    }
                    cv.put("name", employeeName);
                    cv.put("password", password);
                    db.insert("employees", null, cv);
                    finish();
                }
            }
        });
    }
}
