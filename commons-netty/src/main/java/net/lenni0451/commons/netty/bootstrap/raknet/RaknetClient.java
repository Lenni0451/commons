package net.lenni0451.commons.netty.bootstrap.raknet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import net.lenni0451.commons.netty.LazyGroups;
import net.lenni0451.commons.netty.bootstrap.types.AReliableClient;
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;

import java.util.Random;

/**
 * A simple RakNet client implementation.<br>
 * Requires {@code org.cloudburstmc.netty:netty-transport-raknet} as dependency.
 */
public class RaknetClient extends AReliableClient {

    public static final int MAX_ORDERING_CHANNELS = 16;
    private static final Random RND = new Random();

    private long connectTimeout = 5_000;
    private long sessionTimeout = 30_000;

    /**
     * Create a new RakNet client.
     *
     * @param channelInitializer The channel initializer to use
     */
    public RaknetClient(final ChannelInitializer<Channel> channelInitializer) {
        super(channelInitializer);
    }

    /**
     * @return The connect timeout in milliseconds
     */
    public long getConnectTimeout() {
        return this.connectTimeout;
    }

    /**
     * Set the connect timeout in milliseconds.
     *
     * @param connectTimeout The connect timeout
     */
    public void setConnectTimeout(final long connectTimeout) {
        this.connectTimeout = connectTimeout;
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
                    .group(LazyGroups.EPOLL_CLIENT_LOOP_GROUP.get())
                    .channelFactory(RakChannelFactory.client(EpollDatagramChannel.class));
        } else {
            this.bootstrap
                    .group(LazyGroups.NIO_CLIENT_LOOP_GROUP.get())
                    .channelFactory(RakChannelFactory.client(NioDatagramChannel.class));
        }

        this.bootstrap
                .option(ChannelOption.IP_TOS, 0x18)
                .option(RakChannelOption.RAK_CONNECT_TIMEOUT, this.connectTimeout)
                .option(RakChannelOption.RAK_SESSION_TIMEOUT, this.sessionTimeout)
                .option(RakChannelOption.RAK_GUID, RND.nextLong())
                .option(RakChannelOption.RAK_PROTOCOL_VERSION, 11)
                .option(RakChannelOption.RAK_ORDERING_CHANNELS, MAX_ORDERING_CHANNELS)
                .handler(this.channelInitializer);
    }

}
