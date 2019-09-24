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
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;

/**
 * 
 * @author skydu
 *
 */
public final class SocksServer{
	//
	private static Logger logger=LoggerFactory.getLogger(SocksServer.class);
	//
	private int port;
	private int soBackLog;
	private int connectTimeoutMillis;
	private int readerIdleTimeSeconds;
	private int writerIdleTimeSeconds;
	private int allIdleTimeSeconds;
	private long checkInterval;
    private boolean needAuth;
    private String authUserName;
    private String authPassword;
    private LogLevel logLevel;
    private ServerBootstrap serverStrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private UserNamePasswordAuth auth;
    //
    public SocksServer() {
    		port=1080;
    		soBackLog=1024;
    	 	connectTimeoutMillis=1000;
    		logLevel=LogLevel.INFO;
    		readerIdleTimeSeconds=10;
    		writerIdleTimeSeconds=60;
    		allIdleTimeSeconds=0;
    		checkInterval=3000;
    }
    //
	public void start() throws Exception {
		if(needAuth&&auth==null) {
			auth=new UserNamePasswordAuthImpl(authUserName,authPassword);
		}
		bossGroup= new NioEventLoopGroup(2);
		workerGroup = new NioEventLoopGroup();
		try {
			serverStrap = new ServerBootstrap();
			serverStrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, soBackLog)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,connectTimeoutMillis)
			.childHandler(new SocksChannelInitializer(this));
			ChannelFuture future = serverStrap.bind(port).sync();
			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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
	//
	/**
	 * @return the readerIdleTimeSeconds
	 */
	public int getReaderIdleTimeSeconds() {
		return readerIdleTimeSeconds;
	}
	/**
	 * @param readerIdleTimeSeconds the readerIdleTimeSeconds to set
	 */
	public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
		this.readerIdleTimeSeconds = readerIdleTimeSeconds;
	}
	/**
	 * @return the writerIdleTimeSeconds
	 */
	public int getWriterIdleTimeSeconds() {
		return writerIdleTimeSeconds;
	}
	/**
	 * @param writerIdleTimeSeconds the writerIdleTimeSeconds to set
	 */
	public void setWriterIdleTimeSeconds(int writerIdleTimeSeconds) {
		this.writerIdleTimeSeconds = writerIdleTimeSeconds;
	}
	/**
	 * @return the allIdleTimeSeconds
	 */
	public int getAllIdleTimeSeconds() {
		return allIdleTimeSeconds;
	}
	/**
	 * @param allIdleTimeSeconds the allIdleTimeSeconds to set
	 */
	public void setAllIdleTimeSeconds(int allIdleTimeSeconds) {
		this.allIdleTimeSeconds = allIdleTimeSeconds;
	}
	/**
	 * @return the checkInterval
	 */
	public long getCheckInterval() {
		return checkInterval;
	}
	/**
	 * @param checkInterval the checkInterval to set
	 */
	public void setCheckInterval(long checkInterval) {
		this.checkInterval = checkInterval;
	}
	/**
	 * @return the needAuth
	 */
	public boolean isNeedAuth() {
		return needAuth;
	}
	/**
	 * @param needAuth the needAuth to set
	 */
	public void setNeedAuth(boolean needAuth) {
		this.needAuth = needAuth;
	}
	/**
	 * @return the authUserName
	 */
	public String getAuthUserName() {
		return authUserName;
	}
	/**
	 * @param authUserName the authUserName to set
	 */
	public void setAuthUserName(String authUserName) {
		this.authUserName = authUserName;
	}
	/**
	 * @return the authPassword
	 */
	public String getAuthPassword() {
		return authPassword;
	}
	/**
	 * @param authPassword the authPassword to set
	 */
	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}
	/**
	 * @return the logLevel
	 */
	public LogLevel getLogLevel() {
		return logLevel;
	}
	/**
	 * @param logLevel the logLevel to set
	 */
	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}
	/**
	 * @return the auth
	 */
	public UserNamePasswordAuth getAuth() {
		return auth;
	}
	/**
	 * @param auth the auth to set
	 */
	public void setAuth(UserNamePasswordAuth auth) {
		this.auth = auth;
	}
	/**
	 * @param connectTimeoutMillis the connectTimeoutMillis to set
	 */
	public void setConnectTimeoutMillis(int connectTimeoutMillis) {
		this.connectTimeoutMillis = connectTimeoutMillis;
	}
	/**
	 * @return the connectTimeoutMillis
	 */
	public int getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}
	
	/**
	 * @return the soBackLog
	 */
	public int getSoBackLog() {
		return soBackLog;
	}
	/**
	 * @param soBackLog the soBackLog to set
	 */
	public void setSoBackLog(int soBackLog) {
		this.soBackLog = soBackLog;
	}
	/**
	 * @return the serverStrap
	 */
	public ServerBootstrap getServerStrap() {
		return serverStrap;
	}
	/**
	 * @param serverStrap the serverStrap to set
	 */
	public void setServerStrap(ServerBootstrap serverStrap) {
		this.serverStrap = serverStrap;
	}
	/**
	 * @return the bossGroup
	 */
	public EventLoopGroup getBossGroup() {
		return bossGroup;
	}
	/**
	 * @param bossGroup the bossGroup to set
	 */
	public void setBossGroup(EventLoopGroup bossGroup) {
		this.bossGroup = bossGroup;
	}
	/**
	 * @return the workerGroup
	 */
	public EventLoopGroup getWorkerGroup() {
		return workerGroup;
	}
	/**
	 * @param workerGroup the workerGroup to set
	 */
	public void setWorkerGroup(EventLoopGroup workerGroup) {
		this.workerGroup = workerGroup;
	}
	//
	public static void main(String[] args) throws Exception {
		String needAuth=System.getProperty("needAuth");
		String authUserName=System.getProperty("authUserName");
		String authPassword=System.getProperty("authPassword");
		SocksServer socksServer = new SocksServer();
		if(needAuth!=null) {
			socksServer.setNeedAuth(Boolean.valueOf(needAuth));
		}
		if(authUserName!=null) {
			socksServer.setAuthUserName(authUserName);
		}
		if(authPassword!=null) {
			socksServer.setAuthPassword(authPassword);
		}
		socksServer.start();
		logger.info("SocksServer started listen:{}",socksServer.getPort());
	}
}
