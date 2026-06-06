package com.njht.webyun.system.model.sysUser;


public class ResetPasswordModel
{
    private static final long serialVersionUID = -7202949747017421024L;
    private int userId;
    private String username;

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }
    
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
