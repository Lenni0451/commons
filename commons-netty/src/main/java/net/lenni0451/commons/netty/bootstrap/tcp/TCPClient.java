package net.lenni0451.commons.netty.bootstrap.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.commons.netty.bootstrap.types.ReliableClient;
import net.lenni0451.commons.netty.channel.EventLoops;
import net.lenni0451.commons.netty.channel.TCPChannelType;

/**
 * A simple TCP client implementation.
 */
@Getter
@Setter
public class TCPClient extends ReliableClient {

    private final TCPChannelType channelType;
    private int connectTimeout = 5_000;

    /**
     * Create a new TCP client.
     *
     * @param channelInitializer The channel initializer to use
     */
    public TCPClient(final ChannelInitializer<Channel> channelInitializer) {
        this(channelInitializer, TCPChannelType.getBest());
    }

    /**
     * Create a new TCP client.
     *
     * @param channelInitializer The channel initializer to use
     * @param channelType        The channel type to use
     */
    public TCPClient(final ChannelInitializer<Channel> channelInitializer, final TCPChannelType channelType) {
        super(channelInitializer);
        this.channelType = channelType;
    }

    @Override
    protected void configureBootstrap() {
        this.bootstrap
                .group(EventLoops.tcpClientEventLoop(this.channelType))
                .channel(this.channelType.clientChannelClass())
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.IP_TOS, 0x18)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.connectTimeout)
                .handler(this.channelInitializer);
    }

}
