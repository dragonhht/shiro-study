package hht.dragon.shiro.filters

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 自定义过滤器.
 *
 * @author: huang
 * @Date: 2019-4-11
 */
class CustomFiter: Filter {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun init(config: FilterConfig?) {

    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        log.info("自定义过滤器执行：{}, 可在此处做单点登录处理", this::class.java.simpleName)
        chain!!.doFilter(request, response)
    }

    override fun destroy() {

    }
}