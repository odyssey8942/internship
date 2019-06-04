package com.example.hoge.bookmanager;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteEmployeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_employee);

        MyOpenHelper helper = new MyOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();

        Button employeeDeleteButton = (Button)findViewById(R.id.employee_delete_button);
        final CheckBox administratorCheckBox = (CheckBox)findViewById(R.id.administrator_checkBox);
        administratorCheckBox.setChecked(false);
        final EditText employeeEditText = (EditText)findViewById(R.id.employee_edit);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        employeeDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String employeeName = employeeEditText.getText().toString();
                String administrator;
                if (employeeName.equals("")) {
                    Toast.makeText(DeleteEmployeeActivity.this, "名前を入力してください。",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean checked = administratorCheckBox.isChecked();

                    if(checked){
                        administrator = "あり";
                    }
                    else{
                        administrator = "なし";
                    }
                    db.delete("employees", "name = ? AND administrator = ?",new String[]{employeeName,administrator} );
                    finish();
                }
            }
        });
    }
}
