package com.assignment.alarmclock;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 所有记录的接口。
 * <p>
 * Created by yradex on 2016/11/23.
 */

public interface Record extends Serializable {

    /**
     * 取得记录的 id。
     *
     * @return 记录的 id
     */
    int getId();

    /**
     * 设定记录的 id。
     * <p>
     * 不要在你的程序中调用这个函数。id 将会在记录插入后台时自动设定。
     *
     * @param id 要设定的 id
     */
    void setId(int id);

    /**
     * 取得该记录的下一次触发时间。
     * <p>
     * 结果应当是使用该函数被调用的时间计算的。这样当一个能够被多次触发的记录某一次被触发时，调用这个函数就会自动得到下一次的触发时间。
     *
     * @return 该记录的下一次触发时间
     */
    Calendar getNextTriggerTime();

    /**
     * 取得该记录的激活与否。如果记录不被激活，即使到了应该触发的时间，也不会被触发。
     *
     * @return true 如果该记录被激活
     */
    boolean isActive();

    /**
     * 取得该记录被激活时的处理类。该类应该是 activity 的子类。一旦记录被激活，该类就会被调用。应当在该类中设定记录的处理方式。
     *
     * @return 该记录被激活时的处理类
     */
    Class activityToHandleThisAlarm();
}
