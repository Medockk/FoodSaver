import SwiftUI
import Shared

@main
struct iOSApp: App {

    init() {
        PlatformKt.initKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}