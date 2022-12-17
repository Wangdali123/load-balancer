package com.wdl.lclb.channel;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author wdl
 * @ClassName AbstractChannelImpl.java
 * @Description 通道实现抽象类
 * @createTime 2022-12-17 11:32
 */
public abstract class AbstractChannelImpl implements IChannel {

    /**
     * 熔断时间点
     */
    private final AtomicLong circuitClosed = new AtomicLong(-1);
    /**
     * 失败次数
     */
    private final AtomicInteger failCounter = new AtomicInteger(0);
    /**
     * 通道状态
     */
    private final AtomicReference<Status> status = new AtomicReference<>(Status.OPEN);

    /**
     * 熔断时长
     */
    protected Long circuitTime = 10L * 60 * 1000;
    /**
     * 触发熔断的失败次数上限
     */
    protected Integer circuitCount = 10;

    enum Status {
        CLOSED, OPEN;
    }

    /**
     * 通道初始化
     * @param circuitTime 熔断时长,必传
     * @param circuitCount 熔断连续失败次数,必传
     * @param circuitClosed 熔断时间点
     * @param failCounter 连续失败次数
     */
    public void initialize(long circuitTime, int circuitCount, LocalDateTime circuitClosed, Integer failCounter) {
        this.circuitTime = circuitTime;
        this.circuitCount = circuitCount;
        if (circuitClosed != null) {
            this.circuitClosed.set(circuitClosed.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        }
        if (failCounter != null) {
            this.failCounter.set(failCounter);
        }
    }

    public abstract void initialize(LocalDateTime circuitClosed, Integer failCounter);

    /**
     * 获取当前通道id
     * @return
     */
    @Override
    public abstract Integer getChannelId();

    @Override
    public boolean isOpen() {
        return status.get().equals(Status.OPEN);
    }

    @Override
    public boolean allowRequest() {
        return isOpen() || isAfterSleepWindow();
    }

    @Override
    public void markSuccess() {
        status.set(Status.OPEN);
        failCounter.set(0);
        circuitClosed.set(-1L);
    }

    @Override
    public void markNonSuccess() {
        status.set(Status.CLOSED);
        circuitClosed.set(System.currentTimeMillis());
    }

    @Override
    public boolean attemptExecution() {
        Integer failCount = failCounter.incrementAndGet();
        if (failCount > circuitCount) {
            this.markNonSuccess();
        }
        return true;
    }

    /**
     * 是否到熔断时间后
     * @return
     */
    private boolean isAfterSleepWindow() {
        return System.currentTimeMillis() > circuitClosed.get() + circuitTime;
    }
}
