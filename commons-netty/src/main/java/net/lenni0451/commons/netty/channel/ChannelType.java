package net.lenni0451.commons.netty.channel;

import io.netty.channel.IoHandlerFactory;

import java.util.function.Supplier;

public interface ChannelType {

    Supplier<? extends IoHandlerFactory> ioHandlerFactorySupplier();

}
