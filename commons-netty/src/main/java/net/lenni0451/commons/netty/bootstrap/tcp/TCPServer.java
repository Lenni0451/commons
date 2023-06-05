package net.lenni0451.commons.netty.bootstrap.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.lenni0451.commons.netty.LazyGroups;
import net.lenni0451.commons.netty.bootstrap.types.AReliableServer;

/**
 * A simple TCP server implementation.
 */
public class TCPServer extends AReliableServer {

    /**
     * Create a new TCP server.
     *
     * @param channelInitializer The channel initializer to use
     */
    public TCPServer(final ChannelInitializer<Channel> channelInitializer) {
        super(channelInitializer);
    }

    @Override
    protected void configureBootstrap() {
        if (Epoll.isAvailable()) {
            this.bootstrap
                    .group(LazyGroups.EPOLL_SERVER_PARENT_LOOP_GROUP.get(), LazyGroups.EPOLL_SERVER_CHILD_LOOP_GROUP.get())
                    .channel(EpollServerSocketChannel.class);
        } else {
            this.bootstrap
                    .group(LazyGroups.NIO_SERVER_PARENT_LOOP_GROUP.get(), LazyGroups.NIO_SERVER_CHILD_LOOP_GROUP.get())
                    .channel(NioServerSocketChannel.class);
        }

        this.bootstrap
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.IP_TOS, 0x18)
                .childHandler(this.channelInitializer);
    }

}
