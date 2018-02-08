package kangaosi.com.tongxunlu.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskSqlLite extends SQLiteOpenHelper{
    private static final String CREATE_TABLE_Task=
            "create table project(" +
                    "id integer primary key autoincrement," +
                    "number VARCHAR(100)," +
                    "isYiXiang VARCHAR(100)," +
                    "isBoDa VARCHAR(20));";
    public TaskSqlLite(Context context, String name, SQLiteDatabase.CursorFactory factory,
                       int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_Task);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
