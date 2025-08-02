import { useState } from "react";
import "./App.css";
import "@kotlin/kotlin-kotlin-stdlib.js";
import "@kotlin/kotlin-js-react.js";

// This will be available after we build the Kotlin code
declare const com: {
  example: {
    KotlinApp: {
      new (): {
        greet(name: string): string;
      };
    };
  };
};

function App() {
  const [message, setMessage] = useState("");

  const handleClick = () => {
    const app = new com.example.KotlinApp();
    setMessage(app.greet("React"));
  };

  return (
    <div className="App">
      <h1>React + KotlinJS Demo</h1>
      <button onClick={handleClick}>Greet from Kotlin</button>
      {message && <p>{message}</p>}
    </div>
  );
}

export default App;
