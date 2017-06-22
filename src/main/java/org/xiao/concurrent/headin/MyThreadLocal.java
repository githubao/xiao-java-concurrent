package org.xiao.concurrent.headin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * threadlocal
 *
 * @author BaoQiang
 * @version 2.0
 * @date: 2017/6/22 20:04
 */

public class MyThreadLocal {
    private static ThreadLocal<DateFormat> df = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-M-dd"));

}
