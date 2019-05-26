package io.itit.socksserver;

/**
 * 用户名密码认证
 * @author skydu
 *
 */
public interface UserNamePasswordAuth {

	/**
	 * 
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean auth(String user, String password);
	
}
