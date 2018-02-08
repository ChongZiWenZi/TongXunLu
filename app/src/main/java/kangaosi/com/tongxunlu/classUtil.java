package kangaosi.com.tongxunlu;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述：
 * 时间：2017-10-05
 * 公司：COMS
 */

public class classUtil {
    /**
     * 利用系统CallLog获取通话历史记录
     * @return
     */

    public static int getCallHistoryList(Context context, ContentResolver cr){

        Cursor cs;
        cs=cr.query(CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
                new String[]{
                        CallLog.Calls.CACHED_NAME,  //姓名
                        CallLog.Calls.NUMBER,    //号码
                        CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                        CallLog.Calls.DATE,  //拨打时间
                        CallLog.Calls.DURATION   //通话时长
                },null,null,CallLog.Calls.DEFAULT_SORT_ORDER);
        String callHistoryListStr="";
        int callDuration=0;
        int valuye=0;
        int i=0;
        if(cs!=null &&cs.getCount()>0){
            for(cs.moveToFirst();!cs.isAfterLast() & i<1; cs.moveToNext()){
                String callName=cs.getString(0);
                String callNumber=cs.getString(1);
                //通话类型
                int callType=Integer.parseInt(cs.getString(2));
                String callTypeStr="";
                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr="呼入";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr="呼出";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr="未接";
                        break;
                }
                //拨打时间
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date callDate=new Date(Long.parseLong(cs.getString(3)));
                String callDateStr=sdf.format(callDate);
                //通话时长
                callDuration=Integer.parseInt(cs.getString(4));
                int min=callDuration/60;
                int sec=callDuration%60;
                valuye=min+sec;
                /*String callDurationStr=min+"分"+sec+"秒";
                String callOne="类型：" + callTypeStr + ", 称呼：" + callName + ", 号码："
                        +callNumber + ", 通话时长：" + callDurationStr + ", 时间:" + callDateStr
                        +"\n---------------------\n";

                callHistoryListStr+=callOne+"时间"+callDateStr;*/
                i++;
            }
        }

        return valuye;
    }
}
