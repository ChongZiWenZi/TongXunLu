package kangaosi.com.tongxunlu;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kangaosi.com.tongxunlu.db.TaskDao;


public class Task_Adapter extends BaseAdapter {
    private Map<Integer,Boolean> map=new HashMap<>();// 存放已被选中的CheckBox
    private Context context;
    private List<bean> list;
    TaskDao taskDao;
    public Task_Adapter(Context context, List<bean> list){
        this.context=context;
        this.list=list;
    }
    public Task_Adapter(Context context, List<bean> list,TaskDao taskDao){
        this.context=context;
        this.list=list;
        this.taskDao=taskDao;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHoder hoder;
        if (view == null) {
            hoder = new ViewHoder();
            view=View.inflate(context, R.layout.item, null);
            hoder.item_yiXiang=(CheckBox) view.findViewById(R.id.item_yiXiang);
            hoder.item_Number=(TextView) view.findViewById(R.id.item_Number);
            hoder.item_BoDa=(TextView) view.findViewById(R.id.item_BoDa);
            view.setTag(hoder);
        } else {
            hoder = (ViewHoder) view.getTag();
        }
        hoder.item_yiXiang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    map.put(position,true);
                    list.get(position).setIsYiXiang(isChecked+"");
                    taskDao.updateTaskID(list.get(position),list.get(position).getId()+"");
                }else {
                    list.get(position).setIsYiXiang(isChecked+"");
                    map.remove(position);
                    taskDao.updateTaskID(list.get(position),list.get(position).getId()+"");
                }
            }
        });
        if(map!=null&&map.containsKey(position)){
            hoder.item_yiXiang.setChecked(true);
        }else {
            hoder.item_yiXiang.setChecked(false);
        }
        Boolean b=new Boolean(list.get(position).getIsYiXiang()+"");
        hoder.item_yiXiang.setChecked(b);
        hoder.item_Number.setText(list.get(position).getNumber());
        if(list.get(position).getIsBoDa().equals("0")){//0 未拨打  1 接通  2 未接通
            hoder.item_BoDa.setText("未拨打");
            hoder.item_BoDa.setTextColor((Color.rgb(255,140,0)));
        }if(list.get(position).getIsBoDa().equals("1")){//0 未拨打  1 接通  2 未接通
            hoder.item_BoDa.setText("接通");
            hoder.item_BoDa.setTextColor((Color.rgb(50,205,50)));
        }if(list.get(position).getIsBoDa().equals("2")){//0 未拨打  1 接通  2 未接通
            hoder.item_BoDa.setText("未接通");
            hoder.item_BoDa.setTextColor((Color.rgb(255,0,0)));
        }
        return view;
    }
    class ViewHoder {
        CheckBox item_yiXiang;
        TextView item_Number;
        TextView item_BoDa;
    }
}
