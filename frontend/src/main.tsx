import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.tsx';
import './styles.css';

// document.getElementById returns HTMLElement | null, and createRoot will not
// accept null. That null has to be handled here rather than assumed away.
// create-vite's own template writes getElementById('root')! instead; the
// assertion is only honest when you can point at the line of index.html that
// guarantees the element.
const rootElement = document.getElementById('root');

if (!rootElement) {
  throw new Error('Cannot start the app: index.html has no element with id "root".');
}

ReactDOM.createRoot(rootElement).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
