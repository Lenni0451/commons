package net.lenni0451.commons.netty;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.*;
import io.netty.channel.kqueue.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public enum TCPChannelType {

    NIO(NioSocketChannel.class, NioServerSocketChannel.class, LazyGroups.NIO),
    EPOLL(EpollSocketChannel.class, EpollServerSocketChannel.class, LazyGroups.EPOLL),
    KQUEUE(KQueueSocketChannel.class, KQueueServerSocketChannel.class, LazyGroups.KQUEUE),
    UNIX_EPOLL(EpollDomainSocketChannel.class, EpollServerDomainSocketChannel.class, LazyGroups.EPOLL),
    UNIX_KQUEUE(KQueueDomainSocketChannel.class, KQueueServerDomainSocketChannel.class, LazyGroups.KQUEUE);


    public static TCPChannelType getBest() {
        if (Epoll.isAvailable()) return EPOLL;
        if (KQueue.isAvailable()) return KQUEUE;
        return NIO;
    }

    private final Class<? extends Channel> clientChannel;
    private final Class<? extends ServerChannel> serverChannel;
    private final LazyGroups lazyGroups;

    TCPChannelType(final Class<? extends Channel> clientChannel, final Class<? extends ServerChannel> serverChannel, final LazyGroups lazyGroups) {
        this.clientChannel = clientChannel;
        this.serverChannel = serverChannel;
        this.lazyGroups = lazyGroups;
    }

    public Class<? extends Channel> getClientChannel() {
        return this.clientChannel;
    }

    public EventLoopGroup getClientLoopGroup() {
        return this.lazyGroups.getClientLoopGroup().get();
    }

    public Class<? extends ServerChannel> getServerChannel() {
        return this.serverChannel;
    }

    public EventLoopGroup getServerParentLoopGroup() {
        return this.lazyGroups.getServerParentLoopGroup().get();
    }

    public EventLoopGroup getServerChildLoopGroup() {
        return this.lazyGroups.getServerChildLoopGroup().get();
    }

}
