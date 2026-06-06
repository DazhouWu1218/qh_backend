package com.njht.webyun.zuul.session;

import com.njht.webyun.model.CurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AotoSessionListener extends HttpSessionEventPublisher {
	
	 /**  
     * 定义一个简单的内部枚举  
     */    
    private static enum SessionType {    
        ADD, DELETE;    
    }   
	
	/***
	 * 当前在线的用户session
	 */
	private static volatile ConcurrentHashMap<String, Object> sessionMap = new ConcurrentHashMap<String, Object>();
	
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		  super.sessionCreated(sessionEvent);
			handlerSessionMap(sessionEvent, SessionType.ADD);
	  }
	
	
	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		handlerSessionMap(sessionEvent, SessionType.DELETE);
		super.sessionDestroyed(sessionEvent);
	}
	
	public static ConcurrentHashMap<String, Object> getSessionMap() {
		return sessionMap;
	}
	
	public synchronized static void setSessionMap(String sessionId, Object object) {
		sessionMap.put(sessionId, object);
		
	}
	
	
	private void handlerSessionMap(HttpSessionEvent sessionEvent, SessionType type) {
		
		 HttpSession httpSession = sessionEvent.getSession();
		 String sessionId = httpSession.getId();
		
		switch (type) {    
        case ADD:
        	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();    
             if (auth != null) {    
                 Object principal = auth.getPrincipal();
                 if (principal instanceof CurrentUser) {
                	 CurrentUser user = (CurrentUser) principal; 
                	
                	 sessionMap.put(sessionId, user);
                 }    
             }
            break;    
        case DELETE:
        	Iterator<Map.Entry<String, Object>> it = sessionMap.entrySet().iterator();  
            while(it.hasNext()){  
                Map.Entry<String, Object> entry = it.next();  
                if(sessionId.equals(entry.getKey())) {
                	//使用迭代器的remove()方法删除元素  
                	it.remove();
                	break;
                }
                   
            }
            // 清楚对应的修改map
//            ToolsUtils.clearCompleteModifyMapSessionKey(sessionId);
//            ToolsUtils.clearCompleteModifyCountMap(sessionId);
            break;
            default:
            	break;
        }    
		
		
		
	}
	
}
