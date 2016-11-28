# AlarmClockAssignment-Android

com.assignment.alarmclock.AlarmManager 类里有一些用于记录存储的接口，可以先用着

com.assignment.alarmclock.Record 是所有记录项需要实现的接口, 其中 getNextTriggerTime() 返回该记录最近下一次提醒的时间（应该能够根据当前时间自动变化），isActive() 返回该记录是否激活

===

现在修复了一些问题，更改了很多接口的调用方式。

更新了 Record 接口。增加了一些需要实现的内容，例如 id。现在 id 是自动分配的了。

现在可以自动根据记录唤醒指定的 activity 了，只需要添加一下闹钟记录，如 main 文件里 runService() 函数做的那样。

被唤醒的 activity 可以得到唤醒它的 record 信息，就像 TestActivity 所做的一样。直接改这个 Record 信息不会影响后端。

当次唤醒的 record 信息还会保留在后端里面，继续按照 getNextTriggerTime() 函数返回的的时间等待下一次唤醒。因此唤醒一次后 getNextTriggerTime 就应该返回下一次的唤醒时间，建议其使用当前时间进行计算。不希望继续唤醒的话请修改 Record，令 isActive() 返回 false。

如果 getNextTriggerTime() 返回的时间在当前时间之前，对应的 activity 将被直接唤醒。

在修改 Record 的时候应该使用从 AlarmManager 的相关方法删除旧的 Record 并添加新的 Record。其他方式都没有作用。

现在后端返回的 list 是按照 id 而不是时间排序的。

RecordManager、AlarmBroadcastReceiver 类不要调用。

仅经过最小限度的测试，设计可能也不够好，如果有问题请提出，以及时修改。
