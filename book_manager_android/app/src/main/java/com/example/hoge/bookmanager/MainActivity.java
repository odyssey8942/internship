package com.example.hoge.bookmanager;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import static com.example.hoge.bookmanager.FragmentBook.searchWord;

public class MainActivity extends AppCompatActivity {
    Globals globals;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_borrow:
                    Fragment fb = new FragmentBorrow();
                    getFragmentManager().beginTransaction().replace(R.id.content, fb).commit();
                    return true;
                case R.id.navigation_books:
                    searchWord = "";
                    Fragment fb2 = new FragmentBook();
                    getFragmentManager().beginTransaction().replace(R.id.content, fb2).commit();
                    return true;
                case R.id.navigation_employees:
                    Fragment fe = new FragmentEmployee();
                    getFragmentManager().beginTransaction().replace(R.id.content, fe).commit();
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globals = (Globals) this.getApplication();
        Globals.globalContext = getApplication();

        TextView loginInformation = (TextView)findViewById(R.id.login_information);
        loginInformation.setText("Account: " + globals.loginName);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Fragment fb = new FragmentBorrow();
        getFragmentManager().beginTransaction().replace(R.id.content, fb).commit();
    }

}
