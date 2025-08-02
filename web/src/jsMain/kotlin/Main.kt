import kotlinx.browser.document
import kotlinx.browser.window

fun main() {
    println("Hello from Kotlin Multiplatform!")
    
    // Create a simple DOM manipulation
    document.getElementById("app")?.innerHTML = """
        <div style="padding: 20px; font-family: Arial, sans-serif;">
            <h1 style="color: #333;">Hello from Kotlin Multiplatform!</h1>
            <p style="color: #666;">This JavaScript was generated from Kotlin code.</p>
            <button onclick="showMessage()" style="padding: 10px 20px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">
                Click me!
            </button>
        </div>
    """.trimIndent()
    
    // Expose a function to global scope for the button click
    window.asDynamic().showMessage = {
        window.alert("Hello from Kotlin-generated JavaScript!")
    }
} 
