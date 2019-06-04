package com.example.hoge.bookmanager;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.R.attr.title;
import static android.R.id.list;

public class FragmentBorrow extends Fragment {
    private ListView borrowList;
    private String orderBy = "rental_date desc";
    ArrayList<String> DBdata0 = new ArrayList<String>();
    ArrayList<String> DBdata1 = new ArrayList<String>();
    ArrayList<String> DBdata2 = new ArrayList<String>();
    ArrayList<String> DBdata3 = new ArrayList<String>();
    ArrayList<String> DBdata4 = new ArrayList<String>();
    ArrayList<String> DBdata5 = new ArrayList<String>();
    ArrayList<String> DBdata6 = new ArrayList<String>();
    ArrayList<String> SDBdata0 = new ArrayList<String>();
    ArrayList<String> SDBdata1 = new ArrayList<String>();
    ArrayList<String> SDBdata2 = new ArrayList<String>();
    ArrayList<String> SDBdata3 = new ArrayList<String>();

    ArrayList<Integer> year = new ArrayList<Integer>();
    ArrayList<Integer> month = new ArrayList<Integer>();
    ArrayList<Integer> day = new ArrayList<Integer>();

    ArrayList<String> nFB = new ArrayList<String>();
    //ArrayList<ArrayList<String>> DBdata = new ArrayList<ArrayList<String>>();

    Globals globals = (Globals) Globals.globalContext;

    static boolean listState = true;
    static boolean notification = false;
    static int select = 0;
    static String selectString;

    Calendar cal = Calendar.getInstance();
    int[] nowdate = {0,0,0};

    private void updateList() {
        MyOpenHelper helper = new MyOpenHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1);


        DBdata0.clear();
        DBdata1.clear();
        DBdata2.clear();
        DBdata3.clear();
        DBdata4.clear();
        DBdata5.clear();
        DBdata6.clear();

        SDBdata0.clear();
        SDBdata1.clear();
        SDBdata2.clear();
        SDBdata3.clear();


        int i = 0;
        Cursor c = db.query("borrows", new String[]{"title,publish,author,borrower,rental_date,return_date"}, null, null, null, null, orderBy);
        boolean hasNext = c.moveToFirst();
        while (hasNext) {
            String title = c.getString(0);
            String publish = c.getString(1);
            String author = c.getString(2);
            String borrower = c.getString(3);
            String rental_date = c.getString(4);
            String return_date = c.getString(5);
            String return_message;
            int[] num = {0,0,0};
            if (return_date == null) {
                return_message = "未返却";
            } else {
                return_message = "返却済　返却日: " + return_date;
            }

            DBdata0.add(title);
            DBdata1.add(publish);
            DBdata2.add(borrower);
            DBdata3.add(rental_date);
            DBdata4.add(return_date);
            DBdata5.add(return_message);
            DBdata6.add(author);

            String[] date = rental_date.split("-",0);
            for(int j = 0;j<3;j++){
                num[j] = Integer.parseInt(date[j]);
                switch (j){
                    case 0:
                        year.add(num[j]);
                        break;
                    case 1:
                        month.add(++num[j]);
                        break;
                    case 2:
                        day.add(num[j]);
                        break;
                }
            }

            if(listState) {
                switch (select) {
                    case 0:
                        adapter.add("貸出日: " + rental_date + "　借出人: " + borrower + "\nタイトル: " + title + "  著者: "+ author +"  出版社: " + publish + "\n状態: " + return_message);
                        break;
                    case 1:
                        if(title.equals(selectString)){
                            adapter.add("貸出日: " + rental_date + "　借出人: " + borrower + "\nタイトル: " + title + "  著者: "+ author +"  出版社: " + publish + "\n状態: " + return_message);
                            SDBdata0.add(title);
                            SDBdata1.add(publish);
                            SDBdata2.add(borrower);
                            SDBdata3.add(author);
                        }
                        break;
                    case 2:
                        if(author.equals(selectString)){
                            adapter.add("貸出日: " + rental_date + "　借出人: " + borrower + "\nタイトル: " + title + "  著者: "+ author +"  出版社: " + publish + "\n状態: " + return_message);
                            SDBdata0.add(title);
                            SDBdata1.add(publish);
                            SDBdata2.add(borrower);
                            SDBdata3.add(author);
                        }
                        break;
                    case 3:
                        if(publish.equals(selectString)){
                            adapter.add("貸出日: " + rental_date + "　借出人: " + borrower + "\nタイトル: " + title + "  著者: "+ author +"  出版社: " + publish + "\n状態: " + return_message);
                            SDBdata0.add(title);
                            SDBdata1.add(publish);
                            SDBdata2.add(borrower);
                            SDBdata3.add(author);
                        }
                        break;
                    case 4:
                        if(borrower.equals(selectString)){
                            adapter.add("貸出日: " + rental_date + "　借出人: " + borrower + "\nタイトル: " + title + "  著者: "+ author +"  出版社: " + publish + "\n状態: " + return_message);
                            SDBdata0.add(title);
                            SDBdata1.add(publish);
                            SDBdata2.add(borrower);
                            SDBdata3.add(author);
                        }
                        break;
                    default:
                        break;
                }
            }
            else{
                switch (select) {
                    case 0:
                        if (return_date == null) {
                            adapter.add("貸出日: " + rental_date + "　借出人: " + borrower + "\nタイトル: " + title + "  著者: "+ author +"  出版社: " + publish + "\n状態: " + return_message);
                            SDBdata0.add(title);
                            SDBdata1.add(publish);
                            SDBdata2.add(borrower);
                            SDBdata3.add(author);
                        }
                        break;
                    case 1:
                        if(title.equals(selectString) && return_date == null){
                            adapter.add("貸出日: " + rental_date + "　借出人: " + borrower + "\nタイトル: " + title + "  著者: "+ author +"  出版社: " + publish + "\n状態: " + return_message);
                            SDBdata0.add(title);
                            SDBdata1.add(publish);
                            SDBdata2.add(borrower);
                            SDBdata3.add(author);
                        }
                        break;
                    case 2:
                        if(author.equals(selectString) && return_date == null){
                            adapter.add("貸出日: " + rental_date + "　借出人: " + borrower + "\nタイトル: " + title + "  著者: "+ author +"  出版社: " + publish + "\n状態: " + return_message);
                            SDBdata0.add(title);
                            SDBdata1.add(publish);
                            SDBdata2.add(borrower);
                            SDBdata3.add(author);
                        }
                        break;
                    case 3:
                        if(publish.equals(selectString) && return_date == null){
                            adapter.add("貸出日: " + rental_date + "　借出人: " + borrower + "\nタイトル: " + title + "  著者: "+ author +"  出版社: " + publish + "\n状態: " + return_message);
                            SDBdata0.add(title);
                            SDBdata1.add(publish);
                            SDBdata2.add(borrower);
                            SDBdata3.add(author);
                        }
                        break;
                    case 4:
                        if(borrower.equals(selectString) && return_date == null){
                            adapter.add("貸出日: " + rental_date + "　借出人: " + borrower + "\nタイトル: " + title + "  著者: "+ author +"  出版社: " + publish + "\n状態: " + return_message);
                            SDBdata0.add(title);
                            SDBdata1.add(publish);
                            SDBdata2.add(borrower);
                            SDBdata3.add(author);
                        }
                        break;
                    default:
                        break;
                }
            }
            hasNext = c.moveToNext();
        }
        for(int j = 0;j<year.size();j++){
            if(year.get(j) == cal.get(Calendar.YEAR) && month.get(j) == cal.get(Calendar.MONTH) && day.get(j) == cal.get(Calendar.DAY_OF_MONTH) && DBdata4.get(j) == null){
                nFB.add(DBdata2.get(j));
            }
        }
        Set<String> set_nFB = new HashSet<>(nFB);
        List<String> List_nFB = new ArrayList<>(set_nFB);

        borrowList.setAdapter(adapter);

        if(List_nFB.size() > 0 && !notification){
            notification = true;
            new AlertDialog.Builder(getActivity())
                    .setTitle("警告!!")
                    .setMessage("返却期限を過ぎた人が"+ List_nFB.size()+"人います")
                    .setPositiveButton("OK", null)
                    .show();
        }
        c.close();
        db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_borrow, container, false);
        Button addBorrowButton = (Button)v.findViewById(R.id.add_borrow_button);
        Button addSelectButton = (Button)v.findViewById(R.id.add_select_button);
        Button addDeleteButton = (Button)v.findViewById(R.id.add_delete_button);
        Button addcDeleteButton = (Button)v.findViewById(R.id.add_cDelete_button);
        ListView borrow_list = (ListView)v.findViewById(R.id.borrow_list);

        // リストアイテムクリック時のイベントを追加
        if(globals.loginAdministrator) {
            borrow_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(final AdapterView<?> parent,
                                        View view, final int pos, long id) {
                /* Intent intent = new Intent(getActivity(), ReturnActivity.class);
                startActivity(intent); */
                    // 選択アイテムを取得

                    MyOpenHelper helper = new MyOpenHelper(getActivity());
                    SQLiteDatabase DB = helper.getReadableDatabase();

                    ListView listView = (ListView) parent;
                    String item = (String) listView.getItemAtPosition(pos);

                    // 返却確認ダイアログ表示

                    new AlertDialog.Builder(getActivity())
                            .setTitle("確認")
                            .setMessage("この本を返却しますか")
                            .setPositiveButton("返却する", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // OK button pressed
                                    MyOpenHelper helper = new MyOpenHelper(getActivity());
                                    SQLiteDatabase db = helper.getReadableDatabase();

                                    //Calendar cal = Calendar.getInstance();
                                    String dateValue = "\0";
                                    dateValue = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                                    ContentValues values = new ContentValues();
                                    values.put("return_date", dateValue);
                                    if (listState) {
                                        if (select == 0) {
                                            db.update("borrows", values, "title = ? AND publish = ? AND borrower = ?", new String[]{DBdata0.get(pos), DBdata1.get(pos), DBdata2.get(pos)});
                                            updateList();
                                        } else {
                                            db.update("borrows", values, "title = ? AND publish = ? AND borrower = ?", new String[]{SDBdata0.get(pos), SDBdata1.get(pos), SDBdata2.get(pos)});
                                            updateList();
                                        }
                                    } else {
                                        db.update("borrows", values, "title = ? AND publish = ? AND borrower = ?", new String[]{SDBdata0.get(pos), SDBdata1.get(pos), SDBdata2.get(pos)});
                                        updateList();
                                    }
                                    //db.delete("borrows","title = ? AND publish = ? AND borrower = ?",new String[]{dbdata[0][pos],dbdata[1][pos],dbdata[2][pos]});
                                }
                            })
                            .setNegativeButton("返却しない", null)
                            .show();


                    //通知ダイアログを表示
                    //Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();

                }
            });
        }
        addBorrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globals.loginAdministrator) {
                    // 本貸出画面へ遷移
                    Intent intent1 = new Intent(getActivity(), AddBorrowActivity.class);
                    startActivity(intent1);
                }
                else {
                    Toast.makeText(getActivity(), "管理者のみの機能です。", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 0;
                final String[] items = {"タイトル", "著者", "出版社", "借出人"};
                new AlertDialog.Builder(getActivity())
                        .setTitle("絞り込み(枠外選択でリセット)")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // item_which pressed
                                switch (which){
                                    case 0:
                                        select = 1;
                                        Set<String> set1 = new HashSet<>(DBdata0);
                                        List<String> DDBdata0 = new ArrayList<>(set1);

                                        final String[] dbdata0 = new String[DDBdata0.size()];
                                        for(int i = 0;i<DDBdata0.size();i++){
                                            dbdata0[i] = DDBdata0.get(i);
                                        }
                                        final String[] sT = dbdata0;
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("タイトル")
                                                .setItems(sT, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // item_which pressed
                                                        selectString = dbdata0[which];
                                                        updateList();
                                                    }
                                                })
                                                .show();
                                        break;
                                    case 1:
                                        select = 2;
                                        Set<String> set2 = new HashSet<>(DBdata6);
                                        List<String> DDBdata1 = new ArrayList<>(set2);

                                        final String[] dbdata1 = new String[DDBdata1.size()];
                                        for(int i = 0;i<DDBdata1.size();i++){
                                            dbdata1[i] = DDBdata1.get(i);
                                        }
                                        final String[] sA = dbdata1;
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("著者")
                                                .setItems(sA, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // item_which pressed
                                                        selectString = dbdata1[which];
                                                        updateList();
                                                    }
                                                })
                                                .show();
                                        break;
                                    case 2:
                                        select = 3;
                                        Set<String> set3 = new HashSet<>(DBdata1);
                                        List<String> DDBdata2 = new ArrayList<>(set3);

                                        final String[] dbdata2 = new String[DDBdata2.size()];
                                        for(int i = 0;i<DDBdata2.size();i++){
                                            dbdata2[i] = DDBdata2.get(i);
                                        }
                                        final String[] sP = dbdata2;
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("出版社")
                                                .setItems(sP, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // item_which pressed
                                                        selectString = dbdata2[which];
                                                        updateList();
                                                    }
                                                })
                                                .show();
                                        break;
                                    case 3:
                                        select = 4;
                                        Set<String> set4 = new HashSet<>(DBdata2);
                                        List<String> DDBdata3 = new ArrayList<>(set4);

                                        final String[] dbdata3 = new String[DDBdata3.size()];
                                        for(int i = 0;i<DDBdata3.size();i++){
                                            dbdata3[i] = DDBdata3.get(i);
                                        }
                                        final String[] sB = dbdata3;
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("借出人")
                                                .setItems(sB, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // item_which pressed
                                                        selectString = dbdata3[which];
                                                        updateList();
                                                    }
                                                })
                                                .show();
                                        break;
                                    default:
                                        select = 0;
                                        break;
                                }
                            }
                        })
                        .show();
                updateList();
            }
        });
        addDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 本貸出画面へ遷移
                listState = !listState;
                updateList();
            }
        });
        addcDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 本貸出画面へ遷移
                if(globals.loginAdministrator) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("警告")
                            .setMessage("返却済の履歴が完全消去されます。")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // OK button pressed
                                    MyOpenHelper helper = new MyOpenHelper(getActivity());
                                    SQLiteDatabase db = helper.getReadableDatabase();

                                    db.delete("borrows", "return_date IS NOT NULL", null);
                                    updateList();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
                else{
                    Toast.makeText(getActivity(), "管理者のみの機能です。", Toast.LENGTH_SHORT).show();
                }
            }
        });
        borrowList = (ListView)v.findViewById(R.id.borrow_list);
        updateList();
        return v;
        //return inflater.inflate(R.layout.fragment_borrow, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();
        updateList();
        System.out.println("onResume");
    }
}