package com.wdl.lclb.rule;

import com.wdl.lclb.channel.IChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author wdl
 * @ClassName WeightRule.java
 * @Description 权重路由规则
 * A(wt=10), B(wt=30), C(wt=40), D(wt=20).
 * [0, 10)   A'wt region = (A's weight)
 * [11, 40)  B'wt region = (A's weight + B's weight)
 * [41, 80)  C'wt region = (A's weight + B's weight + C's weight)
 * [81, 100) D'wt region = (A's weight + B's weight + C's weight + D's weight)
 * randomFactory.nextInt(total weight) => random
 * random=10 => B; random=0 => A; random=40 => C; random=85 => D;
 * @createTime 2022-12-17 11:08
 */
public class WeightRule implements IRule {

    private Logger log = LoggerFactory.getLogger(WeightRule.class);

    /**
     * 随机器
     */
    private Random randomFactory = new Random();

    /**
     * 权重配置<channelId, 权重数值>
     */
    private NavigableMap<Integer, Integer> weightConfig;

    public WeightRule(){}

    public WeightRule(Map<String, Integer> weightConfigMap) {
        initialize(weightConfigMap);
    }

    /**
     * 权重配置初始化
     * @param weightConfig
     */
    public void initialize(Map<String, Integer> weightConfig) {
        NavigableMap<Integer, Integer> config = new TreeMap<>();
        int total = 0;
        for(Map.Entry<String, Integer> entry : weightConfig.entrySet()) {
            config.putIfAbsent(total+=entry.getValue(), Integer.valueOf(entry.getKey()));
        }
        this.weightConfig = config;
    }

    public NavigableMap<Integer, Integer> getWeightConfig() {
        return this.weightConfig;
    }

    /**
     * 根据权重从集合中随机选择通道<br/>
     * 1、选择通道后会判断通道状态
     * 2、熔断通道会被剔除权重配置，其余通道参与下一次分配
     * 当权重配置为空时,返回channelList第一个元素
     * @param channelList
     * @return
     */
    @Override
    public IChannel choose(List<IChannel> channelList) {
        NavigableMap<Integer, Integer> localWeightConfig = new TreeMap(this.weightConfig);
        IChannel iChannel = null;
        while (iChannel == null && !localWeightConfig.isEmpty()) {
            Map.Entry<Integer, Integer> entry = getChannelEntry(localWeightConfig);
            iChannel = channelList.stream() .filter(channel -> entry.getValue().equals(channel.getChannelId()))
                    .findFirst().orElse(null);
            if (iChannel != null && iChannel.allowRequest()) {
                break;
            }
            log.debug("channel：{} interrupt，afresh load", iChannel == null ? null : iChannel.getChannelId());
            iChannel = null;
            localWeightConfig.remove(entry.getKey());
        }
        return Optional.ofNullable(iChannel).orElse(channelList.get(0));
    }

    public Map.Entry<Integer, Integer> getChannelEntry(NavigableMap<Integer, Integer> localWeightConfig) {
        Integer totalWeight = localWeightConfig.keySet().stream().mapToInt(Integer::valueOf).max().getAsInt();
        int random = randomFactory.nextInt(totalWeight);
        Map.Entry<Integer, Integer> entry = localWeightConfig.higherEntry(random);
        log.debug("NavigableMap-elements:{} {}-{}-{}", localWeightConfig, totalWeight, random, entry);
        return entry;
    }
}
