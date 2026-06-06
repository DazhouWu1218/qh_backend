package com.njht.webyun.system.model.base;

/**
 * 
 * @author David
 */
public class BeanProperty {
	
	/**
	 * 基础bean的常量
	 */
    public static final class Bean
    {
        public static final String DELETED = "deleted";
        public static final String CREATED_BY = "createdBy";
        public static final String CREATED_DATE = "createdDate";
        public static final String LAST_UPDATED_BY = "lastUpdatedBy";
        public static final String LAST_UPDATED_DATE = "lastUpdatedDate";
        
        public static final String PAGING = "paging";
        public static final String LIST = "list";
        
        public static final String BEGIN_DATE = "beginDate";
        public static final String END_DATE = "endDate";
    }

	/**
	 * 用户bean的常量
	 * @author David
	 */
	 public static final class UserBean
    {
        public static final String USER_ID = "userId";
        public static final String PRIMARY_KEY = "pk_" + USER_ID;
        public static final String USERNAME = "username";
        public static final String REAL_NAME = "realName";
        public static final String PWD = "pwd";
        public static final String ORG_ID = "orgId";
        public static final String LOCKED = "locked";
        public static final String ERROR_TIME = "errorTime";
        public static final String PHONE = "phone";
        public static final String MOBILE = "mobile";
        public static final String EMAIL = "email";
        public static final String ADDRESS = "address";
        public static final String REMARK = "remark";
        public static final String LAST_LOGIN_DATE = "lastLoginDate";
        public static final String LAST_LOGIN_IP = "lastLoginIp";
    }
	 
	 /**
	 * 机构bean的常量
	 * @author David
	 */
	 public static final class OrgBean
    {
        public static final String CONTAIN_SUB = "containSub";
        public static final String ORG_ID = "orgId";
        public static final String PRIMARY_KEY = "pk_" + ORG_ID;
        public static final String ORG_NAME = "orgName";
        public static final String ORG_CODE = "orgCode";
        public static final String PARENT_CODE = "parentCode";
        public static final String PARENT_INHERITED_ID = "parentInheritedId";
        public static final String INHERITED_ID = "inheritedId";
        public static final String INHERITED_NAME = "inheritedName";
        public static final String PARENT_ID = "parentId";
        public static final String SORT_NUM = "sortNum";
        public static final String LEVEL_NUM = "levelNum";
        public static final String ADDRESS = "address";
        public static final String REMARK = "remark";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String DELETED = "deleted";
        public static final String TEL = "tel";
        public static final String ISNOTHALL = "isNotHall";

    }
	 
	 /**
		 * 定义Num的常量
		 */
	    public static final class Num
	    {
	        public static final int ZERO = 0;
	        public static final int ONE = 1;
	        public static final int TWO = 2;
	        public static final int THREE = 3;
	        public static final int FOUR = 4;
	        public static final int FIVE = 5;
	        public static final int SIX = 6;
	        public static final int SEVEN = 7;
	        public static final int Eight = 8;
	        public static final int Nine = 9;
	        public static final int TEN = 10;
	    }

    /**
     * 登录日志用常量
     */
    public static final class LoginLog {
        public static final String SESSION_ID = "sessionId";
        public static final String IP = "ip";
        public static final String LOGIN_DATE = "loginDate";
        public static final String LOGOUT_DATE = "logoutDate";
        public static final String OS = "os";
        public static final String BROWSER = "browser";
        public static final String USER_AGENT = "userAgent";
    }

    /**
     * 行为日志常量
     */
    public static final class BehaviorLog {

        public static final String BEHAVIOR_ID = "behaviorId";
        public static final String LOGGED_DATE = "loggedDate";
        public static final String ACTION = "action";
        public static final String DATA_CHANGED = "dataChanged";
    }

    /**
     * 角色
     */
    public static final class Role
    { 
        public static final String ROLE_ID = "roleId"; 
        public static final String PRIMARY_KEY = "pk_" + ROLE_ID;
        public static final String ROLE_NAME = "roleName"; 
        public static final String REMARK = "remark";
    }

    public static final class Cache
    {
        public static final String ORG_CACHE = "orgCache";
        public static final String DIC_CACHE = "dicCache";
        public static final String MENU_CACHE = "menuCache";
        public static final String PERMISSION_CACHE = "permissionCache";
    }

    public static final class Function
    {
        public static final String FUN_ID = "funId";
        public static final String PRIMARY_KEY = "pk_" + FUN_ID;
        public static final String FUN_NAME = "funName";
        public static final String PARENT_ID = "parentId";
        public static final String SORT_NUM = "sortNum";
        public static final String LEVEL_NUM = "levelNum";
        public static final String ENABLED = "enabled";
    }

    public static final class Url {
        public static final String URL_ID = "urlId";
        public static final String PRIMARY_KEY = "pk_" + URL_ID;
        public static final String URL_NAME = "urlName";
        public static final String URL_PATTERN = "urlPattern";
        public static final String HTTP_METHOD = "httpMethod";
        public static final String ACTION_CODE = "actionCode";
        public static final String ARGS_CODE = "argsCode";
        public static final String LOGGED_DATA_CHANGED = "loggedDataChanged";
    }
}
