package kangaosi.com.tongxunlu;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kangaosi.com.tongxunlu.db.TaskDao;

public class MainActivity extends AppCompatActivity {
    private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/TongXunLu/CSV");
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;

    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.tv_WeiBoDa)
    TextView tvWeiBoDa;
    @BindView(R.id.tv_JieTong)
    TextView tvJieTong;
    @BindView(R.id.tv_WeiJieTong)
    TextView tvWeiJieTong;
    Task_Adapter adapter;
    @BindView(R.id.iv_import)
    TextView ivImport;
    @BindView(R.id.tv_Delect)
    TextView tvDelect;
    TaskDao taskDao;
    List<bean> list=new ArrayList<>();;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            list= taskDao.queryTask();
            if(list!=null){
                adapter = new Task_Adapter(MainActivity.this, list,taskDao);
                lv.setAdapter(adapter);
                DataSize(list);
            }
        }
    };
   int PositionISClick=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        taskDao=new TaskDao(this);
        list= taskDao.queryTask();
        if(list!=null&&list.size()>0){
            DataSize(list);
            adapter = new Task_Adapter(MainActivity.this, list,taskDao);
            lv.setAdapter(adapter);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PositionISClick=position;
                isDlose=false;
                call(list.get(position).getNumber(),list.get(position).getId()+"");
            }
        });
    }
    int WeiBoDaNum=0;
    int JieTongNum=0;
    int WeiJieTongNum=0;
   public void DataSize(List<bean> list){
       WeiBoDaNum=0;
       JieTongNum=0;
       WeiJieTongNum=0;
       for(bean b :list){
           if(b.getIsBoDa().equals("0")){
               WeiBoDaNum++;
           }if(b.getIsBoDa().equals("1")){
               JieTongNum++;
           }if(b.getIsBoDa().equals("2")){
               WeiJieTongNum++;
           }
       }
       tvWeiBoDa.setText("未拨打："+WeiBoDaNum);

         tvJieTong.setText("接通："+JieTongNum);

         tvWeiJieTong.setText("未接通："+WeiJieTongNum);
   }

  public  int num=0;
    public void getCSVData() {
        if (!PHOTO_DIR.exists()) {// 目录不存在则创建该目录及其不存在的父目录
            PHOTO_DIR.mkdirs();
            Toast.makeText(MainActivity.this, "请把CSV文件放入/TongXunLu/CSV", Toast.LENGTH_LONG).show();
        } else {
            List<bean> dataList = importCsv(new File(PHOTO_DIR + "/Campaign.csv"));
            dataList.remove(0);
            if (dataList != null && !dataList.isEmpty()) {
                for(bean data : dataList){
                    taskDao.insertTask(data);
                }
            }
            handler.sendEmptyMessage(1);
        }
    }

    public static List<bean> importCsv(File file) {
        List<bean> dataList = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] aa = line.split(",");
                bean b = new bean(0, aa[1], "false", "0");
                dataList.add(b);
            }
        } catch (Exception e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }
String numberHostorl="";

    @OnClick({R.id.iv_import, R.id.tv_Delect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_import:
                getCSVData();
               // Toast.makeText(MainActivity.this, "dianJi", Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_Delect:
              int a= (int) taskDao.deleteTask();
                if(a>0){
                    Toast.makeText(MainActivity.this, "已清空", Toast.LENGTH_LONG).show();
                    WeiBoDaNum=0;
                    JieTongNum=0;
                    WeiJieTongNum=0;
                tvWeiBoDa.setText("未拨打："+WeiBoDaNum);
                tvJieTong.setText("接通："+JieTongNum);
                tvWeiJieTong.setText("未接通："+WeiJieTongNum);
                }
                list= taskDao.queryTask();
                adapter = new Task_Adapter(MainActivity.this, list);
                lv.setAdapter(adapter);
                break;
        }
    }
    private void call(String phone,String ID) {
        numberHostorl=phone;
        Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
        intent.putExtra("id",ID);
        startActivityForResult(intent,11111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ContentResolver resolver = getContentResolver();
        int isValue= classUtil.getCallHistoryList(MainActivity.this,resolver);

        if(isValue>0){
            list.get(PositionISClick).setIsBoDa("1");
            taskDao.updateTaskID(list.get(PositionISClick),list.get(PositionISClick).getId()+"");
            adapter.notifyDataSetChanged();
        }else {
            list.get(PositionISClick).setIsBoDa("2");
            taskDao.updateTaskID(list.get(PositionISClick),list.get(PositionISClick).getId()+"");
            adapter.notifyDataSetChanged();
        }
        for (int b=0;b<list.size();b++){
            if(list.get(b).getIsBoDa().equals("0")){
                boolean isloase= showNormalDialog(list.get(b).getNumber(),list.get(b).getId()+"",b);
                PositionISClick=b;
                if(!isDlose){
                    break;
                }
            }
        }
        DataSize(list);
        //list=taskDao.queryTask();
        System.out.println(list.toString()+"BMS");


    }
    boolean isDlose=true;
    private boolean showNormalDialog(final String NumberStr, final String id, final int bPostion){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);

        normalDialog.setTitle("是否拨打下一个号码");
        normalDialog.setMessage(NumberStr+"");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        call(NumberStr,id);
                        list.get(bPostion).setIsBoDa("2");
                    }
                });
          normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        isDlose=false;
                    }
                });
        // 显示
        normalDialog.show();
        return  isDlose;
    }

}
