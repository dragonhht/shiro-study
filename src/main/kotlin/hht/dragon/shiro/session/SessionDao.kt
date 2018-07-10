package hht.dragon.shiro.session

import hht.dragon.shiro.utils.RedisUtil
import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO
import org.springframework.util.CollectionUtils
import org.springframework.util.SerializationUtils
import java.io.Serializable
import javax.annotation.Resource

/**
 * 自定义SessionDao.
 * User: huang
 * Date: 18-7-9
 */
class SessionDao : AbstractSessionDAO() {

    @Resource
    lateinit var redisUtil : RedisUtil

    private val SHIRO_SESSION_PREFIX = "hht-dragon:"

    /**
     * 生成redis键.
     */
    private fun key(key : String) : String {
        return SHIRO_SESSION_PREFIX + key
    }

    /**
     * 创建Session.
     */
    override fun doCreate(session: Session?): Serializable? {
        println("创建Session")
        // 获取SessionId
        val sessionId = generateSessionId(session)
        // 将Session与SessionId进行捆绑
        assignSessionId(session, sessionId)
        val key = key(session!!.id.toString())
        val value = SerializationUtils.serialize(session).toString()
        // 存储值
        redisUtil.set(key, value)
        // 设置过期时间
        redisUtil.expire(key, 600)
        return sessionId
    }

    /**
     * 更新Session.
     */
    override fun update(session: Session?) {
        if (session != null && session.id != null) {
            val key = key(session!!.id.toString())
            val value = SerializationUtils.serialize(session).toString()
            // 存储值
            redisUtil.set(key, value)
            // 设置过期时间
            redisUtil.expire(key, 600)
        }
    }

    /**
     * 获取存活的Session.
     */
    override fun getActiveSessions(): MutableCollection<Session>? {
        val keys = redisUtil.keys("$SHIRO_SESSION_PREFIX*") as Set<String>
        val sessions = HashSet<Session>()
        if (CollectionUtils.isEmpty(keys)) {
            return sessions
        }
        keys.forEach {
            val s = redisUtil.get(it)
            val session = SerializationUtils.deserialize(s!!.toByteArray()) as Session
            sessions.add(session)
        }
        return sessions
    }

    /**
     * 获得Session.
     */
    override fun doReadSession(sessionId: Serializable?): Session? {
        if (sessionId == null)
            return null
        val key = key(sessionId.toString())
        val value = redisUtil.get(key)
        return SerializationUtils.deserialize(value!!.toByteArray()) as Session?
    }

    /**
     * 删除Session.
     */
    override fun delete(session: Session?) {
        if (session == null || session.id == null)
            return
        val key = key(session.id.toString())
        redisUtil.del(key)
    }
}