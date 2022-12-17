package com.wdl.lclb.rule;

import com.wdl.lclb.channel.IChannel;
import com.wdl.lclb.rule.channel.AChannel;
import com.wdl.lclb.rule.channel.BChannel;
import com.wdl.lclb.rule.channel.CChannel;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WeightRuleTest {

    @Test
    void choose() {
        List<IChannel> channels = Stream.of(new AChannel(), new BChannel(), new CChannel()).collect(Collectors.toList());

        Map<String, Integer> weightConfig = new HashMap<>();
        weightConfig.put("101", 10);
        weightConfig.put("102", 20);
        weightConfig.put("103", 30);
        WeightRule weightRule = new WeightRule(weightConfig);

        Map<Integer, Integer> statistics = new HashMap();
        IntStream.range(0, 30).forEach(i -> {
            IChannel iChannel = weightRule.choose(channels);
            statistics.put(iChannel.getChannelId(), statistics.getOrDefault(iChannel.getChannelId(), 0) + 1);
        });

        System.out.println(statistics);
    }
}