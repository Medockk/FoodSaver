import SwiftUI
import ComposeApp


struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return MainAppControllerKt.MainAppController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

