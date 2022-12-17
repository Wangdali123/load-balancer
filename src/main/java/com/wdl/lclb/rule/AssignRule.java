package com.wdl.lclb.rule;

import com.wdl.lclb.channel.IChannel;

import java.util.List;
import java.util.Objects;

/**
 * @author wdl
 * @ClassName AssignRule.java
 * @Description 指定通道分配规则
 * @createTime 2022-12-17 16:47
 */
public class AssignRule implements IRule{

    private Integer channelId;

    private AssignRule(Integer channelId) {
        this.channelId = channelId;
    }

    public static AssignRule build(Integer channelId) {
        return new AssignRule(channelId);
    }

    @Override
    public IChannel choose(List<IChannel> channelList) {
        return channelList.stream().filter(iChannel -> Objects.equals(channelId, iChannel.getChannelId())).findFirst()
                .orElseThrow(() -> new RuntimeException("Channel Not Found"));
    }
}
