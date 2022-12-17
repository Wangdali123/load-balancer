package com.wdl.lclb.balancer;

import com.wdl.lclb.channel.IChannel;
import com.wdl.lclb.rule.IRule;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author wdl
 * @ClassName ILoadBalancer.java
 * @Description 负载均衡器接口
 * @createTime 2022-12-17 14:12
 */
public interface IChannelLoadBalancer {

    /**
     * 获取全部通道
     * @return
     */
    List<IChannel> getAllChannel();

    /**
     * 选择一个通道
     * @return
     */
    IChannel chooseChannel(IRule rule);

    /**
     * 根据路由规则选择通道执行对应业务方法(function)
     * @param function
     * @param rule
     * @param <T>
     * @param <R>
     * @return
     */
    <T extends IChannel, R> R execute(Function<T, R> function, IRule rule);

    /**
     * 使用默认路由规则选择通道执行对应业务方法(function)
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    <T extends IChannel, R> R execute(Function<T, R> function);

}
