package net.lenni0451.commons.netty.bootstrap.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import lombok.Getter;
import net.lenni0451.commons.netty.bootstrap.types.ReliableServer;
import net.lenni0451.commons.netty.channel.EventLoops;
import net.lenni0451.commons.netty.channel.TCPChannelType;

/**
 * A simple TCP server implementation.
 */
@Getter
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

    @Override
    protected void configureBootstrap() {
        this.bootstrap
                .group(EventLoops.tcpServerParentEventLoop(this.channelType), EventLoops.tcpServerChildEventLoop(this.channelType))
                .channel(this.channelType.serverChannelClass())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.IP_TOS, 0x18)
                .childHandler(this.channelInitializer);
    }

}
