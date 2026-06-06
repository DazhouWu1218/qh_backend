package com.njht.webyun.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * InnoDB free: 13312 kB
 * @author tianjm
 * @date 2019-11-01
 */
@ApiModel(value="CurrentUser",description="系统用户")
public class CurrentUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="用户id")
    private Integer userId;
    @ApiModelProperty(value="用户名")
    private String username;
    @ApiModelProperty(value="真实姓名")
    private String realName;
    @ApiModelProperty(value="用户密码")
    private String password;
    @ApiModelProperty(value="机构ID")
    private Integer orgId;
    @ApiModelProperty(value="是否锁定（0-未锁定，1-已锁定）")
    private boolean locked;
    @ApiModelProperty(value="用户密码输错次数")
    private Integer errorTime;
    @ApiModelProperty(value="年龄")
    private Integer age;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value="用户生日")
    private Date birthday;
    @ApiModelProperty(value="固定电话")
    private String phone;
    @ApiModelProperty(value="手机号")
    private String mobile;
    @ApiModelProperty(value="电子邮件")
    private String email;
    @ApiModelProperty(value="地址")
    private String address;
    @ApiModelProperty(value="备注")
    private String remark;
    @ApiModelProperty(value="用户头像图片文件")
    private String userPicData;
    @ApiModelProperty(value="用户头像文件后缀")
    private String userPicSuffix;
    @ApiModelProperty(value="最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginDate;
    @ApiModelProperty(value="最后登录ip")
    private String lastLoginIp;
    @ApiModelProperty(value="是否删除（0-未删除，1-已删除）")
    private Integer deleted;
    @ApiModelProperty(value="创建人")
    private Integer createdBy;
    @ApiModelProperty(value="创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;
    @ApiModelProperty(value="最后修改人ID")
    private Integer lastUpdatedBy;
    @ApiModelProperty(value="最后修改人姓名")
    private String lastUpdatedByName;
    @ApiModelProperty(value="最后修改日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdatedDate;
    @ApiModelProperty(value="关于用户")
    private String about;
    @ApiModelProperty(value="用户sessionid")
    private String sessionId;
    @ApiModelProperty(value="机构编码")
    private String orgCode;
    @ApiModelProperty(value="机构名称")
    private String orgName;
    @ApiModelProperty(value="机构继承名称")
    private String inheritedName;
    @ApiModelProperty(value="拥有权限菜单")
    private List<Map<String, Object>> menus;
    @ApiModelProperty(value="机构名称")
    private Map<String, Boolean> function;
    private List<SysRole> roles;

    @ApiModelProperty(value = "数管系统使用的组织结构")
    private Integer departmentId;

    @ApiModelProperty(value = "数管系统返回的结果")
    private Object content;

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    private  Set<GrantedAuthority> authorities;
    private  boolean accountNonExpired;
    private  boolean accountNonLocked;
    private  boolean credentialsNonExpired;
    private  boolean enabled;

    public CurrentUser() {
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(Integer errorTime) {
        this.errorTime = errorTime;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserPicData() {
        return userPicData;
    }

    public void setUserPicData(String userPicData) {
        this.userPicData = userPicData;
    }

    public String getUserPicSuffix() {
        return userPicSuffix;
    }

    public void setUserPicSuffix(String userPicSuffix) {
        this.userPicSuffix = userPicSuffix;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getInheritedName() {
        return inheritedName;
    }

    public void setInheritedName(String inheritedName) {
        this.inheritedName = inheritedName;
    }

    public String getLastUpdatedByName() {
        return lastUpdatedByName;
    }

    public void setLastUpdatedByName(String lastUpdatedByName) {
        this.lastUpdatedByName = lastUpdatedByName;
    }


    public CurrentUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                   boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, int userId, String realName, int orgId, String orgCode,
                   String orgName, String inheritedName, List<Map<String, Object>> menus, Map<String, Boolean> function, String phone,
                   String mobile, String email, String address, String remark)
    {

        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.userId = userId;
        this.realName = realName;
        this.orgId = orgId;
        this.orgCode = orgCode;
        this.orgName = orgName;
        this.inheritedName =inheritedName;
        this.menus = menus;
        this.function = function;
        this.phone=phone;
        this.mobile=mobile;
        this.email=email;
        this.address=address;
        this.remark=remark;

    }


    //账户是否未过期,过期无法验证
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    //指定用户是否解锁,锁定的用户无法进行身份验证
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !this.locked;//0-未锁定  1-锁定
    }

    //指示是否已过期的用户的凭据(密码),过期的凭据防止认证
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public List<Map<String, Object>> getMenus() {
        return menus;
    }

    public void setMenus(List<Map<String, Object>> menus) {
        this.menus = menus;
    }

    public Map<String, Boolean> getFunction() {
        return function;
    }

    public void setFunction(Map<String, Boolean> function) {
        this.function = function;
    }

//    @JsonIgnore
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
////        return null;
//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        if (roles == null){
//            return null;
//        }
//        for (SysRole role : roles) {
//            authorities.add(new SimpleGrantedAuthority(String.valueOf(role.getRoleId())));
//        }
//        return authorities;
//    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setLoginInfo(String ip, Date date, String sessionId)
    {
        this.lastLoginIp = ip;
        this.lastLoginDate = date;
        this.sessionId = sessionId;
    }

 /*   public Object deepClone() throws IOException, ClassNotFoundException {
        // 将对象写到流里
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        // 将对象从流量读出来
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (ois.readObject());

    }*/

}