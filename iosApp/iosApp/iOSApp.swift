import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        InitComposeAppKoinKt.doInitComposeAppKoin()
    }

    var body: some Scene {
        WindowGroup {
            ComposeView()
                .ignoresSafeArea(.all)
        }
    }
}
