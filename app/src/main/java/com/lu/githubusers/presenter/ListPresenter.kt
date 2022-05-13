package com.lu.githubusers.presenter

import com.lu.githubusers.model.ListModel
import com.lu.githubusers.view.ListView

abstract class ListPresenter(var listView: ListView) :Presenter{
    lateinit var listModel : ListModel

    override fun getInto() {
        TODO("Not yet implemented")
    }

    override fun getList() {
        listView.getList(listModel.since,listModel.per_page)
    }

}