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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentEmployee extends Fragment {

    private ListView employeeList;
    private static final String[] sortItems = {"名前","管理者"};
    private static final String[] ascItems = {"昇順","降順"};
    private int sortNum = 0;
    private boolean ascBool = true;
    ArrayList<String> DBdata0 = new ArrayList<String>();
    ArrayList<String> DBdata1 = new ArrayList<String>();
    Globals globals = (Globals) Globals.globalContext;


    private void updateList() {
        MyOpenHelper helper = new MyOpenHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1);

        String orderBy;

        DBdata0.clear();
        DBdata1.clear();

        if(ascBool) {
            switch (sortNum) {
                case 0:
                    orderBy = "name asc";
                    break;
                case 1:
                    orderBy = "administrator asc";
                    break;
                default:
                    orderBy = null;
            }
        }
        else{
            switch (sortNum) {
                case 0:
                    orderBy = "name desc";
                    break;
                case 1:
                    orderBy = "administrator desc";
                    break;
                default:
                    orderBy = null;
            }
        }
        Cursor c = db.query("employees", new String[] { "name,administrator" }, null, null, null, null, orderBy);

        boolean hasNext = c.moveToFirst();
        while(hasNext) {
            String name = c.getString(0);
            String administrator = c.getString(1);
            DBdata0.add(name);
            DBdata1.add(administrator);
            //Collections.sort(DBdata);
            adapter.add("社員名 :　" + name + "\n管理者権限の有無 :　" + administrator);
            hasNext = c.moveToNext();
        }
        employeeList.setAdapter(adapter);
        c.close();
        db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_employee, container, false);
        Button addBookButton = (Button)v.findViewById(R.id.add_employee_button);
        Button deleteBookButton = (Button)v.findViewById(R.id.delete_employee_button);
        Spinner sortOrder;
        Spinner ascOrder;
        ListView employee_list = (ListView)v.findViewById(R.id.employee_list);

        // リストアイテムクリック時のイベントを追加
        if(globals.loginAdministrator) {
            employee_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent,
                                        View view, final int pos, long id) {
                /* Intent intent = new Intent(getActivity(), ReturnActivity.class);
                startActivity(intent); */

                    ListView listView = (ListView) parent;
                    String item = (String) listView.getItemAtPosition(pos);

                    // 返却確認ダイアログ表示
                    new AlertDialog.Builder(getActivity())
                            .setTitle("確認")
                            .setMessage("この社員を消去しますか")
                            .setPositiveButton("消去する", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    MyOpenHelper helper = new MyOpenHelper(getActivity());
                                    SQLiteDatabase db = helper.getReadableDatabase();
                                    db.delete("employees", "name = ? AND administrator = ? ", new String[]{DBdata0.get(pos), DBdata1.get(pos)});
                                    updateList();
                                    // OK button presse
                                }
                            })
                            .setNegativeButton("消去しない", null)
                            .show();


               /* // 選択アイテムを取得
                ListView listView = (ListView)parent;
                String item = (String)listView.getItemAtPosition(pos);

                // 通知ダイアログを表示
                Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();*/

                }
            });
        }

        sortOrder = (Spinner)v.findViewById(R.id.employee_sort_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sortItems);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortOrder.setAdapter(arrayAdapter);

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

        ascOrder = (Spinner)v.findViewById(R.id.employee_asc_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ascItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ascOrder.setAdapter(adapter);

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

        addBookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(globals.loginAdministrator) {
                    // 社員追加画面へ遷移
                    Intent intent = new Intent(getActivity(), AddEmployeeActivity.class);
                    startActivity(intent);
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
                    // 社員削除画面へ遷移
                    Intent intent_d = new Intent(getActivity(),DeleteEmployeeActivity.class);
                    startActivity(intent_d);
                }
                else {
                    Toast.makeText(getActivity(), "管理者のみの機能です。", Toast.LENGTH_SHORT).show();
                }
            }
        });

        employeeList = (ListView)v.findViewById(R.id.employee_list);

        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }
}