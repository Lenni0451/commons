package net.lenni0451.commons.netty.bootstrap.kcp;

import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpClientChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.commons.netty.bootstrap.types.ReliableClient;
import net.lenni0451.commons.netty.channel.EventLoops;
import net.lenni0451.commons.netty.channel.TCPChannelType;

/**
 * A simple KCP client implementation.<br>
 * Requires {@code io.jpower.kcp:kcp-netty} as dependency.
 */
@Getter
@Setter
public class KCPClient extends ReliableClient {

    private int connectTimeout = 5_000;

    /**
     * Create a new KCP client.
     *
     * @param channelInitializer The channel initializer to use
     */
    public KCPClient(final ChannelInitializer<Channel> channelInitializer) {
        super(channelInitializer);
    }

    @Override
    protected void configureBootstrap() {
        this.bootstrap
                .group(EventLoops.tcpClientEventLoop(TCPChannelType.NIO))
                .channel(UkcpClientChannel.class)
                .option(UkcpChannelOption.UKCP_NODELAY, true)
                .option(UkcpChannelOption.UKCP_INTERVAL, 20)
                .option(UkcpChannelOption.UKCP_FAST_RESEND, 2)
                .option(UkcpChannelOption.UKCP_NOCWND, true)
                .option(ChannelOption.IP_TOS, 0x18)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.connectTimeout)
                .handler(this.channelInitializer);
    }

}
