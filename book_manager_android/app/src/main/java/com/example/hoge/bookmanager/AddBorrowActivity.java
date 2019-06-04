package com.example.hoge.bookmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddBorrowActivity extends AppCompatActivity {
    private Spinner bookList,employeeList;
    private String bookValue,employeeValue;
    private EditText titleText,authorText,publishText;
    private Button searchButton;
    private boolean sState = false;
    private String[] sWord = new String[3];
    private  int listSize;

    private void updateBookList() {

        MyOpenHelper helper = new MyOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        List<String> borrowedTitles = new ArrayList<String>();
        //貸し出し中の書籍名の検索
        Cursor pv = db.query("borrows",new String[]{"title"},"return_date IS NULL",null,null,null,null);
        boolean hasNextP = pv.moveToFirst();
        while(hasNextP){
            borrowedTitles.add(pv.getString(0));
            hasNextP =pv.moveToNext();
        }
        //title NOT IN(title1,title2,...)のWHERE文を作成
        String texts = new String();
        texts = "title NOT IN(";
        for(int i=0;i<borrowedTitles.size();i++){
            if(i!=0){texts = texts + ",";}
            texts = texts +"\""+ borrowedTitles.get(i)+ "\" ";
        }
        texts = texts + ")";
        System.out.println(texts);
        //貸出可能な書籍を検索
        Cursor c = db.query("books", new String[] { "title,publish,author"}, texts, null, null, null, "title asc");
        boolean hasNextC = c.moveToFirst();
        while(hasNextC) {
            String title = c.getString(0);
            String publish = c.getString(1);
            String author = c.getString(2);
            if(sState) {
                if(title.equals(sWord[0]) || author.equals(sWord[1]) || publish.equals(sWord[2])) {
                    adapter.add(title + "\n著者 : " + author + "\n出版社 : " + publish);
                }
            }
            else
                adapter.add(title + "\n著者 : " + author + "\n出版社 : " + publish);
            hasNextC = c.moveToNext();
        }
        bookList.setAdapter(adapter);
        listSize = adapter.getCount();
        c.close();
        db.close();
        sState = false;
    }
    private void updateEmployeeList() {
        MyOpenHelper helper = new MyOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        Cursor c = db.query("employees", new String[] { "name" }, null, null, null, null, "name asc");
        boolean hasNext = c.moveToFirst();
        while(hasNext) {
            String name = c.getString(0);
            adapter.add(name);
            hasNext = c.moveToNext();
        }
        employeeList.setAdapter(adapter);
        c.close();
        db.close();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyOpenHelper helper = new MyOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_borrow);
        bookList = (Spinner) findViewById(R.id.books_spinner);
        employeeList = (Spinner) findViewById(R.id.borrower_spinner);

        titleText = (EditText) findViewById(R.id.title_editText);
        authorText = (EditText) findViewById(R.id.author_editText);
        publishText = (EditText) findViewById(R.id.publish_editText);

        searchButton = (Button) findViewById(R.id.search_button);

        bookList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner)adapterView;
                String item = (String)spinner.getSelectedItem();
                bookValue = item.substring(0,item.indexOf("\n"));
                //Toast.makeText(getApplicationContext(),bookValue,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        employeeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner)adapterView;
                employeeValue = (String)spinner.getSelectedItem();
                //Toast.makeText(getApplicationContext(),employeeValue,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button submitBorrowButton = (Button) findViewById(R.id.submit_borrow_button);
        submitBorrowButton.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
                if(listSize > 0){
                    if (!bookValue.equals("") && !employeeValue.equals("")) {
                        Calendar cal = Calendar.getInstance();
                        String publishValue = "\0";
                        String authorValue = "\0";
                        String dateValue = "\0";

                        //出版社名の取得
                        Cursor c = db.query("books", new String[]{"publish,author"}, "title = ?", new String[]{bookValue}, null, null, null);
                        c.moveToFirst();
                        publishValue = c.getString(0);
                        authorValue = c.getString(1);

                        //日付の取得
                        dateValue = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                        ContentValues cv = new ContentValues();
                        cv.put("publish", publishValue);
                        cv.put("title", bookValue);
                        cv.put("author", authorValue);
                        cv.put("borrower", employeeValue);
                        cv.put("rental_date", dateValue);

                        db.insert("borrows", null, cv);


                        c.close();
                        db.close();

                        //Toast.makeText(getApplicationContext(),"あ",Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        if (bookValue.equals("")) {
                            Toast.makeText(getApplicationContext(), "書籍情報がありません", Toast.LENGTH_LONG).show();
                        }
                        if (bookValue.equals("")) {
                            Toast.makeText(getApplicationContext(), "書籍情報がありません", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "貸出可能な書籍が存在しません", Toast.LENGTH_SHORT).show();
                }
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sState = true;
                sWord[0] = titleText.getText().toString();
                sWord[1] = authorText.getText().toString();
                sWord[2] = publishText.getText().toString();

                updateBookList();
            }
        });


    }



    @Override
    public void onResume() {
        super.onResume();
        updateEmployeeList();
        updateBookList();
    }

}

