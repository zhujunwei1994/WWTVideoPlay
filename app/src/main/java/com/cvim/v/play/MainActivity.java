package com.cvim.v.play;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mVideoPath;
    private ListView mLvVideoList;
    private USBHelper mHelper;
    private  VideoListAdapter adapter;
    private  ReadVideoFile readVideoFile;

    // selected area
    private ListView mLvSelected;
    private int SELECTED_ITEM = 5;
    private  VideoListSelectedAdapter mSelectedAdapter;
    private ArrayList<File> mSelectedVideoItem = new ArrayList<File>();
    private  ArrayList<File> mVideoLists =new ArrayList<File>();


    // ok  cancel
    private Button mOkBtn;
    private Button mCancelBtn;

    private File mParentFiles;

    private int selectedItemPosition = 0;


    private ListView mSelectedVideoLV;
    int SLVIDEOFILE = 6;
    SelectedVideoListAdapter mSelectedVideoFileAdapter;

     Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            int what = msg.what;

            if (SLVIDEOFILE==what)
            {
                slVideoFile.clear();
                mSelectedVideoFileAdapter = new SelectedVideoListAdapter(paserAllVideoFile(mSelectedVideoItem.size()));

                Log.i("main", "mVideoLists.size = " + mSelectedVideoItem.size() + "  slVideoFile.size = " + slVideoFile.size());
                mSelectedVideoLV.setAdapter(mSelectedVideoFileAdapter);
                mSelectedVideoFileAdapter.notifyDataSetChanged();
            }else if (what == SELECTED_ITEM&&mSelectedVideoItem.size()>0)
            {
                mSelectedAdapter = new VideoListSelectedAdapter(mSelectedVideoItem);
                mLvSelected.setAdapter(mSelectedAdapter);
                mSelectedAdapter.notifyDataSetChanged();

            }else if (what == 500) // 从Sdcard 读取数据完成
            {
                if (null!=readVideoFile&&null != readVideoFile.filesData)
                {
                    mVideoLists = readVideoFile.filesData;
                    mVideoPath.setText(mVideoLists.get(0).getAbsolutePath());
                    adapter = new VideoListAdapter(mVideoLists);
                    mLvVideoList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }else  if (what == 200) // 读取usb数据完成
            {
                if (null!=mHelper&&null!=mHelper.fileData&&mHelper.fileData.size()>0)
                {
                    mVideoPath.setText(mHelper.fileData.get(0).getAbsolutePath());
                    adapter = new VideoListAdapter(mHelper.fileData);
                    mLvVideoList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }else if (what == 400) // 读取数据完成
            {
                if (null!=adapter)
                {
                    mVideoPath.setText("没有视频文件");
                    adapter.mData.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initView();

        //         读取U盘里面的数据
        mHelper = new USBHelper(MainActivity.this,this);
        mHelper.init();


        //readLocalData();
    }
    void readLocalData()
    {

        //首先先检车Sdcard是否存在
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            readVideoFile = new ReadVideoFile(this,this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if ( readVideoFile.isGrantExternalRW(this))
                {
                    readVideoFile.readFile();
                }
            }else
            {
                readVideoFile.readFile();
            }
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction()== KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            if (mParentFiles == null|| mVideoLists ==null) { finish();  return  true;  }

            if (    "/storage/emulated/0".equals(mParentFiles.getPath())
                    ||"/storage/emulated/0".equals(mVideoLists.get(0).getParentFile().getPath()))
                {   finish(); return true;  }

                if (mVideoLists.isEmpty() )
                {
                    mVideoLists = readVideoFile.parseFileList(mParentFiles.getParentFile().listFiles());
                    mVideoPath.setText(mParentFiles.getAbsolutePath());
                    adapter.setmData(mVideoLists);
                }else
                {
                    mVideoLists = readVideoFile.parseFileList(mVideoLists.get(0).getParentFile().getParentFile().listFiles());
                    //  mVideoPath.setText(mVideoLists.get(0).getAbsolutePath());
                    adapter.setmData(mVideoLists);
                }
                adapter.notifyDataSetChanged();
                return true;
        }

        if (event.getRepeatCount() == 12)
        {
            Log.i("main","currentitem = "+mVideoLists.get(selectedItemPosition));

            if (mVideoLists.get(selectedItemPosition).isDirectory()&&event.getKeyCode()==KeyEvent.KEYCODE_DPAD_LEFT)
            {
                Log.i("main","--->currentitem = "+mVideoLists.get(selectedItemPosition));

                mVideoPath.setText(mVideoLists.get(selectedItemPosition).getAbsolutePath());
                mSelectedVideoItem.add(mVideoLists.get(selectedItemPosition));
                mHandler.sendEmptyMessage(SELECTED_ITEM);
                mHandler.sendEmptyMessageDelayed(SLVIDEOFILE,100);

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView()
    {

        mVideoPath = (TextView)findViewById(R.id.video_path);
        mLvVideoList = (ListView)findViewById(R.id.lv_video_list);

        mLvVideoList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mVideoLists.size()<position) return;
//
                selectedItemPosition = position;
                mVideoPath.setText(mVideoLists.get(position).getAbsolutePath());
                mParentFiles = mVideoLists.get(position);

                Log.i("main","onItemSelected position = "+position+"   selectedItemPosition = "+selectedItemPosition+"  mParentFiles = "+mParentFiles.getPath());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("main","onNothingSelected ------- = ");
            }
        });

        mLvVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mVideoLists.size()<position) return;
                if (!mVideoLists.get(position).isDirectory()) return;
                if (mVideoLists.get(position).listFiles().length == 0) return;

                mParentFiles = mVideoLists.get(position);

                mVideoLists = readVideoFile.parseFileList(mVideoLists.get(position).listFiles());
                adapter.setmData(mVideoLists);
                adapter.notifyDataSetChanged();
//                mVideoPath.setText(mVideoLists.get(position).getAbsolutePath());
//                mSelectedVideoItem.add(mVideoLists.get(position));
//                mHandler.sendEmptyMessage(SELECTED_ITEM);
            }
        });

        mLvSelected = (ListView)findViewById(R.id.lv_selected);

        mOkBtn = (Button)findViewById(R.id.ok_btn);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("main","**********************ok ***mSelectedVideoItem ****************");
                if (mSelectedVideoItem.size()>0)
                {
                    Log.i("main","**********************ok ***mSelectedVideoItem *************mSelectedVideoItem = "+mSelectedVideoItem.size());
                    for (File f: mSelectedVideoItem)
                    {
                        Log.i("main","mOkBtn.setOnClickListener f.name = "+f.getName()+"    f.path = "+f.getPath()+"   f.getABSPath = "+f.getAbsolutePath() );
                    }

                    Intent intent = new Intent(MainActivity.this,VideoPlay.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selectedFile",mSelectedVideoItem);
                    intent.putExtra("files",bundle);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(MainActivity.this,"播放列表为空、请重新选择需要播放的文件",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCancelBtn = (Button)findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mSelectedVideoItem&&mSelectedVideoItem.size()>0)
                {
                    Log.i("main","**************cancel ***********mSelectedVideoItem ****************");
                    mSelectedVideoItem.clear();
                    mSelectedAdapter.mData.clear();
                    mSelectedAdapter.notifyDataSetChanged();

                    mSelectedVideoItem.clear();
                    mSelectedVideoFileAdapter.mData.clear();
                    mSelectedVideoFileAdapter.notifyDataSetChanged();

                }else if (mSelectedVideoItem.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"请重新选择播放的文件",Toast.LENGTH_SHORT).show();
                }
            }
        });


        mSelectedVideoLV = (ListView)findViewById(R.id.lv_selected_video_filelist);
    }



   private class VideoListAdapter extends BaseAdapter
   {
       public void setmData(ArrayList<File> mData) {
           this.mData = mData;
       }

       private ArrayList <File>mData;

       public VideoListAdapter(ArrayList mData) {
           this.mData = mData;
       }

       @Override
       public int getCount() {
           return null!=mData&&mData.size()>0?mData.size():0;
       }

       @Override
       public Object getItem(int position) {
           return mData!=null&&mData.size()>0?mData.get(position):null;
       }

       @Override
       public long getItemId(int position) {
           return null!=mData?position:0;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder viewHolder;
           if (null == convertView) {
               convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.video_list_item, null);
               viewHolder = new ViewHolder();
               viewHolder.viewName = convertView.findViewById(R.id.video_item);
               viewHolder.videoPath = convertView.findViewById(R.id.video_item_path);
               convertView.setTag(viewHolder);
           }else {
               viewHolder = (ViewHolder)convertView.getTag();
           }
           viewHolder.viewName.setText(mData!=null?mData.get(position).getName():"123");
//           viewHolder.videoPath.setText(mData!=null?mData.get(position).getAbsolutePath():"123");
           return convertView;
       }

       class ViewHolder {
           TextView viewName;
           TextView videoPath;
       }
   }

    private class VideoListSelectedAdapter extends BaseAdapter
    {
        private ArrayList <File>mData;

        public VideoListSelectedAdapter(ArrayList mData) {
            this.mData = mData;
        }
        @Override
        public int getCount() {
            return null!=mData&&mData.size()>0?mData.size():0;
        }

        @Override
        public Object getItem(int position) {
            return mData!=null?mData.get(position):null;
        }

        @Override
        public long getItemId(int position) {
            return null!=mData&&mData.size()>0?position:0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.video_list_selected_item, null);
                viewHolder = new ViewHolder();
                viewHolder.viewName = convertView.findViewById(R.id.video_seleceted_item);
                viewHolder.videoPath = convertView.findViewById(R.id.video_Selected_item_path);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.viewName.setText(mData!=null?mData.get(position).getName():"**************");
//            viewHolder.videoPath.setText(mData!=null?mData.get(position).getAbsolutePath():"");
            return convertView;
        }
        class ViewHolder {
            TextView viewName;
            TextView videoPath;
        }
    }

    private class SelectedVideoListAdapter extends BaseAdapter
    {
        public void setmData(ArrayList<File> mData) {
            this.mData = mData;
        }

        private ArrayList <File>mData;

        public SelectedVideoListAdapter(ArrayList mData) {
            this.mData = mData;
        }

        @Override
        public int getCount() {
            return null!=mData&&mData.size()>0?mData.size():0;
        }

        @Override
        public Object getItem(int position) {
            return mData!=null&&mData.size()>0?mData.get(position):null;
        }

        public ArrayList<File> getmData() {
            return mData;
        }

        @Override
        public long getItemId(int position) {
            return null!=mData?position:0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.selected_video_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.viewName = convertView.findViewById(R.id.seleceted_video_list_item_name);
                viewHolder.videoPath = convertView.findViewById(R.id.selected_video_list_item_path);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.viewName.setText(mData!=null?mData.get(position).getName():"123");
//           viewHolder.videoPath.setText(mData!=null?mData.get(position).getAbsolutePath():"123");
            return convertView;
        }

        class ViewHolder {
            TextView viewName;
            TextView videoPath;
        }
    }

    @Override
    protected void onDestroy() {
       // mHelper.unRegisterReceiver();
        super.onDestroy();
    }

    ArrayList <File>slVideoFile = new ArrayList<File>();
    ArrayList<File>  paserAllVideoFile(int count)
    {
        if (count ==0) return null;
        if (count>mSelectedVideoItem.size()) count=mSelectedVideoItem.size();
        for (int i = 0; i<count;i++)
        {
            paraseVideFile(mSelectedVideoItem.get(i));
        }
        return slVideoFile;
    }

   void paraseVideFile(File file)
    {
        File[] files = file.listFiles();
        for (File f :files){
            if (f.getName().endsWith(".mp4")
                    ||f.getName().endsWith(".wma")
                    ||f.getName().endsWith(".ogg")
                    ||f.getName().endsWith(".pcm")
                    ||f.getName().endsWith(".m4a")
                    ||f.getName().endsWith(".ac3")
                    ||f.getName().endsWith(".ec3")
                    ||f.getName().endsWith(".dtshd")
                    ||f.getName().endsWith(".wav")
                    ||f.getName().endsWith(".rm")
                    ||f.getName().endsWith(".ra")
                    ||f.getName().endsWith(".cd")
                    ||f.getName().endsWith(".amr")
                    ||f.getName().endsWith(".dts")
                    ||f.getName().endsWith(".dts")
                    ||f.getName().endsWith(".midi")
                    ||f.getName().endsWith(".snd")
                    ||f.getName().endsWith(".vqf")
                    ||f.getName().endsWith(".cda")
                    ||f.getName().endsWith(".aiff"))
            {
                slVideoFile.add(f);
            }
        }
    }

    void startPlayVideo(int position)
    {
        if (null!=mHelper.fileData)
        {
            Intent intent = new Intent(MainActivity.this,VideoPlay.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("viideoList",mHelper.fileData);
            intent.putExtra("position",position);

            intent.putExtra("video",bundle);
            startActivity(intent);
        }
    }
}
