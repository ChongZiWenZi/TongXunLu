package kangaosi.com.tongxunlu;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import kangaosi.com.tongxunlu.db.TaskDao;

/**
 * 描述：
 * 时间：2017-10-04
 * 公司：COMS
 */

public class PhoneReceiver extends BroadcastReceiver {
    String id="";
    TaskDao dao;
    @Override
    public void onReceive(Context context, Intent intent) {
         //如果是去电
        id=intent.getStringExtra("id");
        dao=new TaskDao();
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            String phoneNumber = intent
                    .getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }else{
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }
}

    PhoneStateListener listener=new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    System.out.println("挂断");

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    System.out.println("已拨打");
                     break;
                case TelephonyManager.CALL_STATE_RINGING:
                    System.out.println("响铃:来电号码"+incomingNumber);
                    //输出来电号码
                    break;
            }
        }
    };

}
