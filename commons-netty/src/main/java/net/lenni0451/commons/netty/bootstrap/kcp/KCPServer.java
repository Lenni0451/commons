package net.lenni0451.commons.netty.bootstrap.kcp;

import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpServerChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.lenni0451.commons.netty.bootstrap.types.ReliableServer;
import net.lenni0451.commons.netty.channel.EventLoops;
import net.lenni0451.commons.netty.channel.TCPChannelType;

/**
 * A simple KCP server implementation.<br>
 * * Requires {@code io.jpower.kcp:kcp-netty} as dependency.
 */
public class KCPServer extends ReliableServer {

    /**
     * Create a new KCP server.
     *
     * @param channelInitializer The channel initializer to use
     */
    public KCPServer(final ChannelInitializer<Channel> channelInitializer) {
        super(channelInitializer);
    }

    @Override
    protected void configureBootstrap() {
        this.bootstrap
                .group(EventLoops.tcpServerParentEventLoop(TCPChannelType.NIO), EventLoops.tcpServerChildEventLoop(TCPChannelType.NIO))
                .channel(UkcpServerChannel.class)
                .childOption(UkcpChannelOption.UKCP_NODELAY, true)
                .childOption(UkcpChannelOption.UKCP_INTERVAL, 20)
                .childOption(UkcpChannelOption.UKCP_FAST_RESEND, 2)
                .childOption(UkcpChannelOption.UKCP_NOCWND, true)
                .childHandler(this.channelInitializer);
    }

}
