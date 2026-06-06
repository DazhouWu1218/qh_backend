package com.njht.webyun.system.model.sysOrg;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author David
 * @Time 2019年11月14日 下午9:49:07
 */
public class PersonExportVo implements Serializable {
	 private static final long serialVersionUID = 1L;
	    /**
	     * 姓名
	     */
	    @Excel(name = "姓名", orderNum = "0", width = 15)
	    private String name;
	    
	    /**
	     * 登录用户名
	     */
	    @Excel(name = "用户名", orderNum = "1", width = 15)
	    private String username;

	    @Excel(name = "手机号码", orderNum = "2", width = 15)
	    private String phoneNumber;

	    /**
	     * 人脸图片
	     */
	    @Excel(name = "人脸图片", orderNum = "3", width = 15, height = 30, type = 2)
	    private String imageUrl;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		/**
		 * 
		 */
		public PersonExportVo() {
			super();
		}
	    
}
