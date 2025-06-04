package com.amolina.presentation.ui.components

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amolina.domain.model.City

class ErrorPagingSource : PagingSource<Int, City>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, City> {
        return LoadResult.Error(Exception("Network Error"))
    }

    override fun getRefreshKey(state: PagingState<Int, City>): Int? = null
}
