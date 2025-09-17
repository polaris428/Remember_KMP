package com.devlog.remember.utils

class AndroidAnalytics(
    private val mixpanel: com.mixpanel.android.mpmetrics.MixpanelAPI
) : AnalyticsDelegate {
    override fun track(event: String, propsJson: String?) {
        if (propsJson != null) {
            val json = org.json.JSONObject(propsJson)
            mixpanel.track(event, json)
        } else {
            mixpanel.track(event)
        }
    }
}

