package hht.dragon.shiro.controller

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * Controller.
 * User: huang
 * Date: 18-7-8
 */
@RestController
class IndexController {

    /**
     * 首页.
     */
    @GetMapping("/index")
    fun index() : Mono<String> {
        return Mono.just("首页")
    }

    /**
     * 模拟登录界面请求.
     */
    @GetMapping("/login")
    fun login(userName: String, password: String) : Mono<String> {
        val subject = SecurityUtils.getSubject()

        val token = UsernamePasswordToken(userName, password)
        return try {
            subject.login(token)
            Mono.just("登录成功")
        } catch (e : Exception) {
            e.printStackTrace()
            Mono.just("登录失败")
        }
    }

    @GetMapping("/user")
    fun user() : Mono<String> {
        return Mono.just("user")
    }

    /**
     * 模拟跳转未授权界面.
     */
    @GetMapping("/403")
    fun notAuth() : Mono<String> {
        return Mono.just("403")
    }

    /**
     * 使用注解设定访问权限角色
     */
    @RequiresRoles("admin")
    @GetMapping("/test")
    fun test() : Mono<String> {
        return Mono.just("测试注解")
    }

}