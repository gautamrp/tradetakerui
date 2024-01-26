import React from "react";
import "./index.css";
//import App from "./App";
import Triggers from "./Triggers";
import reportWebVitals from "./reportWebVitals";
import { createRoot } from "react-dom/client";
import Levels from "./Levels";

const triggersElement = document.getElementById("triggers");
const triggers = createRoot(triggersElement);
triggers.render(
  <React.StrictMode>
    <Triggers />
  </React.StrictMode>
);

const levelsElement = document.getElementById("levels");
const levels = createRoot(levelsElement);
 levels.render(
   <React.StrictMode>
     <Levels />
   </React.StrictMode>
 );

// const rootElement = document.getElementById("root");
// const root = createRoot(rootElement);
// root.render(
//   <React.StrictMode>
//     <App />
//   </React.StrictMode>
// );

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
