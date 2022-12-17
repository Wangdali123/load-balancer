package com.wdl.lclb.rule;


import com.wdl.lclb.channel.IChannel;
import com.wdl.lclb.rule.channel.AChannel;
import com.wdl.lclb.rule.channel.BChannel;
import com.wdl.lclb.rule.channel.CChannel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class RoundRuleTest {

    @org.junit.jupiter.api.Test
    void choose() {
        RoundRule roundRule = new RoundRule();
        List<IChannel> channels = Stream.of(new AChannel(), new BChannel(), new CChannel()).collect(Collectors.toList());
        for(int i=0; i < 20; i++) {
            System.out.println(roundRule.choose(channels).getChannelId());
        }
    }
}