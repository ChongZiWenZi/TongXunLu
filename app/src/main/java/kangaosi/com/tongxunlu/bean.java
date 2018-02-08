package kangaosi.com.tongxunlu;

/**
 * 描述：
 * 时间：2017-10-03
 * 公司：COMS
 */

public class bean {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int id;
    public String number;
    public String isYiXiang;
    public String isBoDa;//0 未拨打  1 接通  2 未接通

    public String getIsBoDa() {
        return isBoDa;
    }

    public void setIsBoDa(String isBoDa) {
        this.isBoDa = isBoDa;
    }

    public String getIsYiXiang() {
        return isYiXiang;
    }

    public void setIsYiXiang(String isYiXiang) {
        this.isYiXiang = isYiXiang;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public bean(int id,String number,String isYiXiang,String isBoDa ) {
        this.id=id;
        this.isBoDa = isBoDa;
        this.isYiXiang = isYiXiang;
        this.number = number;
    }

    public bean() {
    }

    @Override
    public String toString() {
        return "id"+id+"isBoDa"+isBoDa+"--isYiXiang"+isYiXiang+"--number"+number;
    }
}
