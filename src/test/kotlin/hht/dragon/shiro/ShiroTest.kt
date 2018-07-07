package hht.dragon.shiro

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.config.IniSecurityManagerFactory
import org.apache.shiro.mgt.DefaultSecurityManager
import org.apache.shiro.realm.SimpleAccountRealm
import org.junit.Before
import org.junit.Test

/**
 * Shiro学习.
 * User: huang
 * Date: 18-7-7
 */
class ShiroTest {

    val simpleAccountRealm = SimpleAccountRealm()

    @Before
    fun addUser() {
        simpleAccountRealm.addAccount("huang", "123", "admin", "user")
    }

    /**
     * Shiro初体验.
     */
    @Test
    fun testShiro() {

        // 构建SecurityManager
        val manager = DefaultSecurityManager()
        manager.setRealm(simpleAccountRealm)

        // 提交请求认证
        SecurityUtils.setSecurityManager(manager)
        val subject = SecurityUtils.getSubject()

        // 模拟用户token
        val token = UsernamePasswordToken("huang", "123")

        // 登录
        subject.login(token)
        println(subject.isAuthenticated)

        // 检查用户是否具有角色
        subject.checkRoles("admin", "user")

        // 退出
        subject.logout()
        println(subject.isAuthenticated)
    }

    /**
     * Shiro官方示例, 从ini文件中获取权限信息.
     */
    @Test
    fun firstTest() {
        // 加载ini文件信息，并创建SecurityManager
        val factory = IniSecurityManagerFactory("classpath:shiro.ini")
        val manager = factory.instance
        SecurityUtils.setSecurityManager(manager)

        val subject = SecurityUtils.getSubject()

        val token = UsernamePasswordToken("lonestarr", "vespa")
        subject.login(token)

        println(subject.isAuthenticated)

        println(subject.hasRole("schwartz"))
    }

}