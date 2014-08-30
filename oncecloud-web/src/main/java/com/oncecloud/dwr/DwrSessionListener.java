package com.oncecloud.dwr;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.directwebremoting.Container;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.servlet.DwrServlet;

import com.oncecloud.entity.User;

/**
 * @author hehai
 * @version 2013/10/25
 */
public class DwrSessionListener extends DwrServlet {
	private static final long serialVersionUID = 622691644260957138L;
	public static ScriptSessionManager manager = null;

	public void init() throws ServletException {
		Container container = ServerContextFactory.get().getContainer();
		manager = container.getBean(ScriptSessionManager.class);
		ScriptSessionListener listener = new ScriptSessionListener() {
			public void sessionCreated(ScriptSessionEvent ev) {
				HttpSession session = WebContextFactory.get().getSession();
				User user = (User) session.getAttribute("user");
				ev.getSession().setAttribute("user", user);
			}

			public void sessionDestroyed(ScriptSessionEvent ev) {
			}
		};
		manager.addScriptSessionListener(listener);
	}
}