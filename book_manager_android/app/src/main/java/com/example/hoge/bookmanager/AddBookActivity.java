package com.example.hoge.bookmanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        MyOpenHelper helper = new MyOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();

        Button submitBookButton = (Button)findViewById(R.id.submit_book_button);
        final EditText publishEditText = (EditText)findViewById(R.id.publisher_edit);
        final EditText bookEditText = (EditText)findViewById(R.id.book_edit);
        final EditText authorEditText = (EditText)findViewById(R.id.author_edit);

        submitBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String publishName = publishEditText.getText().toString();
                String authorName = authorEditText.getText().toString();
                String bookTitle = bookEditText.getText().toString();
                if (publishName.equals("")||bookTitle.equals("")||authorName.equals("")){
                    if ( publishName.equals("") && !bookTitle.equals("") && !authorName.equals("")){
                        Toast.makeText(AddBookActivity.this, "出版社名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else if( !publishName.equals("") && bookTitle.equals("") && !authorName.equals("")){
                        Toast.makeText(AddBookActivity.this, "書名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else if( !publishName.equals("") && !bookTitle.equals("") && authorName.equals("")){
                        Toast.makeText(AddBookActivity.this, "著者名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else if( publishName.equals("") && bookTitle.equals("") && !authorName.equals("")){
                        Toast.makeText(AddBookActivity.this, "書名,出版社名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else if( publishName.equals("") && !bookTitle.equals("") && authorName.equals("")){
                        Toast.makeText(AddBookActivity.this, "著者名,出版社名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else if( !publishName.equals("") && bookTitle.equals("") && authorName.equals("")){
                        Toast.makeText(AddBookActivity.this, "書名,著者名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AddBookActivity.this, "書名,著者名,出版社名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("title", bookTitle);
                    cv.put("author", authorName);
                    cv.put("publish", publishName);

                    db.insert("books", null, cv);
                    finish();
                }
            }
        });
    }

}
