// iOSApp (Swift)
import UIKit
import ComposeApp   // 또는 'shared' 등 KMP 프레임워크 이름
import Mixpanel

class IOSAnalytics: NSObject, AnalyticsDelegate {
    override init() {
        super.init()
        // Mixpanel 초기화
        Mixpanel.initialize(token: "cb6660b7534b41cbba6bf01b403eb908", trackAutomaticEvents: true)
    }

    func track(event: String, propsJson: String?) {
        var properties: Properties? = nil
        if let s = propsJson, let data = s.data(using: .utf8) {
            if let obj = try? JSONSerialization.jsonObject(with: data) as? [String: Any] {
                // Any → MixpanelType으로 변환
                var converted: Properties = [:]
                for (k, v) in obj {
                    if let v = v as? MixpanelType {
                        converted[k] = v
                    }
                }
                properties = converted
            }
        }
        if let p = properties {
            Mixpanel.mainInstance().track(event: event, properties: p)
        } else {
            Mixpanel.mainInstance().track(event: event)
        }
    }

}


class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(
      _ application: UIApplication,
      didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        // Kotlin object는 Swift에서 보통 `Analytics.shared` 식으로 노출됩니다.
        Analytics.shared.install(delegate: IOSAnalytics())
        return true
    }
}
