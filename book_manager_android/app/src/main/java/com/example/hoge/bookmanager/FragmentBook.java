package com.example.hoge.bookmanager;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentBook extends Fragment {

    private ListView bookList;
    private static final String[] sortItems = {"書名","著者名","出版社名"};
    private static final String[] ascItems = {"昇順","降順"};
    private static final String[] searchItems = {"全て","書名","著者名","出版社名"};
    private int sortNum = 0;
    private int searchNum = 0;
    private boolean ascBool = true;
    static boolean search_f = false;
    static String searchWord = "";

    Globals globals = (Globals) Globals.globalContext;

    ArrayList<String> DBdata0 = new ArrayList<String>();
    ArrayList<String> DBdata1 = new ArrayList<String>();
    ArrayList<String> DBdata2 = new ArrayList<String>();

    private void updateList() {
        MyOpenHelper helper = new MyOpenHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1);
        Cursor c;
        int i;
        String orderBy;

        DBdata0.clear();
        DBdata1.clear();
        DBdata2.clear();

        //ソート処理
        if(ascBool) {
            switch (sortNum) {
                case 0:
                    orderBy = "title asc";
                    break;
                case 1:
                    orderBy = "author asc";
                    break;
                case 2:
                    orderBy = "publish asc";
                    break;
                default:
                    orderBy = null;
            }
        }
        else{
            switch (sortNum) {
                case 0:
                    orderBy = "title desc";
                    break;
                case 1:
                    orderBy = "author desc";
                    break;
                case 2:
                    orderBy = "publish desc";
                    break;
                default:
                    orderBy = null;
            }
        }

        if(!search_f){  //検索が行われていないとき
            searchWord = "";
            c = db.query("books", new String[] { "title,author,publish"},null, null, null, null, orderBy);
        }
        else {
            switch (searchNum) {
                case 0:
                    c = db.query("books", new String[]{"title,author,publish"}, "title like '%' || ? || '%' OR author like '%' || ? || '%' OR publish like '%' || ? || '%'", new String[]{searchWord, searchWord, searchWord}, null, null, orderBy);
                    break;
                case 1:
                    c = db.query("books", new String[]{"title,author,publish"}, "title like '%' || ? || '%' ", new String[]{searchWord}, null, null, orderBy);
                    break;
                case 2:
                    c = db.query("books", new String[]{"title,author,publish"}, "author like '%' || ? || '%'", new String[]{searchWord}, null, null, orderBy);
                    break;
                case 3:
                    c = db.query("books", new String[]{"title,author,publish"}, "publish like '%' || ? || '%'", new String[]{searchWord}, null, null, orderBy);
                    break;
                default:
                    c = db.query("books", new String[]{"title,author,publish"}, "title like '%' || ? || '%' OR author like '%' || ? || '%' OR publish like '%' || ? || '%'", new String[]{searchWord, searchWord, searchWord}, null, null, orderBy);
            }
        }

        //リストの表示
        boolean hasNext = c.moveToFirst();
            while (hasNext) {
                String title = c.getString(0);
                String author = c.getString(1);
                String publish = c.getString(2);

                DBdata0.add(title);
                DBdata1.add(author);
                DBdata2.add(publish);

                adapter.add("書名 :　" + title + "\n著者 :　" + author + "\n出版社 :　" + publish);
                hasNext = c.moveToNext();
            }
            bookList.setAdapter(adapter);
            c.close();
            db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book, container, false);
        Button addBookButton = (Button)v.findViewById(R.id.add_book_button);
        Button deleteBookButton = (Button) v.findViewById(R.id.delete_book_button);
        Button searchBookButton = (Button)v.findViewById(R.id.search_book_button);
        Button searchClearButton = (Button) v.findViewById(R.id.search_clear_button);
        Spinner sortOrder = (Spinner)v.findViewById(R.id.book_sort_spinner);
        Spinner ascOrder = (Spinner)v.findViewById(R.id.book_asc_spinner);
        Spinner searchOrder = (Spinner)v.findViewById(R.id.book_search_spinner);
        final EditText searchEditText = (EditText)v.findViewById(R.id.search_book_edit);


        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sortItems);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> ascAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ascItems);
        ascAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, searchItems);
        searchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ascOrder.setAdapter(ascAdapter);
        sortOrder.setAdapter(sortAdapter);
        searchOrder.setAdapter(searchAdapter);

        ListView book_list = (ListView)v.findViewById(R.id.book_list);

        // リストアイテムクリックでの本の消去
        if(globals.loginAdministrator) {
            book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent,
                                        View view, final int pos, final long id) {

                    // 返却確認ダイアログ表示
                    new AlertDialog.Builder(getActivity())
                            .setTitle("確認")
                            .setMessage("この本を消去しますか")
                            .setPositiveButton("消去する", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MyOpenHelper helper = new MyOpenHelper(getActivity());
                                    SQLiteDatabase db = helper.getReadableDatabase();
                                    db.delete("books", "title = ? AND author = ? AND publish = ?", new String[]{DBdata0.get(pos), DBdata1.get(pos), DBdata2.get(pos)});
                                    updateList();
                                    // OK button presse
                                }
                            })
                            .setNegativeButton("消去しない", null)
                            .show();
                }
            });
        }
        //検索ボタンクリック時の処理
        searchBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWord = searchEditText.getText().toString();
                if(searchWord.equals("")) {search_f = false;}
                else {search_f = true;}
                updateList();
            }
        });

        //✕ボタンクリック時の処理（検索のリセット）
        searchClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_f = false;
                searchEditText.getEditableText().clear();
                searchWord="";
                updateList();
            }
        });

        //ソート用スピナーの処理
        sortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner) adapterView;
                String item = (String) spinner.getSelectedItem();
                sortNum = i;
                updateList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ascOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner) adapterView;
                String item = (String) spinner.getSelectedItem();
                if(i == 0){ ascBool = true; }
                else{ ascBool = false; }
                updateList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //検索条件用スピナーの処理
        searchOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner) adapterView;
                String item = (String) spinner.getSelectedItem();
                searchNum = i;
                updateList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //本の追加、消去
        addBookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(globals.loginAdministrator) {
                    // 本削除画面へ遷移
                    Intent intent_a = new Intent(getActivity(),AddBookActivity.class);
                    search_f = false;
                    startActivity(intent_a);
                }
                else {
                    Toast.makeText(getActivity(), "管理者のみの機能です。", Toast.LENGTH_SHORT).show();
                }

            }
        });
        deleteBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globals.loginAdministrator) {
                    // 本削除画面へ遷移
                    search_f = false;
                    Intent intent_d = new Intent(getActivity(),DeleteBookActivity.class);
                    startActivity(intent_d);
                }
                else {
                    Toast.makeText(getActivity(), "管理者のみの機能です。", Toast.LENGTH_SHORT).show();
                }

            }

        });


        bookList = (ListView)v.findViewById(R.id.book_list);
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateList();
        System.out.println("onResume");
    }
}