# AlarmClockAssignment-Android

现在可以通过后台的接口轻松添加和删除记录了。记录的触发由后台程序自动安排。记录的触发不受关闭程序和重新启动手机的影响。

每个功能需要写的前台内容：

- 该功能需要的记录类，实现 Record 接口；
- 一个提供增删改查功能的界面；
- 一个当记录触发时对其进行处理的界面。

===

AlarmManager 类里有一些用于记录存储的方法。

Record 接口是所有记录项需要实现的接口。

后台接口的具体内容可以参见附带的 javaDoc。其中没有提到的类和方法不要修改和调用。

===

代码直接运行就能够看到一个小小的唤醒示例。其代码主要在 MainActivity.java , TestRecord.java 和 TestActivity.java 中。点击测试按钮可以注册一个 5 秒钟后触发的闹钟记录。

现在可以自动根据记录唤醒指定的 activity 了，只需要添加一下闹钟记录，如 main 文件里 runService() 函数做的那样。

被唤醒的 activity 可以得到唤醒它的 record 信息，就像 TestActivity 所做的一样。

当次唤醒的 record 信息还会保留在后端里面，继续按照 getNextTriggerTime() 函数返回的的时间等待下一次唤醒。因此唤醒一次后 getNextTriggerTime 就应该返回下一次的唤醒时间，建议其使用当前时间进行计算。

只希望唤醒一次的话请在唤醒后手动修改 record，令 isActive() 返回 false。

如果 getNextTriggerTime() 返回的时间在当前时间之前，那么它就不会被触发。

在修改 Record 的时候应该使用 AlarmManager 的相关方法删除旧的 Record 并添加新的 Record。其他方式都没有作用。

现在后端返回的 list 不保证其元素的有序性。

后台接口的具体内容可以参见附带的 javaDoc。其中没有提到的类和方法不要修改和调用。

仅经过最小限度的测试，设计可能也不够好，如果有问题请提出，以及时修改。
