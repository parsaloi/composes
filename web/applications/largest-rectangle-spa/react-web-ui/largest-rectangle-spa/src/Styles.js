import { createGlobalStyle } from 'styled-components';
import { theme } from './Theme';

export const GlobalStyles = createGlobalStyle`
  body {
    font-family: ${theme.fonts.main};
    margin: 0;
    padding: 0;
    background-color: ${theme.colors.background};
    color: ${theme.colors.text};
  }
  .layout {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    min-width: 100%;
  }
  .main-content {
    display: flex;
    flex: 1;
    min-width: 100%;
  }
  .content {
    flex: 1;
    padding: 20px;
  }
  .icon-button {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s ease;
  }
  .icon-button:hover {
    background-color: ${theme.colors.secondary};
  }
  .icon {
    margin-right: 10px;
  }
  .output-container {
    margin-top: 20px;
    padding: 20px;
    background-color: white;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  }
  .message-dialog-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .message-dialog {
    background-color: white;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  }
  .input-widget {
    margin-bottom: 20px;
  }
  .matrix-input {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  .matrix-row {
    display: flex;
    gap: 10px;
  }
  .labelled-input {
    display: flex;
    flex-direction: column;
  }
  .labelled-input label {
    margin-bottom: 5px;
  }
  .labelled-input input {
    padding: 5px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
`;
