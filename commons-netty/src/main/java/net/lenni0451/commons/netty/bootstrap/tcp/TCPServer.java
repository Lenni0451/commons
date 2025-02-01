package net.lenni0451.commons.netty.bootstrap.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import net.lenni0451.commons.netty.TCPChannelType;
import net.lenni0451.commons.netty.bootstrap.types.ReliableServer;

/**
 * A simple TCP server implementation.
 */
public class TCPServer extends ReliableServer {

    private final TCPChannelType channelType;

    /**
     * Create a new TCP server.
     *
     * @param channelInitializer The channel initializer to use
     */
    public TCPServer(final ChannelInitializer<Channel> channelInitializer) {
        this(channelInitializer, TCPChannelType.getBest());
    }

    /**
     * Create a new TCP server.
     *
     * @param channelInitializer The channel initializer to use
     * @param channelType        The channel type to use
     */
    public TCPServer(final ChannelInitializer<Channel> channelInitializer, final TCPChannelType channelType) {
        super(channelInitializer);
        this.channelType = channelType;
    }

    /**
     * @return The channel type
     */
    public TCPChannelType getChannelType() {
        return this.channelType;
    }

    @Override
    protected void configureBootstrap() {
        this.bootstrap
                .group(this.channelType.getServerParentLoopGroup(), this.channelType.getServerChildLoopGroup())
                .channel(this.channelType.getServerChannel())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.IP_TOS, 0x18)
                .childHandler(this.channelInitializer);
    }

}
