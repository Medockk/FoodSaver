//
//  AuthScaffoldView.swift
//  iosApp
//
//  Created by student on 28.04.2026.
//

import SwiftUI

struct AuthScaffoldView<Content: View>: View {
    
    let title: String
    let subtitle: String
    let showBackButton: Bool
    @ViewBuilder let content: () -> Content
    
    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        VStack(spacing: 0) {
            
            /// header
            ZStack(alignment: .center) {
                Color(hex: "121223").ignoresSafeArea()
                    .frame(height: 300)
                
                VStack {
                
                    Text(title)
                        .foregroundColor(.white)
                        .font(.system(size: 30))
                        .fontWeight(.bold)
                    
                    Text(subtitle)
                        .foregroundStyle(.white.opacity(0.85))
                }
            }
            
            VStack {
                content()
                    .frame(maxWidth: .infinity)
            }
            .padding(24)
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
            .background(.white)
            .clipShape(UnevenRoundedRectangle(topLeadingRadius: 24, topTrailingRadius: 24))
            .offset(y: -40)
            .padding(.bottom, -40)
            
            Spacer()
        }
        .ignoresSafeArea(edges: .top)
        .background(.white)
        
    }
}

struct AuthScaffoldView_Previews: PreviewProvider {
    static var previews: some View {
        AuthScaffoldView(
            title: "Login",
            subtitle: "Subtitle for login screen",
            showBackButton: false,
            content: {
            Text("igkwkb")
        })
    }
}
