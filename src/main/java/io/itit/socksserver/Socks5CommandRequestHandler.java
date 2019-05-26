package io.itit.socksserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5AddressType;
import io.netty.handler.codec.socksx.v5.Socks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
import io.netty.handler.codec.socksx.v5.Socks5CommandType;

/**
 * 
 * @author skydu
 *
 */
public class Socks5CommandRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5CommandRequest> {
	//
	EventLoopGroup bossGroup;
	//
	private static Logger logger = LoggerFactory.getLogger(Socks5CommandRequestHandler.class);

	//
	public Socks5CommandRequestHandler(EventLoopGroup bossGroup) {
		this.bossGroup = bossGroup;
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext clientChannelContext, DefaultSocks5CommandRequest msg)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("目标服务器:{},{}:{}", msg.type(), msg.dstAddr(), msg.dstPort());
		}
		if (msg.type().equals(Socks5CommandType.CONNECT)) {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(bossGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new Dst2ClientHandler(clientChannelContext));
						}
					});
			ChannelFuture future = bootstrap.connect(msg.dstAddr(), msg.dstPort());
			future.addListener(new ChannelFutureListener() {
				public void operationComplete(final ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						if (logger.isDebugEnabled()) {
							logger.debug("成功连接目标服务器");
						}
						clientChannelContext.pipeline().addLast(new Client2DstHandler(future));
						Socks5CommandResponse commandResponse = new DefaultSocks5CommandResponse(
								Socks5CommandStatus.SUCCESS, Socks5AddressType.IPv4);
						clientChannelContext.writeAndFlush(commandResponse);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("连接目标服务器失败");
						}
						Socks5CommandResponse commandResponse = new DefaultSocks5CommandResponse(
								Socks5CommandStatus.FAILURE, Socks5AddressType.IPv4);
						clientChannelContext.writeAndFlush(commandResponse);
					}
				}

			});
		} else {
			clientChannelContext.fireChannelRead(msg);
		}
	}
	//
	private static class Dst2ClientHandler extends ChannelInboundHandlerAdapter {
		private ChannelHandlerContext context;
		public Dst2ClientHandler(ChannelHandlerContext context) {
			this.context = context;
		}
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			context.writeAndFlush(msg);
		}
		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			context.channel().close();
		}
	}
	//
	private static class Client2DstHandler extends ChannelInboundHandlerAdapter {
		private ChannelFuture future;
		public Client2DstHandler(ChannelFuture future) {
			this.future = future;
		}
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			future.channel().writeAndFlush(msg);
		}
		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			future.channel().close();
		}
	}
}
