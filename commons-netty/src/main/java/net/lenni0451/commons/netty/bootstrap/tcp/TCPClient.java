package net.lenni0451.commons.netty.bootstrap.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.lenni0451.commons.netty.LazyGroups;
import net.lenni0451.commons.netty.bootstrap.types.AReliableClient;

/**
 * A simple TCP client implementation.
 */
public class TCPClient extends AReliableClient {

    private int connectTimeout = 5_000;

    /**
     * Create a new TCP client.
     *
     * @param channelInitializer The channel initializer to use
     */
    public TCPClient(final ChannelInitializer<Channel> channelInitializer) {
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
        if (Epoll.isAvailable()) {
            this.bootstrap
                    .group(LazyGroups.EPOLL_CLIENT_LOOP_GROUP.get())
                    .channel(EpollSocketChannel.class);
        } else {
            this.bootstrap
                    .group(LazyGroups.NIO_CLIENT_LOOP_GROUP.get())
                    .channel(NioSocketChannel.class);
        }

        this.bootstrap
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.IP_TOS, 0x18)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.connectTimeout)
                .handler(this.channelInitializer);
    }

}
