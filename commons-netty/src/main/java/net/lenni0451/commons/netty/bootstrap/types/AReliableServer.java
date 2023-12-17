package net.lenni0451.commons.netty.bootstrap.types;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * An abstract server implementation for reliable protocols.
 */
public abstract class AReliableServer {

    protected final ChannelInitializer<Channel> channelInitializer;
    protected final ServerBootstrap bootstrap;

    protected ChannelFuture channelFuture;

    public AReliableServer(final ChannelInitializer<Channel> channelInitializer) {
        this.channelInitializer = channelInitializer;
        this.bootstrap = new ServerBootstrap();
    }

    /**
     * Configure the bootstrap channel, event group and options.
     */
    protected abstract void configureBootstrap();

    /**
     * Bind the server to the given host and port.
     *
     * @param bindAddress The address to bind to
     * @param bindPort    The port to bind to
     * @param sync        If the method should wait for the bind to complete
     */
    public void bind(final String bindAddress, final int bindPort, final boolean sync) {
        this.bind(new InetSocketAddress(bindAddress, bindPort), sync);
    }

    /**
     * Bind the server to the given socket address.
     *
     * @param bindAddress The address to bind to
     * @param sync        If the method should wait for the bind to complete
     */
    public void bind(final SocketAddress bindAddress, final boolean sync) {
        this.configureBootstrap();
        this.channelFuture = this.bootstrap.bind(bindAddress);
        if (sync) this.channelFuture.syncUninterruptibly();
    }

    /**
     * @return The bootstrap used to create the server
     */
    public ServerBootstrap getBootstrap() {
        return this.bootstrap;
    }

    /**
     * @return Get the bound channel future
     */
    public ChannelFuture getChannelFuture() {
        return this.channelFuture;
    }

    /**
     * Close the bound channel future.
     */
    public void close() {
        this.channelFuture.channel().close();
    }

}
