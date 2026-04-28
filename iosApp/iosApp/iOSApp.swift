import SwiftUI
import Shared

@main
struct iOSApp: App {
    
    init() {
        print("Init")
        
//        let isPreview = ProcessInfo.processInfo.environment["XCODE_RUNNING_FOR_PREVIEWS"] == "1"
//        if !isPreview {
////            InitSharedKoin_iosKt.doInitIosKoin()
//        }
    }
    

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
