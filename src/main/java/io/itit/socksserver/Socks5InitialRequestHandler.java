package io.itit.socksserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.SocksVersion;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialResponse;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponse;

/**
 * 
 * @author skydu
 *
 */
public class Socks5InitialRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5InitialRequest> {
	//
	private static final Logger logger = LoggerFactory.getLogger(Socks5InitialRequestHandler.class);
	//
	private SocksServer socksServer;
	
	public Socks5InitialRequestHandler(SocksServer socksServer) {
		this.socksServer = socksServer;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5InitialRequest msg) throws Exception {
		if(msg.decoderResult().isFailure()) {
			logger.warn("decoderResult failed");
			ctx.fireChannelRead(msg);
		} else {
			if(msg.version().equals(SocksVersion.SOCKS5)) {
				if(socksServer.isNeedAuth()) {
					Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.PASSWORD);
					ctx.writeAndFlush(initialResponse);
				} else {
					Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH);
					ctx.writeAndFlush(initialResponse);
				}
			}
		}
	}

}
