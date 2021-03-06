package io.github.feelfreelinux.wykopmobilny

import android.content.Context
import android.support.multidex.MultiDex
import com.bugsnag.android.Bugsnag
import com.codemonkeylabs.fpslibrary.TinyDancer
import com.evernote.android.job.JobManager
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.feelfreelinux.wykopmobilny.di.DaggerAppComponent
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationJobCreator
import javax.inject.Inject


class WykopApp : DaggerApplication() {
    @Inject lateinit var jobCreator : WykopNotificationJobCreator
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    companion object {
        val WYKOP_API_URL = "https://a2.wykop.pl"
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        TinyDancer.create()
                .show(this)
        AndroidThreeTen.init(this)
        JobManager.create(this).addJobCreator(jobCreator)
        if (!BuildConfig.DEBUG) {
            Bugsnag.init(this, "3b54bcbf963f13ae9aa8f8376ea7c1cc")
            Bugsnag.setReleaseStage(BuildConfig.BUILD_TYPE)
            Bugsnag.setProjectPackages("io.github.feelfreelinux.wykopmobilny")
        }
    }
}
