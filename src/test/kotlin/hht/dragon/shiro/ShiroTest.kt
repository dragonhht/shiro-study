package hht.dragon.shiro

import com.alibaba.druid.pool.DruidDataSource
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.authc.credential.HashedCredentialsMatcher
import org.apache.shiro.config.IniSecurityManagerFactory
import org.apache.shiro.crypto.hash.Md5Hash
import org.apache.shiro.mgt.DefaultSecurityManager
import org.apache.shiro.realm.SimpleAccountRealm
import org.apache.shiro.realm.jdbc.JdbcRealm
import org.apache.shiro.realm.text.IniRealm
import org.junit.Before
import org.junit.Test
import javax.sql.DataSource

/**
 * Shiro学习.
 * User: huang
 * Date: 18-7-7
 */
class ShiroTest {

    val simpleAccountRealm = SimpleAccountRealm()

    @Before
    fun addUser() {
        simpleAccountRealm.addAccount("huang", "202cb962ac59075b964b07152d234b70", "admin", "user")
    }

    /**
     * Shiro初体验.
     */
    @Test
    fun testShiro() {

        // 加密
        val matchaer = HashedCredentialsMatcher()
        // 使用md5加密
        matchaer.hashAlgorithmName = "md5"
        // 设置加密次数
        matchaer.hashIterations = 1
        simpleAccountRealm.setCredentialsMatcher(matchaer)

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

        //val realm = IniRealm("classpath:shiro.ini")

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


    val dataSource = DruidDataSource()


    /**
     * 从数据库中获取权限信息.
     */
    @Test
    fun testJdbc() {
        // 数据源设置
        dataSource.url = "jbdc:mysql://localhost:3306/shiro"
        dataSource.username = "root"
        dataSource.password = "123"

        // 创建JdbcRealm
        val jdbcRealm = JdbcRealm()
        // 设置数据源
        jdbcRealm.setDataSource(dataSource)

        // 构建SecurityManager
        val manager = DefaultSecurityManager()
        manager.setRealm(jdbcRealm)
        // 提交请求认证
        SecurityUtils.setSecurityManager(manager)
        val subject = SecurityUtils.getSubject()
        // 模拟用户token
        val token = UsernamePasswordToken("huang", "123")
        // 登录
        subject.login(token)

        // 我们的数据库表可与系统的不一致，则需要自定义查询语句如
//        val authenticatQuery = "select password from users where username = ?"
//        jdbcRealm.setAuthenticationQuery(authenticatQuery)

        println(subject.isAuthenticated)

        // 检查用户是否具有角色
        subject.checkRoles("admin", "user")
        // 退出
        subject.logout()
        println(subject.isAuthenticated)
    }

    /**
     * 自定义realm测试.
     */
    @Test
    fun MyRealmTest() {

        // 构造自定义realm
        val myRealm = MyRealm()

        // 加密
        val matchaer = HashedCredentialsMatcher()
        // 使用md5加密
        matchaer.hashAlgorithmName = "md5"
        // 设置加密次数
        matchaer.hashIterations = 1
        myRealm.credentialsMatcher = matchaer

        // 构建SecurityManager
        val manager = DefaultSecurityManager()
        manager.setRealm(myRealm)

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
        // 检查用户是否有权限
        subject.checkPermission("user:delete")

        // 退出
        subject.logout()
        println(subject.isAuthenticated)
    }

}

//fun main(args: Array<String>) {
//    val md5 = Md5Hash("123", "huang")
//    println(md5)
//}