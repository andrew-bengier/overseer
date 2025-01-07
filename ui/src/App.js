import React from 'react';
import icon from './icon.svg';
import './App.css';

import { getApiInfo } from './services/InfoService';

function App() {
  React.useEffect(() => {
    getApiInfo()
      .then(response => console.log(response.data))
      .catch(error => console.log(error));
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <img src={icon} className="App-logo" alt="icon" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
