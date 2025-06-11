import { useState } from "react";
import "./App.css";

// This will be available after we build the Kotlin code
declare const KotlinApp: {
  new (): {
    greet(name: string): string;
  };
};

function App() {
  const [message, setMessage] = useState("");

  const handleClick = () => {
    const app = new KotlinApp();
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
