package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.related

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.ProfileRelatedAdapter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.RelatedListAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileFragmentNotifier
import javax.inject.Inject

class ProfileRelatedFragment : BaseFeedFragment<Related>(), ProfileRelatedView, ProfileFragmentNotifier {
    val username by lazy { (activity as ProfileActivity).username }
    @Inject override lateinit var feedAdapter : ProfileRelatedAdapter
    @Inject lateinit var presenter : ProfileRelatedPresenter
    lateinit var dataFragment : DataFragment<PagedDataModel<List<Related>>>
    companion object {
        val DATA_FRAGMENT_TAG = "PROFILE_RELATED_LINKS_FRAGMENT"

        fun newInstance() : ProfileRelatedFragment {
            val fragment = ProfileRelatedFragment()
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        dataFragment = childFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        dataFragment.data?.apply {
            presenter.page = page
        }
        presenter.subscribe(this)
        initAdapter(dataFragment.data?.model)
    }

    override fun loadData(shouldRefresh: Boolean) {
        presenter.loadData(shouldRefresh)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::dataFragment.isInitialized)
            dataFragment.data = PagedDataModel(presenter.page , data)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }

    override fun removeDataFragment() {
        if (isAdded) childFragmentManager.removeDataFragment(dataFragment)
    }
}