package com.haoyue;

/**
 * Created by LiJia on 2017/12/15.
 */
import java.util.Scanner;

public class WuZiQi {
    String a = "●";
    String b = "○";
    boolean bool = true;//作为黑白棋交替标记；
    String[][] qipan = new String[25][25];
    public WuZiQi() {
        this.Qipan();
    }
    //棋盘构造25*25，遍历二维数组赋值“+”每25个换行即可
    public void Qipan() {
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                qipan[i][j] = "+";
                System.out.print(qipan[i][j]);
                while (j == 24) {
                    System.out.print("\n");
                    break;
                }
            }
        }
        xiaqi();
    }
    //操作下棋的方法
    public void xiaqi() {

        System.out.println("请输入坐标：");
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int y = sc.nextInt();
        if (x > 24 || y > 24) {
            System.out.println("超出坐标范围，请重新输入！");
            xiaqi();
        } else {
            if(qipan[x][y]==a||qipan[x][y]==b){
                System.out.println("坐标已被其它旗子占用，请重新输入");
                xiaqi();
            }else{
                if (bool) {
                    qipan[x][y] = a;
                    bool = false;
                } else {
                    qipan[x][y] = b;
                    bool = true;
                }
            }
            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < 25; j++) {
                    System.out.print(qipan[i][j]);
                    while (j == 24) {
                        System.out.print("\n");
                        break;
                    }
                }
            }
            panduan();
        }
    }
    //判断输赢的方法
    public void panduan() {
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                if (qipan[i][j] == a && qipan[i][j + 1] == a
                        && qipan[i][j + 2] == a && qipan[i][j + 3] == a
                        && qipan[i][j + 4] == a) {
                    System.out.println("黑方赢");
                    System.out.println("是否进行下一局？\n Y、进行下一局\n N、取消");
                    Scanner sc = new Scanner(System.in);
                    String f=sc.next();
                    if(f.equals("Y")){
                        Qipan();
                    }else{
                        System.out.println("gameover");
                    }
                } else if (qipan[i][j] == a && qipan[i + 1][j] == a
                        && qipan[i + 2][j] == a && qipan[i + 3][j] == a
                        && qipan[i + 4][j] == a) {
                    System.out.println("黑方赢");
                    System.out.println("是否进行下一局？\n Y、进行下一局\n N、取消");
                    Scanner sc = new Scanner(System.in);
                    String f=sc.next();
                    if(f.equals("Y")){
                        Qipan();
                    }else{
                        System.out.println("gameover");
                    }
                } else if (qipan[i][j] == a && qipan[i + 1][j + 1] == a
                        && qipan[i + 2][j + 2] == a && qipan[i + 3][j + 3] == a
                        && qipan[i + 4][j + 4] == a) {
                    System.out.println("黑方赢");
                    System.out.println("是否进行下一局？\n Y、进行下一局\n N、取消");
                    Scanner sc = new Scanner(System.in);
                    String f=sc.next();
                    if(f.equals("Y")){
                        Qipan();
                    }else{
                        System.out.println("gameover");
                    }
                } else if (qipan[i][j + 4] == a && qipan[i + 1][j + 3] == a
                        && qipan[i + 2][j + 2] == a && qipan[i + 3][j + 1] == a
                        && qipan[i + 4][j + 0] == a) {
                    System.out.println("黑方赢");
                    System.out.println("是否进行下一局？\n Y、进行下一局\n N、取消");
                    Scanner sc = new Scanner(System.in);
                    String f=sc.next();
                    if(f.equals("Y")){
                        Qipan();
                    }else{
                        System.out.println("gameover");
                    }
                } else if (qipan[i][j] == b && qipan[i][j + 1] == b
                        && qipan[i][j + 2] == b && qipan[i][j + 3] == b
                        && qipan[i][j + 4] == b) {
                    System.out.println("白方赢");
                    System.out.println("是否进行下一局？\n Y、进行下一局\n N、取消");
                    Scanner sc = new Scanner(System.in);
                    String f=sc.next();
                    if(f.equals("Y")){
                        Qipan();
                    }else{
                        System.out.println("gameover");
                    }
                } else if (qipan[i][j] == b && qipan[i + 1][j] == b
                        && qipan[i + 2][j] == b && qipan[i + 3][j] == b
                        && qipan[i + 4][j] == b) {
                    System.out.println("白方赢");
                    System.out.println("是否进行下一局？\n Y、进行下一局\n N、取消");
                    Scanner sc = new Scanner(System.in);
                    String f=sc.next();
                    if(f.equals("Y")){
                        Qipan();
                    }else{
                        System.out.println("gameover");
                    }
                } else if (qipan[i][j] == b && qipan[i + 1][j + 1] == b
                        && qipan[i + 2][j + 2] == b && qipan[i + 3][j + 3] == b
                        && qipan[i + 4][j + 4] == b) {
                    System.out.println("白方赢");
                    System.out.println("是否进行下一局？\n Y、进行下一局\n N、取消");
                    Scanner sc = new Scanner(System.in);
                    String f=sc.next();
                    if(f.equals("Y")){
                        Qipan();
                    }else{
                        System.out.println("gameover");
                    }
                } else if (qipan[i][j + 4] == b && qipan[i + 1][j + 3] == b
                        && qipan[i + 2][j + 2] == b && qipan[i + 3][j + 1] == b
                        && qipan[i + 4][j + 0] == b) {
                    System.out.println("白方赢");
                    System.out.println("是否进行下一局？\n Y、进行下一局\n N、取消");
                    Scanner sc = new Scanner(System.in);
                    String f=sc.next();
                    if(f.equals("Y")){
                        Qipan();
                    }else{
                        System.out.println("gameover");
                    }
                }else{
                    xiaqi();
                }
            }
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        WuZiQi wzq = new WuZiQi();
    }


}
