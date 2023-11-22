package com.example.localmusicplayer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login,btn_l_register;
    private EditText l_username,l_password;
    RegisterActivity.MySQLiteHelper mySQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mySQLiteHelper = new RegisterActivity.MySQLiteHelper(this);

        l_username = findViewById(R.id.l_username);
        l_password = findViewById(R.id.l_password);
        btn_login = findViewById(R.id.btn_login);
        btn_l_register = findViewById(R.id.btn_l_register);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btn_l_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); //第二个参数即为执行完跳转后的Activity
                startActivity(intent);
            }
        });
    }

    public void login() {
        String str_username = l_username.getText().toString();
        String str_password = l_password.getText().toString();
        SQLiteDatabase db;
        ContentValues contentValues;
        if(str_username.equals("")){
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
        }else if(str_password.equals("")){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        }else{
            db = mySQLiteHelper.getReadableDatabase(); //获取可读写SQLiteDatabase对象
            Cursor cursor1 = db.rawQuery("select * from userInfo where username =?",new String[]{str_username});//判断用户名是否存在
            if (cursor1.getCount() > 0) {//判断用户名是否存在

                //判断用户名与密码是否匹配
                Cursor cursor2 = db.query("userInfo",new String[]{"username","password"},"username=?",new String[]{str_username},null,null,"password");
                while (cursor2.moveToNext()) {
                    @SuppressLint("Range") String real_password = cursor2.getString(cursor2.getColumnIndex("password"));
                    cursor2.close();
                    if (str_password.equals(real_password)) {
                        btn_login.setEnabled(false);
                        btn_login.setTextColor(0xFFD0EFC6);
                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    } else {
                        Toast.makeText(this, "用户名或密码不正确", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

            } else {
                Toast.makeText(this, "用户名不存在", Toast.LENGTH_SHORT).show();
            }
            cursor1.close();

        }
    }
}