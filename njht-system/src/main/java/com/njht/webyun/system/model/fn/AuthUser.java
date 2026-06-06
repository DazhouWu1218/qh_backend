package com.njht.webyun.system.model.fn;

import javax.validation.constraints.Email;
import java.util.Date;

public class AuthUser {

    private static final long serialVersionUID = -4439197626405046146L;

    private String uid;

    /** 用户名(nick_name) */
    private String username;

    /** 密码(MD5(密码+盐)) */
    private String password;

    /** 盐 */
    private String salt;

    /** 用户真名 */
    private String realName;

    /** 头像 */
    private String avatar;

    /** 电话号码(唯一) */
    private String phone;

    /**  */
    private String address;

    /**  */
    private String companyType;

    /**  */
    private String industryType;

    /** 邮件地址(唯一) */
    @Email
    private String email;

    /** 性别(1.男 2.女) */
    private Byte sex = 1;

    /** 账户状态(1.正常 2.锁定 3.删除 4.非法) */
    private Byte status = 1;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 创建来源(1.web 2.android 3.ios 4.win 5.macos 6.ubuntu) */
    private Byte createWhere = 1;

    /** 验证码 */
    private String code;

    /** 重复的密码*/
    private String rePassword;

    private String region;

    public AuthUser() {
    }

    public AuthUser(String username, String password, String salt, String realName, String avatar, String phone, String address, String companyType, String industryType, @Email String email, Byte sex, Byte status, Date createTime, Date updateTime, Byte createWhere, String code, String rePassword, String region) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.realName = realName;
        this.avatar = avatar;
        this.phone = phone;
        this.address = address;
        this.companyType = companyType;
        this.industryType = industryType;
        this.email = email;
        this.sex = sex;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.createWhere = createWhere;
        this.code = code;
        this.rePassword = rePassword;
        this.region = region;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getCreateWhere() {
        return createWhere;
    }

    public void setCreateWhere(Byte createWhere) {
        this.createWhere = createWhere;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
