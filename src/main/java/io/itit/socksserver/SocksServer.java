package io.itit.socksserver;
/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 
 * @author skydu
 *
 */
public final class SocksServer{
	//
	private static Logger logger=LoggerFactory.getLogger(SocksServer.class);
	//
    private int port=1088;
    private ServerBootstrap serverStrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    //
	public void init() throws Exception {
		bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        serverStrap = new ServerBootstrap();
        serverStrap.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
         .handler(new LoggingHandler(LogLevel.INFO))
         .childHandler(new SocksServerInitializer());
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		if(logger.isInfoEnabled()){
			logger.info(getClass().getSimpleName()+" start port:{}",port);
		}
        serverStrap.bind(port).sync().channel().closeFuture().sync();
	}

	public void stop() throws Exception {
		if(logger.isInfoEnabled()){
			logger.info(getClass().getSimpleName()+" stop");
		}
		if(bossGroup!=null){
			bossGroup.shutdownGracefully();
		}
		if(workerGroup!=null){
			workerGroup.shutdownGracefully();
		}
	}

	public String dump() {
		ObjectPrinter op=new ObjectPrinter();
		op.section(getClass().getSimpleName());
		op.print("port",port);
		return op.toString();
	}
	
	public static void main(String[] args) throws Exception {
		SocksServer socksServer = new SocksServer();
		socksServer.init();
		socksServer.start();
	}
}
