package com.wdl.lclb.channel;

/**
 * @author wdl
 * @ClassName IChannel.java
 * @Description 通道接口
 * @createTime 2022-12-17 11:30
 */
public interface IChannel {

    /**
     * 获取通道唯一标识
     * @return
     */
    Integer getChannelId();

    /**
     * 是否开启断路
     * @return
     */
    boolean isOpen();

    /**
     * 是否允许此次请求通过
     * @return
     */
    boolean allowRequest();

    /**
     * 标记成功
     */
    void markSuccess();

    /**
     * 标记失败（直接关闭）
     */
    void markNonSuccess();

    /**
     * 标记异常(失败)
     * @return
     */
    boolean attemptExecution();

}
