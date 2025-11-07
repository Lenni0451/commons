package net.lenni0451.commons.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollDomainDatagramChannel;
import io.netty.channel.epoll.EpollIoHandler;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueDomainDatagramChannel;
import io.netty.channel.kqueue.KQueueDomainSocketChannel;
import io.netty.channel.kqueue.KQueueIoHandler;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalIoHandler;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.uring.IoUring;
import io.netty.channel.uring.IoUringDatagramChannel;
import io.netty.channel.uring.IoUringIoHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.net.SocketAddress;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum UDPChannelType implements ChannelType {

    NIO(NioDatagramChannel.class, NioIoHandler::newFactory, true),
    EPOLL(EpollDatagramChannel.class, EpollIoHandler::newFactory, Epoll.isAvailable()),
    EPOLL_DOMAIN_SOCKET(EpollDomainDatagramChannel.class, EpollIoHandler::newFactory, Epoll.isAvailable()),
    KQUEUE(KQueueDomainSocketChannel.class, KQueueIoHandler::newFactory, KQueue.isAvailable()),
    KQUEUE_DOMAIN_SOCKET(KQueueDomainDatagramChannel.class, KQueueIoHandler::newFactory, KQueue.isAvailable()),
    IO_URING(IoUringDatagramChannel.class, IoUringIoHandler::newFactory, IoUring.isAvailable()),
    LOCAL(LocalChannel.class, LocalIoHandler::newFactory, true);

    private static final UDPChannelType[] BEST = {EPOLL, KQUEUE, IO_URING, NIO};
    private static final UDPChannelType[] BEST_DOMAIN_SOCKET = {EPOLL_DOMAIN_SOCKET, KQUEUE_DOMAIN_SOCKET};

    public static UDPChannelType getBest() {
        return getBest(false);
    }

    public static UDPChannelType getBest(final SocketAddress address) {
        return getBest(address instanceof DomainSocketAddress);
    }

    public static UDPChannelType getBest(final boolean domainSocket) {
        return getBest(domainSocket ? BEST_DOMAIN_SOCKET : BEST);
    }

    public static UDPChannelType getBest(final UDPChannelType... channelTypes) {
        for (UDPChannelType channelType : channelTypes) {
            if (channelType.available()) return channelType;
        }
        throw new IllegalStateException("No available channel type found");
    }

    private final Class<? extends Channel> channelClass;
    private final Supplier<? extends IoHandlerFactory> ioHandlerFactorySupplier;
    private final boolean available;

}
