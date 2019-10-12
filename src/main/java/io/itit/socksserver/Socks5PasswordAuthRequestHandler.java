package io.itit.socksserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthResponse;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthResponse;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthStatus;

/**
 * 
 * @author skydu
 *
 */
public class Socks5PasswordAuthRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5PasswordAuthRequest> {
	//
	private static final Logger logger = LoggerFactory.getLogger(Socks5PasswordAuthRequestHandler.class);
	//
	private UserNamePasswordAuth auth;
	/**
	 * 
	 * @param auth
	 */
	public Socks5PasswordAuthRequestHandler(UserNamePasswordAuth auth) {
		this.auth = auth;
	}
	
	/**
	 * 
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5PasswordAuthRequest msg) throws Exception {
		if(logger.isInfoEnabled()) {
			logger.info("用户名密码: " + msg.username() + "," + msg.password());
		}
		if(auth.auth(msg.username(), msg.password())) {
			SocksChannelTrafficShapingHandler.setUserName(ctx, msg.username());
			Socks5PasswordAuthResponse passwordAuthResponse = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.SUCCESS);
			ctx.writeAndFlush(passwordAuthResponse);
		} else {
			SocksChannelTrafficShapingHandler.setUserName(ctx, "[认证失败]");
			Socks5PasswordAuthResponse passwordAuthResponse = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.FAILURE);
			ctx.writeAndFlush(passwordAuthResponse).addListener(ChannelFutureListener.CLOSE);
		}
	}

}
