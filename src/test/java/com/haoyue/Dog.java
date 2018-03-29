package com.haoyue;

import java.util.*;

/**
 * Created by Lijia on 2018/3/5.
 */
public class Dog  implements  Comparable<Dog>{

    private String name;

    private int age;

    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int compareTo(Dog o) {
        if (o.getAge()==this.getAge()){
            return o.getName().compareTo(this.getName());
        }
        return o.getAge()-this.getAge();
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public static <T> List<List<T>> divider(Collection<T> datas, Comparator<? super T> c) {
        List<List<T>> result = new ArrayList<List<T>>();
        for (T t : datas) {
            boolean isSameGroup = false;
            for (int j = 0; j < result.size(); j++) {
                if (c.compare(t, result.get(j).get(0)) == 0) {
                    isSameGroup = true;
                    result.get(j).add(t);
                    break;
                }
            }
            if (!isSameGroup) {
                // 创建
                List<T> innerList = new ArrayList<T>();
                result.add(innerList);
                innerList.add(t);
            }
        }
        return result;
    }
}


class Test{
    public static void main(String args[]){


        Stack stack=new Stack();
        stack.push("a");
        stack.push("b");
        stack.push("c");



        System.out.println(stack.search("a"));
        System.out.println(stack.pop());
        System.out.println(stack.search("a"));



    }
}
