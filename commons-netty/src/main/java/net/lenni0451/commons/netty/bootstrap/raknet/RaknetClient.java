package net.lenni0451.commons.netty.bootstrap.raknet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.DatagramChannel;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.commons.netty.bootstrap.types.ReliableClient;
import net.lenni0451.commons.netty.channel.EventLoops;
import net.lenni0451.commons.netty.channel.UDPChannelType;
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;

import java.util.Random;

/**
 * A simple RakNet client implementation.<br>
 * Requires {@code org.cloudburstmc.netty:netty-transport-raknet} as dependency.
 */
@Getter
@Setter
public class RaknetClient extends ReliableClient {

    public static final int MAX_ORDERING_CHANNELS = 16;
    private static final Random RND = new Random();

    private final UDPChannelType channelType;
    private long connectTimeout = 5_000;
    private long sessionTimeout = 30_000;

    /**
     * Create a new RakNet client.
     *
     * @param channelInitializer The channel initializer to use
     */
    public RaknetClient(final ChannelInitializer<Channel> channelInitializer) {
        this(channelInitializer, UDPChannelType.getBest());
    }

    /**
     * Create a new RakNet client.
     *
     * @param channelInitializer The channel initializer to use
     * @param channelType        The channel type to use
     */
    public RaknetClient(final ChannelInitializer<Channel> channelInitializer, final UDPChannelType channelType) {
        super(channelInitializer);
        this.channelType = channelType;
    }

    @Override
    protected void configureBootstrap() {
        this.bootstrap
                .group(EventLoops.udpClientEventLoop(this.channelType))
                .channelFactory(RakChannelFactory.client((Class<? extends DatagramChannel>) this.channelType.channelClass()))
                .option(ChannelOption.IP_TOS, 0x18)
                .option(RakChannelOption.RAK_CONNECT_TIMEOUT, this.connectTimeout)
                .option(RakChannelOption.RAK_SESSION_TIMEOUT, this.sessionTimeout)
                .option(RakChannelOption.RAK_GUID, RND.nextLong())
                .option(RakChannelOption.RAK_PROTOCOL_VERSION, 11)
                .option(RakChannelOption.RAK_ORDERING_CHANNELS, MAX_ORDERING_CHANNELS)
                .handler(this.channelInitializer);
    }

}
