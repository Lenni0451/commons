package net.lenni0451.commons.netty.channel;

import io.netty.channel.MultiThreadIoEventLoopGroup;
import net.lenni0451.commons.threading.ThreadFactoryImpl;

import java.util.EnumMap;

public class EventLoops {

    private static final EnumMap<TCPChannelType, MultiThreadIoEventLoopGroup> TCP_CLIENT_EVENT_LOOPS = new EnumMap<>(TCPChannelType.class);
    private static final EnumMap<TCPChannelType, MultiThreadIoEventLoopGroup> TCP_SERVER_PARENT_EVENT_LOOPS = new EnumMap<>(TCPChannelType.class);
    private static final EnumMap<TCPChannelType, MultiThreadIoEventLoopGroup> TCP_SERVER_CHILD_EVENT_LOOPS = new EnumMap<>(TCPChannelType.class);
    private static final EnumMap<UDPChannelType, MultiThreadIoEventLoopGroup> UDP_CLIENT_EVENT_LOOPS = new EnumMap<>(UDPChannelType.class);
    private static final EnumMap<UDPChannelType, MultiThreadIoEventLoopGroup> UDP_SERVER_PARENT_EVENT_LOOPS = new EnumMap<>(UDPChannelType.class);
    private static final EnumMap<UDPChannelType, MultiThreadIoEventLoopGroup> UDP_SERVER_CHILD_EVENT_LOOPS = new EnumMap<>(UDPChannelType.class);

    public static MultiThreadIoEventLoopGroup tcpClientEventLoop(final TCPChannelType channelType) {
        return getOrCreateEventLoop(TCP_CLIENT_EVENT_LOOPS, channelType, "TCPClientEventLoop-" + channelType.name());
    }

    public static MultiThreadIoEventLoopGroup tcpServerParentEventLoop(final TCPChannelType channelType) {
        return getOrCreateEventLoop(TCP_SERVER_PARENT_EVENT_LOOPS, channelType, "TCPServerParentEventLoop-" + channelType.name());
    }

    public static MultiThreadIoEventLoopGroup tcpServerChildEventLoop(final TCPChannelType channelType) {
        return getOrCreateEventLoop(TCP_SERVER_CHILD_EVENT_LOOPS, channelType, "TCPServerChildEventLoop-" + channelType.name());
    }

    public static MultiThreadIoEventLoopGroup udpClientEventLoop(final UDPChannelType channelType) {
        return getOrCreateEventLoop(UDP_CLIENT_EVENT_LOOPS, channelType, "UDPClientEventLoop-" + channelType.name());
    }

    public static MultiThreadIoEventLoopGroup udpServerParentEventLoop(final UDPChannelType channelType) {
        return getOrCreateEventLoop(UDP_SERVER_PARENT_EVENT_LOOPS, channelType, "UDPServerParentEventLoop-" + channelType.name());
    }

    public static MultiThreadIoEventLoopGroup udpServerChildEventLoop(final UDPChannelType channelType) {
        return getOrCreateEventLoop(UDP_SERVER_CHILD_EVENT_LOOPS, channelType, "UDPServerChildEventLoop-" + channelType.name());
    }

    private static synchronized <E extends Enum<E> & ChannelType> MultiThreadIoEventLoopGroup getOrCreateEventLoop(final EnumMap<E, MultiThreadIoEventLoopGroup> map, final E channelType, final String threadFactoryName) {
        return map.computeIfAbsent(channelType, t -> {
            ThreadFactoryImpl threadFactory = ThreadFactoryImpl.of(threadFactoryName + " #").daemon(true);
            return new MultiThreadIoEventLoopGroup(0, threadFactory, channelType.ioHandlerFactorySupplier().get());
        });
    }

}
