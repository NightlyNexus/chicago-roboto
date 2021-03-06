package com.chicagoroboto.features.sessions

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker
import javax.inject.Inject

class SessionListView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        RecyclerView(context, attrs, defStyle), SessionListMvp.View {

    private val adapter: SessionAdapter

    @Inject lateinit var presenter: SessionListMvp.Presenter
    @Inject lateinit var sessionNavigator: SessionNavigator

    init {
        context.getComponent<MainComponent>().sessionListComponent().inject(this)

        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        addItemDecoration(SessionItemDecoration(context))
        adapter = SessionAdapter({ session ->
            sessionNavigator.showSession(session)
        })
        super.setAdapter(adapter)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttach(this)
    }

    override fun onDetachedFromWindow() {
        presenter.onDetach()
        super.onDetachedFromWindow()
    }

    fun setDate(date: String) {
        presenter.setDate(date)
    }

    override fun showNoSessions() {
        // todo
    }

    override fun showSessions(sessions: List<Session>) {
        adapter.sessions.clear()
        adapter.sessions.addAll(sessions)
        adapter.notifyDataSetChanged()
    }

    override fun showSpeakers(speakers: Map<String, Speaker>) {
        adapter.speakers.clear()
        adapter.speakers.putAll(speakers)
        adapter.notifyDataSetChanged()
    }
}

