package io.itit.socksserver;

import java.util.Date;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

/**
 * 
 * @author skydu
 *
 */
public class SocksChannelTrafficShapingHandler extends ChannelTrafficShapingHandler {
	
	private Date startTime;
	
	private Date endTime;
	
	private String userName = "[匿名]";
	
	private SocksChannelTrafficShapingLog log;
	
	/**
	 * 
	 * @param checkInterval
	 */
	public SocksChannelTrafficShapingHandler(long checkInterval) {
		super(checkInterval);
		this.log = new SocksChannelTrafficShapingLog();
	}
	
	/**
	 * 
	 * @param ctx
	 * @return
	 */
	public static SocksChannelTrafficShapingHandler getHandler(ChannelHandlerContext ctx) {
		return (SocksChannelTrafficShapingHandler)ctx.pipeline().
				get(SocksChannelTrafficShapingHandler.class.getSimpleName());
	}
	
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		startTime = new Date();
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		endTime = new Date();
		log.log(ctx);
		super.channelInactive(ctx);
	}

	public static void setUserName(ChannelHandlerContext ctx, String userName) {
		getHandler(ctx).userName = userName;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the log
	 */
	public SocksChannelTrafficShapingLog getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(SocksChannelTrafficShapingLog log) {
		this.log = log;
	}
}
