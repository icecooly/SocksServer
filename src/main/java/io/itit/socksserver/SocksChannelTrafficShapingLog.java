package io.itit.socksserver;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author skydu
 *
 */
public class SocksChannelTrafficShapingLog {
	//
	private static final Logger logger = LoggerFactory.getLogger(SocksChannelTrafficShapingLog.class);

	/**
	 * 
	 * @param ctx
	 */
	public void log(ChannelHandlerContext ctx) {
		SocksChannelTrafficShapingHandler handler = SocksChannelTrafficShapingHandler.getHandler(ctx);
		if (handler == null) {
			return;
		}
		InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
		InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		long readByte = handler.trafficCounter().cumulativeReadBytes();
		long writeByte = handler.trafficCounter().cumulativeWrittenBytes();
		Date startTime = handler.getStartTime();
		Date endTime = handler.getStartTime();
		//
		if(logger.isInfoEnabled()) {
			logger.info("userName:{},startTime:{},endTime:{},"
				+ "localAddr:{}/{},remoteAddr:{}/{},"
				+ "readByte:{},writeByte:{},"
				+ "using:{}ms",
				handler.getUserName(), formatDate(startTime), formatDate(endTime), getLocalAddress(),
				localAddress.getPort(), remoteAddress.getAddress().getHostAddress(), remoteAddress.getPort(), readByte,
				writeByte, endTime.getTime() - startTime.getTime());
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	/**
	 * 
	 * @return
	 */
	private static String getLocalAddress() {
		try {
			for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces
					.hasMoreElements();) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
					continue;
				}
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				if (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					if (address instanceof Inet4Address) {
						return address.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "127.0.0.1";
	}

}
