package hht.dragon.shiro.config

import hht.dragon.shiro.MyRealm
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.credential.HashedCredentialsMatcher
import org.apache.shiro.mgt.DefaultSecurityManager
import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.spring.LifecycleBeanPostProcessor
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.web.mgt.WebSecurityManager
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.web.filter.DelegatingFilterProxy


/**
 * Shiro配置类.
 * User: huang
 * Date: 18-7-8
 */
@Configuration
@Order(1)
class ShiroConfig {

    /**
     * Shiro过滤器.
     */
    @Bean
    fun shiroFilter(securityManager: SecurityManager) : ShiroFilterFactoryBean {
        println("ShiroFilter")
        val factoryBean = ShiroFilterFactoryBean()

        // 设置SecurityManager
        factoryBean.securityManager = securityManager

        // 设置登录界面请求，不设置则默认访问根目录下的"/login.jsp"
        factoryBean.loginUrl = "/static/login.html"
        // 设置登录成功后的跳转链接
        factoryBean.successUrl = "/index"
        // 设置未授权界面
        factoryBean.unauthorizedUrl = "/403"

        // 拦截器
        val filterChainDefinitionMap = LinkedHashMap<String, String>()

        // 配置不会被拦截的链接,按顺序判断
        // anon ：  请求可匿名访问
        filterChainDefinitionMap["/static/**"] = "anon"
        filterChainDefinitionMap["/login"] = "anon"

        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap["/logout"] = "logout"

        filterChainDefinitionMap["/user"] = "user"

        // 拦截器会按配置从上往下检查，所以"/**"请求放在最后
        // authc : 所有url都必须认证通过才可以访问
        filterChainDefinitionMap["/**"] = "authc, roles[admin]"

        factoryBean.filterChainDefinitionMap = filterChainDefinitionMap
        println("Shiro拦截器配置完毕" )

        return factoryBean
    }

    /**
     * 配置SecurityManager.
     */
    @Bean
    fun securityManager() : SecurityManager {
        var manager = DefaultSecurityManager()
        manager = DefaultWebSecurityManager()
        manager.setRealm(getRealm())
        SecurityUtils.setSecurityManager(manager)
        return manager
    }

    /**
     * 配置自定义Realm.
     */
    @Bean
    fun getRealm() : MyRealm {
        val realm = MyRealm()
        realm.credentialsMatcher = hashedCredentialsMatcher()
        return realm
    }

    /**
     * 配置加密.
     */
    @Bean
    fun hashedCredentialsMatcher() : HashedCredentialsMatcher {
        val matcher = HashedCredentialsMatcher()
        matcher.hashAlgorithmName = "md5"
        matcher.hashIterations = 1
        return matcher
    }

    @Bean
    fun lifecycleBeanPostProcessor(): LifecycleBeanPostProcessor {
        return LifecycleBeanPostProcessor()
    }

    @Bean
    fun authorizationAttributeSourceAdvisor(securityManager: SecurityManager): AuthorizationAttributeSourceAdvisor {
        val authorizationAttributeSourceAdvisor = AuthorizationAttributeSourceAdvisor()
        authorizationAttributeSourceAdvisor.securityManager = securityManager
        return authorizationAttributeSourceAdvisor
    }

}