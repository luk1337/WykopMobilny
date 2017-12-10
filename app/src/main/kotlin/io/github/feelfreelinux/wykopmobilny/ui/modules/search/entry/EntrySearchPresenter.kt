package io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry

import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class EntrySearchPresenter(val subscriptionHelperApi: SubscriptionHelperApi, val searchApi: SearchApi) : BasePresenter<EntrySearchView>() {
    var page = 1
    fun searchEntries(q : String, shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        subscriptionHelperApi.subscribe(searchApi.searchEntries(page, q),
                {
                    when {
                        it.isNotEmpty() -> {
                            page++
                            view?.addDataToAdapter(it, shouldRefresh)
                        }
                        page == 1 -> view?.addDataToAdapter(it, true)
                        else -> view?.disableLoading()
                    }
                },
                { view?.showErrorDialog(it) }, this)
    }
}