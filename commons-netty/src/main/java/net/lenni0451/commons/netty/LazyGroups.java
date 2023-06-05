package net.lenni0451.commons.netty;

import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import net.lenni0451.commons.Lazy;
import net.lenni0451.commons.threading.ThreadFactoryImpl;

public class LazyGroups {

    /**
     * Event loop group for nio clients.
     */
    public static final Lazy<NioEventLoopGroup> NIO_CLIENT_LOOP_GROUP = Lazy.of(() -> new NioEventLoopGroup(0, ThreadFactoryImpl.of("NioClientWorker #").daemon(true)));
    /**
     * Event loop group for epoll clients.
     */
    public static final Lazy<EpollEventLoopGroup> EPOLL_CLIENT_LOOP_GROUP = Lazy.of(() -> new EpollEventLoopGroup(0, ThreadFactoryImpl.of("EpollClientWorker").daemon(true)));

    /**
     * Parent event loop group for nio servers.
     */
    public static final Lazy<NioEventLoopGroup> NIO_SERVER_PARENT_LOOP_GROUP = Lazy.of(() -> new NioEventLoopGroup(0, ThreadFactoryImpl.of("NioServerParent").daemon(true)));
    /**
     * Child event loop group for nio servers.
     */
    public static final Lazy<NioEventLoopGroup> NIO_SERVER_CHILD_LOOP_GROUP = Lazy.of(() -> new NioEventLoopGroup(0, ThreadFactoryImpl.of("NioServerChild").daemon(true)));

    /**
     * Parent event loop group for epoll servers.
     */
    public static final Lazy<EpollEventLoopGroup> EPOLL_SERVER_PARENT_LOOP_GROUP = Lazy.of(() -> new EpollEventLoopGroup(0, ThreadFactoryImpl.of("EpollServerParent").daemon(true)));
    /**
     * Child event loop group for epoll servers.
     */
    public static final Lazy<EpollEventLoopGroup> EPOLL_SERVER_CHILD_LOOP_GROUP = Lazy.of(() -> new EpollEventLoopGroup(0, ThreadFactoryImpl.of("EpollServerChild").daemon(true)));

}
