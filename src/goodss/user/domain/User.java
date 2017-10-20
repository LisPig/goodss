package goodss.user.domain;

/**
 * 用户模块实体类
 * @author lsp11
 *
 */
public class User {
	private String uid;
	private String loginname;//登陆名
	private String loginpass;//登陆密码
	private String email;//邮箱
	private boolean status;//状态，true表示激活
	private String activationCode;//激活码
	
	//注册表单
	private String reloginpass;//确认密码
	private String verifyCode;//验证码
	
	private String newpass;//新密码

	public String getReloginpass() {
		return reloginpass;
	}

	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	
	
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getLoginpass() {
		return loginpass;
	}

	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public String getNewpass() {
		return newpass;
	}

	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "User [uid=" + uid + ", loginname=" + loginname + ", loginpass="
		+ loginpass + ", email=" + email + ", status=" + status
		+ ", activationCode=" + activationCode + ", reloginpass="
		+ reloginpass + ", verifyCode=" + verifyCode + ", newpass="
		+ newpass + "]";
	}
	
	
	
	

}
