# AlarmClockAssignment-Android

com.assignment.alarmclock.AlarmManager 类里有一些用于记录存储的接口，可以先用着

com.assignment.alarmclock.RecordQueueManager 类里面的函数不要调用

com.assignment.alarmclock.Record 是所有记录项需要实现的接口, 其中 getNextTriggerTime() 返回该记录最近下一次提醒的时间，isActive() 返回该记录是否激活

主文件 runService() 里有条调用语句，运行以后过 5 秒会自动激活指定的 Activity，有需要可以试用
