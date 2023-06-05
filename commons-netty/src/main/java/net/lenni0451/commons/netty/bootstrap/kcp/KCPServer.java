package net.lenni0451.commons.netty.bootstrap.kcp;

import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpServerChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.lenni0451.commons.netty.LazyGroups;
import net.lenni0451.commons.netty.bootstrap.types.AReliableServer;

/**
 * A simple KCP server implementation.<br>
 * * Requires {@code io.jpower.kcp:kcp-netty} as dependency.
 */
public class KCPServer extends AReliableServer {

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
                .group(LazyGroups.NIO_SERVER_PARENT_LOOP_GROUP.get(), LazyGroups.NIO_SERVER_CHILD_LOOP_GROUP.get())
                .channel(UkcpServerChannel.class)

                .childOption(UkcpChannelOption.UKCP_NODELAY, true)
                .childOption(UkcpChannelOption.UKCP_INTERVAL, 20)
                .childOption(UkcpChannelOption.UKCP_FAST_RESEND, 2)
                .childOption(UkcpChannelOption.UKCP_NOCWND, true)
                .childHandler(this.channelInitializer);
    }

}
