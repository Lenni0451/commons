package net.lenni0451.commons.netty.bootstrap.kcp;

import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpClientChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import net.lenni0451.commons.netty.LazyGroups;
import net.lenni0451.commons.netty.bootstrap.types.ReliableClient;

/**
 * A simple KCP client implementation.<br>
 * Requires {@code io.jpower.kcp:kcp-netty} as dependency.
 */
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

    /**
     * @return The connect timeout in milliseconds
     */
    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    /**
     * Set the connect timeout in milliseconds.
     *
     * @param connectTimeout The connect timeout
     */
    public void setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @Override
    protected void configureBootstrap() {
        this.bootstrap
                .group(LazyGroups.NIO_CLIENT_LOOP_GROUP.get())
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
