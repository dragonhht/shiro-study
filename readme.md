# Shiro学习

-   Shiro依赖

```
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
    <version>1.2.2</version>
</dependency>
```


-   初体验(认证及授权)

```kotlin
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

}
```

-   从ini文件中回去定义的用户账号信息

```kotlin
    fun firstTest() {
        // 也可使用IniRealm加载ini文件信息
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
```

-   从数据库中获取权限信息(数据库表信息可由JdbcRealm类中的查询语句了解结构,也可使用自定义的表结构，但需自定义查询语句，但查询结果字段要一致)

```kotlin
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
        // val authenticatQuery = "select password from users where username = ?"
        // jdbcRealm.setAuthenticationQuery(authenticatQuery)
        
        println(subject.isAuthenticated)
        // 检查用户是否具有角色
        subject.checkRoles("admin", "user")
        // 退出
        subject.logout()
        println(subject.isAuthenticated)
    }
```
