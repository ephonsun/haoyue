package com.haoyue;

/**
 * Created by LiJia on 2017/11/17.
 */
public enum  MyEnum {

    red("红色",1),blue("蓝色",2),white("白色",3),green("绿色",4);

    private String name;
    private int index;

    MyEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    MyEnum() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "MyEnum{" +
                "name='" + name + '\'' +
                ", index=" + index +
                '}';
    }
}
