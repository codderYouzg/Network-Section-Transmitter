package edu.youzg.network_transmission.net;

/**
 * 提供了 单例性、多例性 的构造方式，<br/>
 * 并 提供了接收后 计算接收速率 的方法
 */
public class NetSpeed extends NetSendReceiveSpeedAdapter {
    private volatile static NetSpeed me;    // 用作 “单例性构造”
    private volatile static long startReceiveTime;  // 开始接收时间
    private volatile static long lastReceiveTime;   // 接收结束时间

    private volatile static long allReceiveBytes;   // 总共接收的字节数
    private volatile static long curSpeed;  // 当前接收速率
    private volatile static long averSpeed; // 平均接收速率

    /**
     * 获取一个NetSpeed实例
     * (保证了“多例性”)
     */
    public NetSpeed() {
    }

    /**
     * 获取一个NetSpeed实例
     * @return 一个NetSpeed实例(保证了“单例性”)
     */
    public synchronized static NetSpeed newInstance() {
        if (me==null) {
            startReceiveTime = lastReceiveTime
                    = System.currentTimeMillis();
            allReceiveBytes = 0;
            curSpeed = averSpeed = 0;
            me = new NetSpeed();
        }
        return me;
    }

    /**
     * 清空当前属性的值<br/>
     * 便于第二次使用不受上一次使用的影响
     */
    public static void clear() {
        me = null;
        allReceiveBytes = 0;
        curSpeed = averSpeed = 0;
    }

    /**
     * 计算 瞬时/平均速率，并更新各种时间
     * @param receiveBytes 接收到的字节数
     */
    @Override
    public void afterReceive(int receiveBytes) {
        long curTime = System.currentTimeMillis();
        long deltaTime = curTime - lastReceiveTime;
        long allTime = curTime - startReceiveTime;

        curSpeed = (long) ((double)receiveBytes*1000.0 / deltaTime);    // 计算当前瞬时速率
        allReceiveBytes += receiveBytes;    // 计算当前总收到字节数
        averSpeed = (long) ((double) allReceiveBytes * 1000.0 / allTime); // 计算平均速率

        lastReceiveTime = curTime;
    }

    public static long getCurSpeed() {
        return curSpeed;
    }

    public static long getAverSpeed() {
        return averSpeed;
    }

}