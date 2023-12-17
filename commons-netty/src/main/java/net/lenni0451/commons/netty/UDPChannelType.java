package net.lenni0451.commons.netty;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollDomainDatagramChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueDomainDatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;

public enum UDPChannelType {

    NIO(NioDatagramChannel.class, LazyGroups.NIO),
    EPOLL(EpollDatagramChannel.class, LazyGroups.EPOLL),
    KQUEUE(KQueueDatagramChannel.class, LazyGroups.KQUEUE),
    UNIX_EPOLL(EpollDomainDatagramChannel.class, LazyGroups.EPOLL),
    UNIX_KQUEUE(KQueueDomainDatagramChannel.class, LazyGroups.KQUEUE);


    public static UDPChannelType getBest() {
        if (Epoll.isAvailable()) return EPOLL;
        if (KQueue.isAvailable()) return KQUEUE;
        return NIO;
    }

    private final Class<? extends Channel> channelClass;
    private final LazyGroups lazyGroups;

    UDPChannelType(final Class<? extends Channel> channelClass, final LazyGroups lazyGroups) {
        this.channelClass = channelClass;
        this.lazyGroups = lazyGroups;
    }

    public Class<? extends Channel> getChannelClass() {
        return this.channelClass;
    }

    public EventLoopGroup getClientLoopGroup() {
        return this.lazyGroups.getClientLoopGroup().get();
    }

    public EventLoopGroup getServerParentLoopGroup() {
        return this.lazyGroups.getServerParentLoopGroup().get();
    }

    public EventLoopGroup getServerChildLoopGroup() {
        return this.lazyGroups.getServerChildLoopGroup().get();
    }

}
