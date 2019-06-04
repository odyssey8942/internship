//グローバル変数用クラス
package com.example.hoge.bookmanager;

import android.app.Application;
import android.content.Context;

public class Globals extends Application {
    public static Context globalContext;
    String loginName;
    boolean loginAdministrator;
}
