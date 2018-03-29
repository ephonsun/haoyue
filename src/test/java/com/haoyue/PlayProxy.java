package com.haoyue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Lijia on 2018/3/26.
 */
public class PlayProxy {

    private Object target;

    public PlayProxy(Object target) {
        this.target = target;
    }

    public Object getProcy() {
       Object proxy= Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String method_name=method.getName();
                Object result=null;
                //方法返回值
                System.out.println("proxy..before");
                result=method.invoke(target,args);
                System.out.println("proxy..after");
                return result;
            }
        });

        return proxy;

    }


    public static void main(String args[]){

        Play target=new PlayImpy();
        Play proxy= (Play) new PlayProxy(target).getProcy();
        proxy.playgames();

    }
}
