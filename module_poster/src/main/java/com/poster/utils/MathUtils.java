package com.poster.utils;

import static org.jcodec.common.tools.MathUtil.gcd;

public class MathUtils {
    /**
     * n个数的最小公倍数
     *
     * @return
     */
    public static int nlcm(int[] m) {
        int min = 0;
        for (int i = 0; i < m.length - 1; i++) {
            min = lcm(m[i], m[i + 1]);
            m[i + 1] = min;
        }
        return min;
    }

    /**
     * 求两个数的最小公倍数
     *
     * @param a
     * @param b
     * @return
     */
    public static int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }
}
