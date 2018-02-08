package kangaosi.com.tongxunlu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kangaosi.com.tongxunlu.bean;

public class TaskDao {
    private SQLiteDatabase db;
    public TaskDao(Context context) {
        TaskSqlLite lite = new TaskSqlLite(context,Environment.getExternalStorageDirectory() + "/TongXunLu/Task.db" , null, 1);
        db = lite.getReadableDatabase();
    }
    public TaskDao() {

    }


    public long deleteTask(){

        return db.delete("project",null,null);
    }

    public long insertTask(bean task){
        ContentValues values=new ContentValues();
        values.put("number", task.getNumber()+"");
        values.put("isYiXiang", task.getIsYiXiang()+"");
        values.put("isBoDa", task.getIsBoDa()+"");
        return db.insert("project", null, values);
    }

    public List<bean> queryTask(){
        List<bean> list=new ArrayList<bean>();
        Cursor cursor = db.rawQuery("SELECT * FROM project", null);
        while (cursor.moveToNext()) {
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String number=cursor.getString(cursor.getColumnIndex("number"));
            String isYiXiang=cursor.getString(cursor.getColumnIndex("isYiXiang"));
            String isBoDa=cursor.getString(cursor.getColumnIndex("isBoDa"));
            list.add(new bean(id, number, isYiXiang, isBoDa));
        }
        cursor.close();
        return list;
    }


    public long updateTaskID(bean task,String strID){
        ContentValues values=new ContentValues();
        values.put("number", task.getNumber());
        values.put("isYiXiang", task.getIsYiXiang());
        values.put("isBoDa", task.getIsBoDa());
        Log.i("TXL",task.toString());
        return db.update("project", values, "id=?", new String[]{strID});
    }

}
