package com.assignment.alarmclock;

/**
 * 记录的类型。
 * <p>
 * Created by yradex on 2016/11/29.
 */

public enum RecordType {
    /**
     * 闹钟类型。
     */
    ALARM(0),
    /**
     * 计时器类型。
     */
    TIMER(1),
    /**
     * 纪念日类型。
     */
    ANNIVERSARY(2),
    /**
     * 空类型；不要使用这种类型。
     */
    NULL(3);

    private Integer value;

    RecordType(int value) {
        this.value = value;
    }

    Integer getValue() {
        return this.value;
    }

}