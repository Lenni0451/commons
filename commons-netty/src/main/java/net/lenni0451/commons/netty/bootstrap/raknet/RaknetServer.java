package net.lenni0451.commons.netty.bootstrap.raknet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import net.lenni0451.commons.netty.LazyGroups;
import net.lenni0451.commons.netty.bootstrap.types.AReliableServer;
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;

import java.util.Random;

/**
 * A simple RakNet server implementation.<br>
 * * Requires {@code org.cloudburstmc.netty:netty-transport-raknet} as dependency.
 */
public class RaknetServer extends AReliableServer {

    public static final int MAX_ORDERING_CHANNELS = 16;
    private static final Random RND = new Random();

    private long sessionTimeout = 30_000;

    /**
     * Create a new RakNet server.
     *
     * @param channelInitializer The channel initializer to use
     */
    public RaknetServer(final ChannelInitializer<Channel> channelInitializer) {
        super(channelInitializer);
    }

    /**
     * @return The session timeout in milliseconds
     */
    public long getSessionTimeout() {
        return this.sessionTimeout;
    }

    /**
     * Set the session timeout in milliseconds.
     *
     * @param sessionTimeout The session timeout
     */
    public void setSessionTimeout(final long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    protected void configureBootstrap() {
        if (Epoll.isAvailable()) {
            this.bootstrap
                    .group(LazyGroups.EPOLL_SERVER_PARENT_LOOP_GROUP.get(), LazyGroups.EPOLL_SERVER_CHILD_LOOP_GROUP.get())
                    .channelFactory(RakChannelFactory.server(EpollDatagramChannel.class));
        } else {
            this.bootstrap
                    .group(LazyGroups.NIO_SERVER_PARENT_LOOP_GROUP.get(), LazyGroups.NIO_SERVER_CHILD_LOOP_GROUP.get())
                    .channelFactory(RakChannelFactory.server(NioDatagramChannel.class));
        }

        this.bootstrap
                .childOption(ChannelOption.IP_TOS, 0x18)
                .childOption(RakChannelOption.RAK_SESSION_TIMEOUT, this.sessionTimeout)
                .childOption(RakChannelOption.RAK_ORDERING_CHANNELS, MAX_ORDERING_CHANNELS)
                .option(RakChannelOption.RAK_GUID, RND.nextLong())
                .option(RakChannelOption.RAK_MAX_CONNECTIONS, Integer.MAX_VALUE)
                .option(RakChannelOption.RAK_SUPPORTED_PROTOCOLS, new int[]{11})
                .childHandler(this.channelInitializer);
    }

}
