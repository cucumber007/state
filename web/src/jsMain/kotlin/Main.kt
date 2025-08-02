import kotlinx.browser.document
import kotlinx.browser.window

// Test function that can be called from JavaScript
fun jsTest(message: String = "Test from Kotlin"): String {
    println("JS Test called with message: $message")
    return "Kotlin test function executed successfully: $message"
}

fun main() {
    // Create a visual indicator that Kotlin is loaded
    document.getElementById("root")?.innerHTML = """
        <div style="
            position: fixed; 
            top: 10px; 
            right: 10px; 
            background: #28a745; 
            color: white; 
            padding: 10px 15px; 
            border-radius: 5px; 
            font-family: Arial, sans-serif;
            font-size: 14px;
            z-index: 1000;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        ">
            ✅ Kotlin Loaded
        </div>
    """.trimIndent()
    
    // Create a simple DOM manipulation
    document.getElementById("root")?.innerHTML = """
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
    
    // Expose the test function to global scope for JavaScript testing
    window.asDynamic().jsTest = { message: String? ->
        jsTest(message ?: "Default test message")
    }
    
    println("Kotlin JavaScript initialization complete!")
} 
