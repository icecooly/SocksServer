package io.itit.socksserver;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 * @author skydu
 *
 */
public class SocksChannelInitializer extends ChannelInitializer<SocketChannel>{
	//
	SocksServer socksServer;
	//
	public SocksChannelInitializer(SocksServer socksServer) {
		this.socksServer=socksServer;
	}
	//
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline cp=ch.pipeline();
		cp.addLast(
				SocksChannelTrafficShapingHandler.class.getSimpleName(), 
				new SocksChannelTrafficShapingHandler(socksServer.getCheckInterval())
				);
		cp.addLast(new IdleStateHandler(
				socksServer.getReaderIdleTimeSeconds(),
				socksServer.getWriterIdleTimeSeconds(), 
				socksServer.getAllIdleTimeSeconds()));
		cp.addLast(new SocksIdleHandler());
		if(socksServer.getLogLevel()!=null) {
			cp.addLast(new LoggingHandler(socksServer.getLogLevel()));
		}
		cp.addLast(Socks5ServerEncoder.DEFAULT);
		cp.addLast(new Socks5InitialRequestDecoder());
		cp.addLast(new Socks5InitialRequestHandler(socksServer));
		if(socksServer.isNeedAuth()) {
			cp.addLast(new Socks5PasswordAuthRequestDecoder());
			cp.addLast(new Socks5PasswordAuthRequestHandler(socksServer.getAuth()));
		}
		cp.addLast(new Socks5CommandRequestDecoder());
		cp.addLast(new Socks5CommandRequestHandler(socksServer.getBossGroup()));
	}

}
