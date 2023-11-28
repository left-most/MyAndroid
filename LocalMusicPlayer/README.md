# MyAndroid
## *作者:雪山白凤凰*

## LocalMusicPlayer
### 一.功能
1.扫描本地音乐  
2.service后台播放  
3.单曲循环/顺序播放  
4.关键词搜索歌曲  
### 二.技术实现

#### 权限:
```java
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

####获取本地音乐信息:读取本地存储loadLocalMusicData  
通过getContentResolver()方法获取ContentResolver对象,使用MediaStore.Audio.Media.EXTERNAL_CONTENT_URI作为Uri地址来查询本地音乐存储中的数据,使用ContentResolver访问uri地址。遍历Cursor对象,获取歌名,歌手,歌曲时长专辑以及歌曲播放路径，将这些信息封装成一个LocalMusicBean对象，并将其添加到MainData集合中。
```java
private void loadLocalMusicData() {
        //加载本地存储当中的音乐mp3文件到集合当中
        // 获取ContentResolver对象
        ContentResolver resolver = getContentResolver();
        // 获取本地音乐存储的Uri地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // 开始查询地址
        Cursor cursor = resolver.query(uri, null, null, null, null);
        // 遍历Cursor
        int id = -1;
        LocalMusicBean bean;

        while (cursor.moveToNext()) {
            //歌曲时间
            @SuppressLint("Range") long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            //限制时长
            if (duration>30*1000) {
                //歌名
                @SuppressLint("Range") String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                //歌手
                @SuppressLint("Range") String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                //专辑
                @SuppressLint("Range") String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                if(album.equals("Sounds")) continue;
                ++id;
                @SuppressLint("Range") String sid = String.valueOf(id+1);
                //路径
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));


                bean = new LocalMusicBean(sid, song, singer, album, duration, path);
                MainData.add(bean);
            }
        }
        if(id==0) {
            bean = new LocalMusicBean("0", "没有找到本地歌曲", "", "", 0, "");
            MainData.add(bean);
            currentId=-2;
        } else{
            currentId=-1;
        }
        musicDataSize = MainData.size();
        SetData.addAll(MainData);
        Log.d("LPYloadLocalMusicData----3----","loadLocalMusicData");
        //数据源变化，提示适配器更新
        MusicAdapter.notifyDataSetChanged();
    }
```
#### 侧边栏信息
```xml
navigation.xml
<?xml version="1.0" encoding="utf-8"?>

<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <group
    android:id="@+id/group1"
    android:checkableBehavior="single">
        <item
            android:id="@+id/item_0"
            android:icon="@mipmap/icon_song2"
            android:title="首页" />

        <item
            android:id="@+id/item_2"
            android:icon="@mipmap/icon_menu_introduction"
            android:title="功能介绍" />

        <item
            android:id="@+id/item_3"
            android:icon="@mipmap/icon_menu_about"
            android:title="关于" />

    </group>
</menu>

menu_about.xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#FFF"
    android:id="@+id/menu_about">
    <ImageView
        android:layout_width="389dp"
        android:layout_height="186dp"
        android:src="@drawable/ic_launcher_my" />
    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true">
        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:layout_centerHorizontal="true"
            android:orientation="vertical">
            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="20dp"
                android:orientation="horizontal">
                <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:src="@mipmap/icon_author" />
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="作者"
                    android:textColor="@color/design_default_color_primary"
                    android:textSize="20dp" />
            </LinearLayout>
            <LinearLayout
                android:layout_marginTop="25dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="20dp"
                android:orientation="horizontal">
                <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:src="@mipmap/icon_blog" />
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="github"
                    android:textColor="@color/design_default_color_primary"
                    android:textSize="20dp" />
            </LinearLayout>
            <LinearLayout
                android:layout_marginTop="25dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="20dp"
                android:orientation="horizontal">
                <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:src="@mipmap/icon_email" />
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="email"
                    android:textColor="@color/design_default_color_primary"
                    android:textSize="20dp" />
            </LinearLayout>
        </LinearLayout>
        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:layout_marginLeft="20dp"
            android:orientation="vertical">
            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="23dp"
                android:orientation="horizontal">
                <TextView
                    android:id="@+id/link_qq"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="雪山白凤凰"
                    android:textColor="@color/design_default_color_primary"
                    android:textSize="20dp" />
            </LinearLayout>
            <LinearLayout
                android:layout_marginTop="18dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="35dp"
                android:orientation="horizontal">
                <TextView
                    android:id="@+id/link_blog"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="https://github.com/left-most/MyAndroid"
                    android:textColor="@color/design_default_color_primary"
                    android:textSize="20dp" />
            </LinearLayout>
            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="horizontal">

                <TextView
                    android:id="@+id/link_email"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="2510583985@qq.com"
                    android:textColor="@color/design_default_color_primary"
                    android:textSize="20dp" />
            </LinearLayout>
        </LinearLayout>
    </LinearLayout>
    <Button
        android:id="@+id/tuichu"
        android:backgroundTint="#f8df72"
        android:textSize="22dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="500dp"
        android:layout_marginLeft="150dp"
        android:text="退出"/>
</RelativeLayout>
```
```java
Frag_about
public class Frag_about extends Fragment implements View.OnTouchListener {
    TextView qq, email, blog;
    Button tuichu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_about, container, false);
        view.setOnTouchListener((View.OnTouchListener) this);
        qq = view.findViewById(R.id.link_qq);
        blog = view.findViewById(R.id.link_blog);
        email = view.findViewById(R.id.link_email);
        tuichu = view.findViewById(R.id.tuichu);
        tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "qq:2510583985", Toast.LENGTH_SHORT).show();
            }
        });
        blog.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
        blog.getPaint().setAntiAlias(true);//抗锯齿
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://left-most.github.com"));
                startActivity(Intent.createChooser(intent, "Choose a Browser"));
            }
        });
        email.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
        email.getPaint().setAntiAlias(true);//抗锯齿
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"2510583985@qq.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hi, This is a test mail..");
                intent.putExtra(Intent.EXTRA_TEXT   , "Welcome! Contact me anytime!  --from author");
                startActivity(Intent.createChooser(intent, "Choose an Email Client"));
            }
        });

        return view;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
```
```java
private void setDrawer() {
        navigationView.setItemIconTintList(null);
        navigationView.getChildAt(0).setVerticalScrollBarEnabled(false);
        Log.d("setDrawer","setDrawer");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.item_0) {
                    Log.d("ItemSelectedListener", "item0");
                    fragmentManager.popBackStackImmediate(null, 1);
                    transaction = fragmentManager.beginTransaction();
                    transaction.remove(fragment_message).commit();
                    transaction = fragmentManager.beginTransaction();
                    transaction.remove(fragment_introduction).commit();
                    transaction = fragmentManager.beginTransaction();
                    transaction.remove(fragment_about).commit();

                }


                if (menuItem.getItemId() == R.id.item_2) {
                    //弹出功能介绍
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment, fragment_introduction).commit();
                    Log.d("ItemSelectedListener", "item2");
                        //Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                }
                if (menuItem.getItemId() == R.id.item_3) {
                    //弹出软件开发介绍
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment, fragment_about).commit();
                    Log.d("ItemSelectedListener", "item3");
                        //Toast.makeText(MainActivity.this, "3", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                transaction.addToBackStack(null);
                return true;
            }
        });

        //设置滑动主activity跟随
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //获取高度宽度
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                Display display = MainActivity.this.getWindowManager().getDefaultDisplay();
                display.getMetrics(metrics);
                //设置点击侧边栏时主页高度改变
                //设置activity高度，注意要加上状态栏高度
                RelativeLayout relativeLayout = findViewById(R.id.main_activity);
                relativeLayout.layout(drawerView.getRight(), Notification_height, drawerView.getRight()+metrics.widthPixels, metrics.heightPixels);

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
    private void setFragment() {
        Log.d("LPYsetFragment","LPYsetFragment");
        fragmentManager = getSupportFragmentManager();
        fragment_message = new Frag_message();
        fragment_introduction = new Frag_introduction();
        fragment_about = new Frag_about();
        transaction = fragmentManager.beginTransaction();
    }
```
#### 数据库设计
```java
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
```


