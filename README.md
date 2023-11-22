# MyAndroid
## *作者:雪山白凤凰*

### LocalMusicPlayer
#### 一.功能
1.扫描本地音乐  
2.service后台播放  
3.单曲循环/顺序播放  
4.关键词搜索歌曲  
#### 二.技术实现
权限:
```java
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

获取本地音乐信息:  loadLocalMusicData  
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

