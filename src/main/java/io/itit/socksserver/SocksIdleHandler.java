package io.itit.socksserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 
 * @author skydu
 *
 */
public class SocksIdleHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			ctx.channel().close();
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

}
