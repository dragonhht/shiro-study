package hht.dragon.shiro

import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.SimpleAuthenticationInfo
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.util.ByteSource

/**
 * 自定义Realm.
 * User: huang
 * Date: 18-7-8
 */
class MyRealm : AuthorizingRealm() {

    // 模拟数据库中的用户权限
    private val userMap = mapOf("huang" to  "21e7f4ea9b773045b4a0fef86fe3b4d2")

    init {
        super.setName("myRealm")
    }


    /**
     * 认证.
     */
    override fun doGetAuthenticationInfo(token: AuthenticationToken?): AuthenticationInfo? {
        // 从主体传来的信息中获取用户名
        val userName = token!!.principal as String
        // 通过用户名获取凭证
        val password = getUserNamePassword(userName)
        if (password === null) {
            return null
        }
        val simpleAuthenticationInfo = SimpleAuthenticationInfo(userName, password, "myRealm")
        // 设置盐
        simpleAuthenticationInfo.credentialsSalt = ByteSource.Util.bytes("huang")
        return simpleAuthenticationInfo
    }

    /**
     * 模拟查询用户密码.
     */
    private fun getUserNamePassword(userName: String): String? {
        return userMap[userName]
    }

    /**
     * 授权.
     */
    override fun doGetAuthorizationInfo(collection: PrincipalCollection?): AuthorizationInfo? {
        // 获取用户信息
        val userName = collection!!.primaryPrincipal as String
        // 获取用户权限
        val roles = getRolesByUserName(userName)

        val permissions = getPerssionsByUserName(userName)

        val simpleAuthorizationInfo = SimpleAuthorizationInfo()
        // 设置权限
        simpleAuthorizationInfo.stringPermissions = permissions
        // 设置角色
        simpleAuthorizationInfo.roles = roles
        return simpleAuthorizationInfo
    }

    /**
     * 模拟获取用户权限信息.
     */
    private fun getPerssionsByUserName(userName: String): Set<String> {
        val perssions = HashSet<String>()
        perssions.add("user:delete")
        return perssions
    }

    /**
     * 模拟获取用户角色.
     */
    private fun getRolesByUserName(userName: String): Set<String> {
        val roles = HashSet<String>()
        roles.add("admin")
        roles.add("user")
        return roles
    }
}