package net.lenni0451.commons.netty.bootstrap.types;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * An abstract client implementation for reliable protocols.
 */
public abstract class ReliableClient {

    protected final ChannelInitializer<Channel> channelInitializer;
    @Getter
    protected final Bootstrap bootstrap;
    @Getter
    protected ChannelFuture channelFuture;

    public ReliableClient(final ChannelInitializer<Channel> channelInitializer) {
        this.channelInitializer = channelInitializer;
        this.bootstrap = new Bootstrap();
    }

    /**
     * Configure the bootstrap channel, event group and options.
     */
    protected abstract void configureBootstrap();

    /**
     * Connect to the given host and port.
     *
     * @param ip   The ip to connect to
     * @param port The port to connect to
     * @param sync If the method should wait for the connect to complete
     */
    public void connect(final String ip, final int port, final boolean sync) {
        this.connect(new InetSocketAddress(ip, port), sync);
    }

    /**
     * Connect to the given address.
     *
     * @param address The address to connect to
     * @param sync    If the method should wait for the connect to complete
     */
    public void connect(final SocketAddress address, final boolean sync) {
        this.configureBootstrap();
        this.channelFuture = this.bootstrap.register().channel().connect(address);
        if (sync) this.channelFuture.syncUninterruptibly();
    }

    /**
     * Close the connected channel future.
     */
    public void close() {
        this.channelFuture.channel().close();
    }

}
