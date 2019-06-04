package com.example.hoge.bookmanager;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class DeleteBookActivity extends AppCompatActivity {

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_book);
        Button bookDeleteButton = (Button)findViewById(R.id.book_delete_button);
        final EditText publishEditText = (EditText)findViewById(R.id.publisher_edit);
        final EditText authorEditText = (EditText)findViewById(R.id.author_edit);
        final EditText bookEditText = (EditText)findViewById(R.id.book_edit);

        MyOpenHelper helper = new MyOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();

        bookDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String publishName = publishEditText.getText().toString();
                String authorName = authorEditText.getText().toString();
                String bookTitle = bookEditText.getText().toString();
                if (publishName.equals("")||bookTitle.equals("")||authorName.equals("")){
                    if ( publishName.equals("") && !bookTitle.equals("") && !authorName.equals("")){
                        Toast.makeText(DeleteBookActivity.this, "出版社名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else if( !publishName.equals("") && bookTitle.equals("") && !authorName.equals("")){
                        Toast.makeText(DeleteBookActivity.this, "書名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else if( !publishName.equals("") && !bookTitle.equals("") && authorName.equals("")){
                        Toast.makeText(DeleteBookActivity.this, "著者名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else if( publishName.equals("") && bookTitle.equals("") && !authorName.equals("")){
                        Toast.makeText(DeleteBookActivity.this, "書名,出版社名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else if( publishName.equals("") && !bookTitle.equals("") && authorName.equals("")){
                        Toast.makeText(DeleteBookActivity.this, "著者名,出版社名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else if( !publishName.equals("") && bookTitle.equals("") && authorName.equals("")){
                        Toast.makeText(DeleteBookActivity.this, "書名,著者名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(DeleteBookActivity.this, "書名,著者名,出版社名を入力してください。",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    db.delete("books","title=? AND author = ? AND publish=?",new String[]{bookTitle,authorName,publishName});
                    Toast.makeText(DeleteBookActivity.this, "削除完了",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

}
