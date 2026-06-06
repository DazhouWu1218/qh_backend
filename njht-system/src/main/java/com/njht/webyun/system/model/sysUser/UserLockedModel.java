package com.njht.webyun.system.model.sysUser;


import java.util.List;

public class UserLockedModel
{
    private static final long serialVersionUID = 3604960983383802960L;
    
    private List<Integer> list;
    private boolean locked;
    
    public List<Integer> getList()
    {
        return list;
    }
    public void setList(List<Integer> list)
    {
        this.list = list;
    }
    public boolean isLocked()
    {
        return locked;
    }
    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }
}
