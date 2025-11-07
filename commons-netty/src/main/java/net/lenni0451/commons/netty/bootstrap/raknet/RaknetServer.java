package net.lenni0451.commons.netty.bootstrap.raknet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.DatagramChannel;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.commons.netty.bootstrap.types.ReliableServer;
import net.lenni0451.commons.netty.channel.EventLoops;
import net.lenni0451.commons.netty.channel.UDPChannelType;
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;

import java.util.Random;

/**
 * A simple RakNet server implementation.<br>
 * * Requires {@code org.cloudburstmc.netty:netty-transport-raknet} as dependency.
 */
@Getter
@Setter
public class RaknetServer extends ReliableServer {

    public static final int MAX_ORDERING_CHANNELS = 16;
    private static final Random RND = new Random();

    private final UDPChannelType channelType;
    private long sessionTimeout = 30_000;

    /**
     * Create a new RakNet server.
     *
     * @param channelInitializer The channel initializer to use
     */
    public RaknetServer(final ChannelInitializer<Channel> channelInitializer) {
        this(channelInitializer, UDPChannelType.getBest());
    }

    /**
     * Create a new RakNet server.
     *
     * @param channelInitializer The channel initializer to use
     * @param channelType        The channel type to use
     */
    public RaknetServer(final ChannelInitializer<Channel> channelInitializer, final UDPChannelType channelType) {
        super(channelInitializer);
        this.channelType = channelType;
    }

    @Override
    protected void configureBootstrap() {
        this.bootstrap
                .group(EventLoops.udpServerParentEventLoop(this.channelType), EventLoops.udpServerChildEventLoop(this.channelType))
                .channelFactory(RakChannelFactory.server((Class<? extends DatagramChannel>) this.channelType.channelClass()))
                .childOption(ChannelOption.IP_TOS, 0x18)
                .childOption(RakChannelOption.RAK_SESSION_TIMEOUT, this.sessionTimeout)
                .childOption(RakChannelOption.RAK_ORDERING_CHANNELS, MAX_ORDERING_CHANNELS)
                .option(RakChannelOption.RAK_GUID, RND.nextLong())
                .option(RakChannelOption.RAK_MAX_CONNECTIONS, Integer.MAX_VALUE)
                .option(RakChannelOption.RAK_SUPPORTED_PROTOCOLS, new int[]{11})
                .childHandler(this.channelInitializer);
    }

}
