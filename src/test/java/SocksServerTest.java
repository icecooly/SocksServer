import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.itit.socksserver.SocksServer;

/**
 * 
 * @author skydu
 */
public class SocksServerTest {
	//
	private static Logger logger=LoggerFactory.getLogger(SocksServerTest.class);
	//
	public static void main(String[] args) throws Exception {
		SocksServer socksServer = new SocksServer();
		socksServer.start();
		logger.info("SocksServer started listen:{}",socksServer.getPort());
	}
}
