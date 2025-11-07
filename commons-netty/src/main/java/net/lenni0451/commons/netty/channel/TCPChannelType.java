package net.lenni0451.commons.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.*;
import io.netty.channel.kqueue.*;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalIoHandler;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.uring.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.net.SocketAddress;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum TCPChannelType implements ChannelType {

    NIO(NioSocketChannel.class, NioServerSocketChannel.class, NioIoHandler::newFactory, true),
    EPOLL(EpollSocketChannel.class, EpollServerSocketChannel.class, EpollIoHandler::newFactory, Epoll.isAvailable()),
    EPOLL_DOMAIN_SOCKET(EpollDomainSocketChannel.class, EpollServerDomainSocketChannel.class, EpollIoHandler::newFactory, Epoll.isAvailable()),
    KQUEUE(KQueueSocketChannel.class, KQueueServerSocketChannel.class, KQueueIoHandler::newFactory, KQueue.isAvailable()),
    KQUEUE_DOMAIN_SOCKET(KQueueDomainSocketChannel.class, KQueueServerDomainSocketChannel.class, KQueueIoHandler::newFactory, KQueue.isAvailable()),
    IO_URING(IoUringSocketChannel.class, IoUringServerSocketChannel.class, IoUringIoHandler::newFactory, IoUring.isAvailable()),
    IO_URING_DOMAIN_SOCKET(IoUringDomainSocketChannel.class, IoUringServerDomainSocketChannel.class, IoUringIoHandler::newFactory, IoUring.isAvailable()),
    LOCAL(LocalChannel.class, LocalServerChannel.class, LocalIoHandler::newFactory, true);

    private static final TCPChannelType[] BEST = {EPOLL, KQUEUE, IO_URING, NIO};
    private static final TCPChannelType[] BEST_DOMAIN_SOCKET = {EPOLL_DOMAIN_SOCKET, KQUEUE_DOMAIN_SOCKET, IO_URING_DOMAIN_SOCKET};

    public static TCPChannelType getBest() {
        return getBest(false);
    }

    public static TCPChannelType getBest(final SocketAddress address) {
        return getBest(address instanceof DomainSocketAddress);
    }

    public static TCPChannelType getBest(final boolean domainSocket) {
        return getBest(domainSocket ? BEST_DOMAIN_SOCKET : BEST);
    }

    public static TCPChannelType getBest(final TCPChannelType... channelTypes) {
        for (TCPChannelType channelType : channelTypes) {
            if (channelType.available()) return channelType;
        }
        throw new IllegalStateException("No available channel type found");
    }

    private final Class<? extends Channel> clientChannelClass;
    private final Class<? extends ServerChannel> serverChannelClass;
    private final Supplier<? extends IoHandlerFactory> ioHandlerFactorySupplier;
    private final boolean available;

}
