package com.github.starter.core.filters

class RequestTimingFilters {
    private constructor() {
    }

    companion object {
        fun newInstance(log: Boolean): RequestTimingFilter {
            return RequestTimingFilter(log)
        }
    }

}
