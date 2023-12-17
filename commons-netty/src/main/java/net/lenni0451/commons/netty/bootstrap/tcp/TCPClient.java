package net.lenni0451.commons.netty.bootstrap.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import net.lenni0451.commons.netty.TCPChannelType;
import net.lenni0451.commons.netty.bootstrap.types.AReliableClient;

/**
 * A simple TCP client implementation.
 */
public class TCPClient extends AReliableClient {

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

    /**
     * @return The channel type
     */
    public TCPChannelType getChannelType() {
        return this.channelType;
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
                .group(this.channelType.getClientLoopGroup())
                .channel(this.channelType.getClientChannel())

                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.IP_TOS, 0x18)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.connectTimeout)
                .handler(this.channelInitializer);
    }

}
