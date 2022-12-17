package com.wdl.lclb.balancer;

import com.wdl.lclb.channel.IChannel;
import com.wdl.lclb.rule.IRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author wdl
 * @ClassName LoadBalancerClient.java
 * @Description 负载均衡客户端
 * @createTime 2022-12-17 16:47
 */
public class ChannelLoadBalancerClient implements IChannelLoadBalancer {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 通道集合
     */
    private final List<IChannel> channelList = new ArrayList<>();
    /**
     * 默认路由规则
     */
    private IRule defaultRule;

    public ChannelLoadBalancerClient(IChannel... channels) {
        Collections.addAll(channelList, channels);
    }

    public ChannelLoadBalancerClient(IRule rule, IChannel... channels) {
        this(channels);
        this.defaultRule = rule;
    }

    @Override
    public List<IChannel> getAllChannel() {
        return new ArrayList<>(channelList);
    }

    @Override
    public IChannel chooseChannel(IRule rule) {
        return rule.choose(getAllChannel());
    }

    @Override
    public <T extends IChannel, R> R execute(Function<T, R> function, IRule rule) {
        IChannel channel = this.chooseChannel(rule);
        log.debug("channel:{}", channel.getChannelId());
        return function.apply((T) channel);
    }

    @Override
    public <T extends IChannel, R> R execute(Function<T, R> function) {
        return execute(function, defaultRule);
    }

}
