package com.kcibald.services.fronting.utils

import com.kcibald.services.ServiceClient
import com.uchuhimo.konf.Config
import com.wusatosi.recaptcha.RecaptchaClient
import io.vertx.ext.auth.jwt.JWTAuth

interface SharedObjects {
    val config: Config
    val recaptchaClient: RecaptchaClient?
    val jwtAuth: JWTAuth

    /**
     * For testing intercept
     */
    fun checkServiceClientOverride(serviceName: String): ServiceClient?

    companion object {
        fun createDefault(
            config: Config,
            // null if disabled
            recaptchaClient: RecaptchaClient?,
            jwtAuth: JWTAuth
        ): SharedObjects {
            data class SharedObjectsImpl(
                override val config: Config,
                override val recaptchaClient: RecaptchaClient?,
                override val jwtAuth: JWTAuth
            ) : SharedObjects {
                override fun checkServiceClientOverride(serviceName: String): ServiceClient? = null
            }

            return SharedObjectsImpl(
                config, recaptchaClient, jwtAuth
            )
        }
    }
}

internal inline fun <reified T : ServiceClient> SharedObjects.getService(name: String, otherWise: () -> T): T =
    this.checkServiceClientOverride(name) as? T ?: otherWise()