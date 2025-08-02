import React, { useState, useEffect } from "react";

const App: React.FC = () => {
  const [kotlinReady, setKotlinReady] = useState(false);

  useEffect(() => {
    // Wait for Kotlin function to be available
    const checkKotlinFunction = () => {
      if (typeof (window as any).jsTest === "function") {
        setKotlinReady(true);
        console.log("Kotlin jsTest function is ready!");
      } else {
        // Retry after a short delay
        setTimeout(checkKotlinFunction, 100);
      }
    };

    checkKotlinFunction();
  }, []);

  const handleTestClick = () => {
    if (kotlinReady) {
      // Call the Kotlin jsTest function
      const result = (window as any).jsTest("Test from React TypeScript!");
      alert(`Kotlin function result: ${result}`);
    } else {
      alert(
        "Kotlin jsTest function not ready yet. Please wait a moment and try again."
      );
    }
  };

  return (
    <div>
      <h1>Hello React TypeScript!</h1>
      <p>This is a simple React app with TypeScript and no styles.</p>
      <p>
        Kotlin function status: {kotlinReady ? "✅ Ready" : "⏳ Loading..."}
      </p>
      <button
        onClick={handleTestClick}
        disabled={!kotlinReady}
        style={{
          padding: "10px 20px",
          fontSize: "16px",
          cursor: kotlinReady ? "pointer" : "not-allowed",
          opacity: kotlinReady ? 1 : 0.6,
        }}
      >
        Test Kotlin Function
      </button>
    </div>
  );
};

export default App;
