package hht.dragon.shiro.session

import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.SessionKey
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager
import org.apache.shiro.web.session.mgt.WebSessionKey
import javax.servlet.ServletRequest

/**
 * 自定义SessionManager.
 * User: huang
 * Date: 18-7-10
 */
class MyWebSessionManager : DefaultWebSessionManager() {

    override fun retrieveSession(sessionKey: SessionKey?): Session {

        val sessionId = getSessionId(sessionKey)
        var request : ServletRequest? = null
        if (sessionKey is WebSessionKey) {
            request = sessionKey.servletRequest
        }
        if (request != null && sessionId != null) {
            val session =  request.getAttribute(sessionId.toString()) as Session?
            if (session != null) {
                return session
            }
        }
        val session = super.retrieveSession(sessionKey)
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session)
        }
        return session
    }
}