package com.example.localmusicplayer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private Button btn_register;
    private EditText r_username,r_password;
    MySQLiteHelper mySQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mySQLiteHelper = new MySQLiteHelper(this);
        r_username = findViewById(R.id.r_username);
        r_password = findViewById(R.id.r_password);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

            }
        });
    }
    public void register() {
        String str_username = r_username.getText().toString();
        String str_password = r_password.getText().toString();
        SQLiteDatabase db;
        ContentValues contentValues;
        //UserBean userBean = new UserBean(str_username,str_password);
        if(str_username.equals("")){
            Toast.makeText(this, "账号不能为空！", Toast.LENGTH_SHORT).show();
        } else if (str_password.equals("")) {
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            db = mySQLiteHelper.getWritableDatabase();//获取可读写SQLiteDatabase对象
            contentValues = new ContentValues();//创建对象
            contentValues.put("username", str_username);//将数据添加到contentValues对象
            contentValues.put("password", str_password);
            db.insert("userInfo", null, contentValues);
            btn_register.setEnabled(false);
            btn_register.setTextColor(0xFFD0EFC6);
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            db.close();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);	//第二个参数即为执行完跳转后的Activity
            startActivity(intent);
        }
    }


    static class MySQLiteHelper extends SQLiteOpenHelper {
        public MySQLiteHelper(Context context) {
            super(context,"LocalMusicPlayer.db",null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table userInfo(_id integer primary key autoincrement,username varchar(20),password varchar(20))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }



}