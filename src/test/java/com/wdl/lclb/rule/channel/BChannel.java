package com.wdl.lclb.rule.channel;

import com.wdl.lclb.channel.AbstractChannelImpl;

import java.time.LocalDateTime;

/**
 * <p>Title: BChannel</p>
 * <p>Description: ClassDescription</p>
 * <p>Copyright: Copyright (c) 2021</p>
 *
 * @author wangdali
 * @version 1.0
 * @date 2022/12/17 15:29
 */
public class BChannel extends AbstractChannelImpl {
    @Override
    public void initialize(LocalDateTime circuitClosed, Integer failCounter) {

    }

    @Override
    public Integer getChannelId() {
        return 102;
    }
}
