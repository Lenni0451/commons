package net.lenni0451.commons.netty.bootstrap.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;
import io.netty.util.AttributeKey;
import lombok.Getter;
import net.lenni0451.commons.netty.channel.EventLoops;
import net.lenni0451.commons.netty.channel.UDPChannelType;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * A simple UDP client/server implementation.
 */
public class UDPClientServer {

    public static final AttributeKey<UDPClientServer> ATTRIBUTE_CLIENT_SERVER = AttributeKey.valueOf("commons-netty UDPClientServer");

    protected final ChannelInitializer<DatagramChannel> channelInitializer;
    @Getter
    protected final UDPChannelType channelType;
    @Getter
    protected final Bootstrap bootstrap;
    @Getter
    protected ChannelFuture channelFuture;

    /**
     * Create a new UDP client/server.
     *
     * @param channelInitializer The channel initializer to use
     */
    public UDPClientServer(final ChannelInitializer<DatagramChannel> channelInitializer) {
        this(channelInitializer, UDPChannelType.getBest());
    }

    /**
     * Create a new UDP client/server.
     *
     * @param channelInitializer The channel initializer to use
     * @param channelType        The channel type to use
     */
    public UDPClientServer(final ChannelInitializer<DatagramChannel> channelInitializer, final UDPChannelType channelType) {
        this.channelInitializer = channelInitializer;
        this.channelType = channelType;
        this.bootstrap = new Bootstrap();
    }

    /**
     * Configure the bootstrap channel, event group and options.
     */
    protected void configureBootstrap() {
        this.bootstrap
                .group(EventLoops.udpClientEventLoop(this.channelType))
                .channel(this.channelType.channelClass())
                .attr(ATTRIBUTE_CLIENT_SERVER, this)
                .handler(this.channelInitializer);
    }

    /**
     * Bind the server to the given host and port.
     *
     * @param host The host to bind to
     * @param port The port to bind to
     * @param sync If the method should wait for the bind to complete
     */
    public void bind(final String host, final int port, final boolean sync) {
        this.configureBootstrap();
        this.channelFuture = this.bootstrap.localAddress(host, port).bind();
        if (sync) this.channelFuture.syncUninterruptibly();
    }

    /**
     * Connect the client to the given host and port.
     *
     * @param host The host to connect to
     * @param port The port to connect to
     * @param sync If the method should wait for the connect to complete
     */
    public void connect(final String host, final int port, final boolean sync) {
        this.connect(new InetSocketAddress(host, port), sync);
    }

    /**
     * Connect the client to the given address.
     *
     * @param address The address to connect to
     * @param sync    If the method should wait for the connect to complete
     */
    public void connect(final SocketAddress address, final boolean sync) {
        this.configureBootstrap();
        this.channelFuture = this.bootstrap.connect(address);
        if (sync) this.channelFuture.syncUninterruptibly();
    }

    /**
     * Close the connected channel future.
     */
    public void close() {
        this.channelFuture.channel().close();
    }

}
