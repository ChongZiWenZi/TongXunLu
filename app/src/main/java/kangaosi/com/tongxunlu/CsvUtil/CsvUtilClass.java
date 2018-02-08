package kangaosi.com.tongxunlu.CsvUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kangaosi.com.tongxunlu.bean;

/**
 * 描述：
 * 时间：2017-10-03
 * 公司：COMS
 */

public class CsvUtilClass {
    public static final String TAG = "CsvUtil";

    private BufferedReader bufferedreader = null;
    private List<String> list = new ArrayList<String>();
    private List<bean> locSegments = new ArrayList<bean>();

    public CsvUtilClass(InputStream inputStream) throws IOException {

        bufferedreader = new BufferedReader(new InputStreamReader(inputStream));

        String stemp;

        while ((stemp = bufferedreader.readLine()) != null) {

            list.add(stemp);
        }
    }

    public CsvUtilClass(String filename) throws IOException {

        bufferedreader = new BufferedReader(new FileReader(filename));

        String stemp;

        while ((stemp = bufferedreader.readLine()) != null) {

            list.add(stemp);
        }
    }

    public List getList() throws IOException {

        return list;
    }

    public int getRowNum() {

        return list.size();
    }

    public int getColNum() {

        if (!list.toString().equals("[]")) {

            if (list.get(0).toString().contains(",")) {
                return list.get(0).toString().split(",").length;
            } else if (list.get(0).toString().trim().length() != 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public String getRow(int index) {

        if (this.list.size() != 0)
            return (String) list.get(index);
        else
            return null;
    }

    public String getCol(int index) {

        if (this.getColNum() == 0) {
            return null;
        }

        StringBuffer scol = new StringBuffer();
        String temp = null;
        int colnum = this.getColNum();

        if (colnum > 1) {
            for (Iterator it = list.iterator(); it.hasNext();) {
                temp = it.next().toString();
                scol = scol.append(temp.split(",")[index] + ",");
            }
        } else {
            for (Iterator it = list.iterator(); it.hasNext();) {
                temp = it.next().toString();
                scol = scol.append(temp + ",");
            }
        }
        String str = new String(scol.toString());
        str = str.substring(0, str.length() - 1);
        return str;
    }

    public String getString(int row, int col) {

        String temp = null;
        int colnum = this.getColNum();
        if (colnum > 1) {
            temp = list.get(row).toString().split(",")[col];
        } else if (colnum == 1) {
            temp = list.get(row).toString();
        } else {
            temp = null;
        }
        return temp.replace("\"", "");
    }

    public void CsvClose() throws IOException {
        this.bufferedreader.close();
    }

    public void run() throws IOException {


      //这里需要根据具体的CSV文件内容和业务需求来决定取那些字段
        //the title not used, so we start from 1 row
        for (int i = 1; i < getRowNum() - 1; i++) {

            String preFix = getString(i, 1);

            String startLoc = getString(i, 2);
            String endLoc = getString(i, 3);

            //Util.BIZ_CONF_DEBUG(TAG, preFix + "    " + startLoc + "    " + endLoc);

            bean segment = new bean(0,preFix, startLoc, endLoc);

            locSegments.add(segment);
        }

        CsvClose();
    }

    public List<bean> getSegments() {

        return locSegments;
    }
}
