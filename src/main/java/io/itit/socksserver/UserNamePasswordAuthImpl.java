package io.itit.socksserver;

/**
 * 
 * @author skydu
 *
 */
public class UserNamePasswordAuthImpl implements UserNamePasswordAuth {
	//
	private String configUserName;
    private String configPassword;
    
    /**
     * 
     * @param configUserName
     * @param configPassword
     */
	public UserNamePasswordAuthImpl(String configUserName, String configPassword) {
		this.configUserName=configUserName;
		this.configPassword=configPassword;
	}
	
	/**
	 * 
	 */
	public boolean auth(String userName, String password) {
		if(configUserName==null||configPassword==null) {
			return true;
		}
		if(userName==null||password==null) {
			return false;
		}
		if(!userName.equals(configUserName)||
				!password.equals(configPassword)) {
			return false;
		}
		return true;
	}

}
