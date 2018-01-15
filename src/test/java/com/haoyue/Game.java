package com.haoyue;

/**
 * Created by LiJia on 2017/12/29.
 */
import java.util.Scanner;

public class Game {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Play p = new Play();
        System.out.println("游戏现在开始" + "输入 0:石头  1：剪刀 2：布");
        p.begin();
    }
}

class Play {
    public void begin() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if (n == 0) {
            System.out.println("你出的是石头");

        }
        if (n == 1) {
            System.out.println("你出的是剪刀");

        }
        if (n == 2) {
            System.out.println("你出的是布");

        }
        int x = (int) (Math.random() * 2);
        if (x == 0) {
            System.out.println("电脑出的是石头");

        }
        if (x == 1) {
            System.out.println("电脑出的是剪刀");

        }
        if (x == 2) {
            System.out.println("电脑出的是布");

        }
        if ((n == 0 && x == 1) || (n == 1 && x == 2) || (n == 2 && x == 0)) {
            System.out.println("恭喜你赢了");
        } else if (n == x) {
            System.out.println("平局");
        } else {
            System.out.println("电脑赢了");
        }
    }
}


