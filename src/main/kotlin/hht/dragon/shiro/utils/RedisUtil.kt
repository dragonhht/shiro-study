package hht.dragon.shiro.utils

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

/**
 * Redis工具包.
 * User: huang
 * Date: 18-7-9
 */
@Component
class RedisUtil {

    @Resource
    lateinit var redis : StringRedisTemplate

    fun test() {
        println(redis)
    }

    fun set(key: String, value: String) {
        redis.opsForValue().set(key, value)
    }

    fun expire(key: String, i: Long) {
        redis.expire(key, i.toLong(), TimeUnit.SECONDS)
    }

    fun get(key: String): String? {
        return redis.opsForValue().get(key)
    }

    fun del(key: String) {
        redis.delete(key)
    }

    fun keys(key: String): Any {
        return redis.keys(key)
    }


}