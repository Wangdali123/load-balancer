package com.wdl.lclb.rule;

import com.wdl.lclb.channel.IChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wdl
 * @ClassName RoundRule.java
 * @Description 轮询路由规则
 * list = [A, B, C, D]
 * choose() => count.incr
 * index = count % list.size
 * channel = list.get(index)
 * @createTime 2022-05-31 22:51
 */
public class RoundRule implements IRule{

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 计数器，用来计算索引
     */
    private final AtomicInteger nextServerCyclicCounter = new AtomicInteger(0);

    @Override
    public IChannel choose(List<IChannel> channelList) {
        int count = 0;
        IChannel iChannel = null;
        while (iChannel == null && count++ < 10) {
            iChannel = channelList.get(getNextIndex(channelList.size()));
            if (iChannel != null && iChannel.allowRequest()) {
                break;
            }
            log.debug("channel：{} interrupt，afresh load", iChannel == null ? null : iChannel.getChannelId());
            iChannel = null;
        }
        return Optional.ofNullable(iChannel).orElse(channelList.get(0));
    }

    /**
     * 计算下个通道的索引值(绝对值，防止计数溢出)
     * @param modulo
     * @return
     */
    private int getNextIndex(int modulo) {
        return Math.abs((nextServerCyclicCounter.getAndIncrement() % modulo));
    }

}
