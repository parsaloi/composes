import { theme } from '../Theme';

export default function MessageDialog({ message, onClose }) {
  return (
    <div className="message-dialog-overlay">
      <div className="message-dialog">
        <div className="text-text mb-4">{message}</div>
        <button
          onClick={onClose}
          className="px-4 py-2 bg-error text-white rounded hover:bg-red-700"
        >
          Close
        </button>
      </div>
    </div>
  );
}
