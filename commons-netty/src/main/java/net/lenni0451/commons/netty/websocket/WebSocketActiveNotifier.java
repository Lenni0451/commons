package net.lenni0451.commons.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import javax.annotation.Nonnull;

/**
 * Delay the channelActive until the WebSocket handshake is complete.
 */
public class WebSocketActiveNotifier extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(@Nonnull ChannelHandlerContext ctx) {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            ctx.fireChannelActive();
            ctx.pipeline().remove(this);
        }

        super.userEventTriggered(ctx, evt);
    }

}
