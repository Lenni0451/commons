package net.lenni0451.commons.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import net.lenni0451.commons.Lazy;
import net.lenni0451.commons.threading.ThreadFactoryImpl;

public class LazyGroups {

    public static final Lazy<NioEventLoopGroup> NIO_CLIENT_LOOP_GROUP = Lazy.of(() -> new NioEventLoopGroup(0, ThreadFactoryImpl.of("NioClientWorker #").daemon(true)));
    public static final Lazy<NioEventLoopGroup> NIO_SERVER_PARENT_LOOP_GROUP = Lazy.of(() -> new NioEventLoopGroup(0, ThreadFactoryImpl.of("NioServerParent").daemon(true)));
    public static final Lazy<NioEventLoopGroup> NIO_SERVER_CHILD_LOOP_GROUP = Lazy.of(() -> new NioEventLoopGroup(0, ThreadFactoryImpl.of("NioServerChild").daemon(true)));

    public static final Lazy<EpollEventLoopGroup> EPOLL_CLIENT_LOOP_GROUP = Lazy.of(() -> new EpollEventLoopGroup(0, ThreadFactoryImpl.of("EpollClientWorker").daemon(true)));
    public static final Lazy<EpollEventLoopGroup> EPOLL_SERVER_PARENT_LOOP_GROUP = Lazy.of(() -> new EpollEventLoopGroup(0, ThreadFactoryImpl.of("EpollServerParent").daemon(true)));
    public static final Lazy<EpollEventLoopGroup> EPOLL_SERVER_CHILD_LOOP_GROUP = Lazy.of(() -> new EpollEventLoopGroup(0, ThreadFactoryImpl.of("EpollServerChild").daemon(true)));

    public static final Lazy<KQueueEventLoopGroup> KQUEUE_CLIENT_LOOP_GROUP = Lazy.of(() -> new KQueueEventLoopGroup(0, ThreadFactoryImpl.of("KQueueClientWorker").daemon(true)));
    public static final Lazy<KQueueEventLoopGroup> KQUEUE_SERVER_PARENT_LOOP_GROUP = Lazy.of(() -> new KQueueEventLoopGroup(0, ThreadFactoryImpl.of("KQueueServerParent").daemon(true)));
    public static final Lazy<KQueueEventLoopGroup> KQUEUE_SERVER_CHILD_LOOP_GROUP = Lazy.of(() -> new KQueueEventLoopGroup(0, ThreadFactoryImpl.of("KQueueServerChild").daemon(true)));


    public static final LazyGroups NIO = new LazyGroups(NIO_CLIENT_LOOP_GROUP, NIO_SERVER_PARENT_LOOP_GROUP, NIO_SERVER_CHILD_LOOP_GROUP);
    public static final LazyGroups EPOLL = new LazyGroups(EPOLL_CLIENT_LOOP_GROUP, EPOLL_SERVER_PARENT_LOOP_GROUP, EPOLL_SERVER_CHILD_LOOP_GROUP);
    public static final LazyGroups KQUEUE = new LazyGroups(KQUEUE_CLIENT_LOOP_GROUP, KQUEUE_SERVER_PARENT_LOOP_GROUP, KQUEUE_SERVER_CHILD_LOOP_GROUP);

    private final Lazy<? extends EventLoopGroup> clientLoopGroup;
    private final Lazy<? extends EventLoopGroup> serverParentLoopGroup;
    private final Lazy<? extends EventLoopGroup> serverChildLoopGroup;

    private LazyGroups(final Lazy<? extends EventLoopGroup> clientLoopGroup, final Lazy<? extends EventLoopGroup> serverParentLoopGroup, final Lazy<? extends EventLoopGroup> serverChildLoopGroup) {
        this.clientLoopGroup = clientLoopGroup;
        this.serverParentLoopGroup = serverParentLoopGroup;
        this.serverChildLoopGroup = serverChildLoopGroup;
    }

    public Lazy<? extends EventLoopGroup> getClientLoopGroup() {
        return this.clientLoopGroup;
    }

    public Lazy<? extends EventLoopGroup> getServerParentLoopGroup() {
        return this.serverParentLoopGroup;
    }

    public Lazy<? extends EventLoopGroup> getServerChildLoopGroup() {
        return this.serverChildLoopGroup;
    }

}
