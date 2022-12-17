package com.wdl.lclb.rule;

import com.wdl.lclb.channel.IChannel;

import java.util.List;

/**
 * @author wdl
 * @ClassName IRule.java
 * @Description 路由规则
 * @createTime 2022-12-17 16:43
 */
public interface IRule {

    /**
     * 从channelList中选择一个通道
     * @param channelList
     * @return
     */
    IChannel choose(List<IChannel> channelList);

}
