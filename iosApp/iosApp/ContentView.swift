import SwiftUI
import Shared

struct ContentView: View {
    
//    @StateObject private var router = NavigationRouter()
    
    var body: some View {
        VStack {
            
        }
//        NavigationStack(path: $router.path) {
//            LoginView()
//                .navigationDestination(for: Route.self) { route in
////                    buildView(for: route)
//                }
//        }.environmentObject(router)
    }
    
//    @ViewBuilder
//    private func buildView(for route: Route) -> some View {
//        switch route {
//        case is Route.AuthGraph:
//            LoginView()
//        default:
//            Button(action: {}, label: {})
//        }
//    }
}

#Preview {
    ContentView()
}
