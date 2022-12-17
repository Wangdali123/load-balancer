package com.wdl.lclb.rule.channel;

import com.wdl.lclb.channel.AbstractChannelImpl;

import java.time.LocalDateTime;

/**
 * <p>Title: AChannel</p>
 * <p>Description: ClassDescription</p>
 * <p>Copyright: Copyright (c) 2021</p>
 *
 * @author wangdali
 * @version 1.0
 * @date 2022/12/17 15:29
 */
public class AChannel extends AbstractChannelImpl {

    public void initialize(LocalDateTime circuitClosed, Integer failCounter) {

    }

    public Integer getChannelId() {
        return 101;
    }
}
